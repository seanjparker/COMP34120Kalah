package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.lang.Math;

class Search implements Runnable{

	private Thread t;
	private String threadName;
	private Board board;
	private Side side;
	private ValueObj valueObject;
	private Terminate isTerminating;
	private static HashMap<Board, ValueObj> transposTable;

	Search( String name, Board board, Side side, Terminate it, ValueObj vt) {
		this.threadName = name;
		this.board= board;
		this.side = side;
		this.isTerminating = it;
		this.valueObject = vt;
	}

	public void run() {
		try {
			search(board, side, 7, this.isTerminating);
		} catch (Exception e) {
			System.out.println("Thread " +  threadName + " interrupted.");
		}
	}

	public void start () {
		if (t == null) {
			t = new Thread (this, threadName);
			t.start ();
		}
	}

	public void search(Board board, Side side, ValueObj vt, Terminate t) {
		// ValueObj bestMove = null;
		for (int i = 1; i <= 20; i++) {
			vt = MTDf(board, side, vt, i);
			if (t.getIsTerminating()) break;
		}
		// return bestMove;
	}

	private ValueObj MTDf(Board board, Side side, ValueObj first, int depth) {
		ValueObj g = first;
		int beta, upperbound = Integer.MAX_VALUE, lowerbound = Integer.MIN_VALUE;
		do {
			if (g.getValue() == lowerbound)
				beta = g.getValue() + 1;
			else
				beta = g.getValue();

			g = alphaBetaTT(board, side, beta - 1, beta, depth);

			if (g.getValue() < beta)
				upperbound = g.getValue();
			else
				lowerbound = g.getValue();
		} while (lowerbound < upperbound);
		return g;
	}

	private static ValueObj alphaBetaTT(Board board, Side side, int depth, int alpha, int beta) {
		ValueObj value = getTTValueObj(board);
		if (value != null && value.getDepth() >= depth) {
			if (value.getType() == 0) // stored value is exact
				return value;
			if (value.getType() == -1 && value.getValue() > alpha)
				alpha = value.getValue(); // update lowerbound alpha if needed
			else if (value.getType() == 1 && value.getValue() < beta)
				beta = value.getValue(); // update upperbound beta if needed
			if (alpha >= beta)
				return value; // if lowerbound surpasses upperbound
		}

		if (depth == 0 || getSortedChildren(board, side).size() == 0) {
			value.setValue(Evaluation.evaluate(board, side));
			if (value.getValue() <= alpha) // a lowerbound value
				storeValueObjTT(board, value, -1, depth);
			else if (value.getValue() >= beta)
				storeValueObjTT(board, value, 1, depth);
			else
				storeValueObjTT(board, value, 0, depth);
			return value;
		}

		List<ValueObj> children = getSortedChildren(board, side);
		ValueObj best = new ValueObj();
		best.setValue(Integer.MIN_VALUE);

		for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);
			Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
			value = alphaBetaTT(nextBoard, side.opposite(), depth - 1, -beta, -alpha);
			value.setValue(-value.getValue());

			// Value is a better move than best
			if (value.compareTo(best) > 0) {
				value.setMove(children.get(i).getMove());
				best = value.clone();
			}
			if (best.getValue() > alpha)
				alpha = best.getValue();
			if (best.getValue() >= beta)
				break; // Cut off the current branch
		}

		if (best.getValue() <= alpha) // a lowerbound value
			storeValueObjTT(board, best, -1, depth);
		else if (best.getValue() >= beta) // a upperbound value
			storeValueObjTT(board, best, 1, depth);
		else	// a true minimax value
			storeValueObjTT(board, best, 0, depth);
		return best;
	}

	/* private static ValueObj alphaBeta(Board board, Side side, int depth, double alpha, double beta) {
		ValueObj value = new ValueObj();
		if (depth == 0 || getSortedChildren(board, side).size() == 0) {
			value.setValue(Evaluation.evaluate(board, side));
			return value;
		}
		List<ValueObj> children = getSortedChildren(board, side);
		ValueObj best = new ValueObj();
		best.setValue(Integer.MIN_VALUE);

		for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);
			Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
			value = alphaBeta(nextBoard, side.opposite(), depth - 1, -beta, -alpha);
			value.setValue(-value.getValue());

			// Value is a better move than best
			if (value.compareTo(best) > 0) {
				value.setMove(children.get(i).getMove());
				best = value.clone();
			}
			if (best.getValue() > alpha)
				alpha = best.getValue();
			if (alpha >= beta)
				break; // Cut off the current branch
		}

		return best;
	} */

	public List<ValueObj> getSortedChildren(Board board, Side side) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				children.add(new ValueObj(i, Evaluation.quickEval(board, side)));
			}
		}
		Collections.sort(children);
		return children;
	}

	public static ValueObj getTTValueObj(Board board)
	{
		return transposTable.get(board);
	}

	public static void storeValueObjTT(Board board, ValueObj value, int type, int depth)
	{
		transposTable.put(board, new ValueObj(value.getMove(), value.getValue(), type, depth));
	}
}


// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
