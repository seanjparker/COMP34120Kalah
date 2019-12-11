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
	public static String recvMsg(Search R1, Terminate t) throws IOException {
		StringBuilder message = new StringBuilder();
		int newCharacter;

		// R1.start();
		
		do {
			newCharacter = input.read();
			if (newCharacter == -1)
				throw new EOFException("Input ended unexpectedly.");
			message.append((char) newCharacter);
		} while ((char) newCharacter != '\n');

		// t.setIsTerminating(true);
		// R1.join();
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
		redirectSystemErr();
		try {
			Terminate t = new Terminate();
			Side side = Side.SOUTH;
			String s;
			Board board = new Board(7, 7);
			ValueObj nextMove = new ValueObj();
			int howDeep = 9;
			while (true) {
				System.err.println();
				t.setIsTerminating(false);
				Search R1 = new Search( "Thread-1", board, side, t, nextMove);
				s = recvMsg(R1, t);
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


							Search R2 = new Search( "Thread-1", board, side, t, nextMove);
							R2.search(board, side, new ValueObj(), t);
							// t.setIsTerminating(false);
							// R2.start();

							// TimeUnit.SECONDS.sleep(3);

							// t.setIsTerminating(true);
							// R2.join();
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
							Search R2 = new Search("Thread-2", board, side, t, nextMove);
							// t.setIsTerminating(false);
							// R2.start();

							// TimeUnit.SECONDS.sleep(1);

							// t.setIsTerminating(true);
							// R2.join();

							R2.search(board, side, new ValueObj(), t);

							sendMsg(Protocol.createMoveMsg(R2.getBestMove().getMove()));
							System.err.println("MOVE;" + Protocol.createMoveMsg(R2.getBestMove().getMove()));

						}

						if (Kalah.secondMove) Kalah.secondMove = false;
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
