package MKAgent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.Reader;

/**
 * The main application class. It also provides methods for communication with
 * the game engine.
 */
public class Main {
	/**
	 * Input from the game engine.
	 */
	private static Reader input = new BufferedReader(new InputStreamReader(System.in));

	private static void redirectSystemErr() {
		try {
			System.setErr(new PrintStream(new FileOutputStream(System.getProperty("user.dir") + "/KalahLog" + ".log")));
		} catch (Exception ex) {
			System.err.println("Exception " + ex.getMessage());
		}
	}

	/**
	 * Sends a message to the game engine.
	 * 
	 * @param msg The message.
	 */
	public static void sendMsg(String msg) {
		System.out.print(msg);
		System.out.flush();
	}

	/**
	 * Receives a message from the game engine. Messages are terminated by a '\n'
	 * character.
	 * 
	 * @return The message.
	 * @throws IOException if there has been an I/O error.
	 */
	public static String recvMsg(Board board, Side side) throws IOException {
		StringBuilder message = new StringBuilder();
		int newCharacter;

		ValueTree valueTree = new ValueTree(0, null);
		Terminate t = new Terminate();

		SearchIDDFS R1 = new SearchIDDFS( "Thread-1", board, side, t, valueTree);
      	R1.start();
		
		  do {
			newCharacter = input.read();
			if (newCharacter == -1)
				throw new EOFException("Input ended unexpectedly.");
			message.append((char) newCharacter);
		} while ((char) newCharacter != '\n');

		t.setIsTerminating(true);
		return message.toString();
	}

	/**
	 * The main method, invoked when the program is started.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		redirectSystemErr();
		try {
			Side side = Side.SOUTH;
			String s;
			Board board = new Board(7, 7);
			int howDeep = 9;
			while (true) {
				System.err.println();
				s = recvMsg(board, side);
				System.err.print("Received: " + s);
				try {
					MsgType mt = Protocol.getMessageType(s);
					Protocol.MoveTurn r;
					switch (mt) {
					case START:
						System.err.println("A start.");
						boolean first = Protocol.interpretStartMsg(s);
						System.err.println("Starting player? " + first);
						if (first) {
							Board b = new Board(7, 7);
							ValueObj nextMove = Search.search(b, side, howDeep);
							sendMsg(Protocol.createMoveMsg(nextMove.getMove()));
							System.err.println("MOVE;" + nextMove.getMove());
						} else {
							side = Side.NORTH;
						}
						break;
					case STATE:
						System.err.println("A state.");
						Board b = new Board(7, 7);
						r = Protocol.interpretStateMsg(s, b);

						if(r.move == -1) {
							side = side.opposite();
						}
						if (!r.end && r.again) {
							ValueObj nextMove = Search.search(b, side, howDeep);
							sendMsg(Protocol.createMoveMsg(nextMove.getMove()));
							System.err.println("MOVE;" + nextMove.getMove());
							Kalah.makeMove(board, new Move(side, nextMove.getMove()));
						}
						
						System.err.println("This was the move: " + r.move);
						System.err.println("Is the game over? " + r.end);
						if (!r.end)
							System.err.println("Is it our turn again? " + r.again);
						// System.err.print("The board:\n" + b);
						// }while(r.again && !r.end);
						break;
					case END:
						System.err.println("An end. Bye bye!");
						return;
					}

				} catch (InvalidMessageException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("This shouldn't happen: " + e.getMessage());
		}
	}
}
