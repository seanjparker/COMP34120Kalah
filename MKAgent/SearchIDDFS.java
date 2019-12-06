package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;
import java.lang.InterruptedException;

class SearchIDDFS implements Runnable{

	private Thread t;
	private String threadName;
	private Board board;
	private Side side;
	private ValueTree valueTree;
	private Terminate isTerminating;

	SearchIDDFS( String name, Board board, Side side, Terminate it, ValueTree vt) {
		this.threadName = name;
		this.board= board;
		this.side = side;
		this.valueTree = vt;
		this.isTerminating = it;
	}

	public void run() {
		try {
			search(board, side, this.valueTree, this.isTerminating);
		} catch (InterruptedException e) {
			System.out.println("Thread " +  threadName + " interrupted.");
		}
	}

	public void start () {
		if (t == null) {
			t = new Thread (this, threadName);
			t.start ();
		}
	}

	public static void search(Board board, Side side, ValueTree valueTree, Terminate it) throws InterruptedException{
		alphaBeta(board, side, Integer.MIN_VALUE, Integer.MAX_VALUE, true, valueTree, it);
	}

	private static void alphaBeta(Board board, Side side, double alpha, double beta, boolean maximizingPlayer, ValueTree valueTree, Terminate it) throws InterruptedException{
		
		// if (it.getIsTerminating() != true && getSortedChildren(board, side, maximizingPlayer).size() != 0) {
		// 	ValueObj value = new ValueObj();
		// 	List<ValueObj> children = getSortedChildren(board, side, maximizingPlayer);

		// 	valueTree.setChildren(children.size());

		// 	for (int i = 0; i < children.size(); i++) {
		// 		children.get(i).setValue(-children.get(i).getValue());
		// 		valueTree.updateValue(children.get(i));
		// 		valueTree.getChildren(i).updateValue(children.get(i));

		// 		Board nextBoard = new Board(board);
		// 		Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
		// 		alphaBeta(nextBoard, side.opposite(), -beta, -alpha, !maximizingPlayer, valueTree.getChildren(i), it);
		// 	}
		// }
	}

	public static List<ValueObj> getSortedChildren(Board board, Side side, boolean maximizingPlayer) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				//children.add(new ValueObj(i, Evaluation.evaluate(board, side, maximizingPlayer)));
			}
		}
		Collections.sort(children);
		return children;
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
