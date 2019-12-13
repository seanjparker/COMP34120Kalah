package MKAgent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

/**
 * The main application class. It also provides methods for communication with
 * the game engine.
 */
public class Main {
	/**
	 * Input from the game engine.
	 */
	private static Reader input = new BufferedReader(new InputStreamReader(System.in));

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
	public static String recvMsg() throws IOException {
		StringBuilder message = new StringBuilder();
		int newCharacter;
		
		do {
			newCharacter = input.read();
			if (newCharacter == -1)
				throw new EOFException("Input ended unexpectedly.");
			message.append((char) newCharacter);
		} while ((char) newCharacter != '\n');

		return message.toString();
	}

	/**
	 * The main method, invoked when the program is started.
	 *
	 * @param args Command line arguments.
	 * @throws InterruptedException 
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws InterruptedException, CloneNotSupportedException {
		try {
			Side side = Side.SOUTH;
			String s;
			Board board = new Board(7, 7);
			ValueObj nextMove = new ValueObj();
			while (true) {
				System.err.println();
				s = recvMsg();
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
							Kalah.secondMove = false;


							Search R2 = new Search( board, side, nextMove);
							R2.search(board, side, new ValueObj());
							
							sendMsg(Protocol.createMoveMsg(R2.getBestMove().getMove()));
							System.err.println("MOVE;" + R2.getBestMove().getMove());
						} else {
							side = Side.NORTH;
						}
						break;
					case STATE:
						System.err.println("A state.");
						Board b = new Board(7, 7);
						r = Protocol.interpretStateMsg(s, b);

						board = b.clone();
						if(r.move == -1) {
							side = side.opposite();
						}
						if (r.move >= 0 && r.move <= 2 && Kalah.secondMove) {
							sendMsg(Protocol.createSwapMsg());
							side = side.opposite();
							System.err.println("Swapping sides");
						} else if (!r.end && r.again) {
							Search R2 = new Search(board, side, nextMove);

							R2.search(board, side, new ValueObj());

							sendMsg(Protocol.createMoveMsg(R2.getBestMove().getMove()));
							System.err.println("MOVE;" + Protocol.createMoveMsg(R2.getBestMove().getMove()));

						}

						if (Kalah.secondMove) Kalah.secondMove = false;
						System.err.println("This was the move: " + r.move);
						System.err.println("Is the game over? " + r.end);
						if (!r.end)
							System.err.println("Is it our turn again? " + r.again);
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
