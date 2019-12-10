package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.lang.Math;

class Search implements Runnable {

	private Thread t;
	private String threadName;
	private Board board;
	private Side side;
	private ValueObj valueObject;
	private Terminate isTerminating;


	Search(String name, Board board, Side side, Terminate it, ValueObj vt) {
		this.threadName = name;
		this.board= board;
		this.side = side;
		this.isTerminating = it;
		this.valueObject = vt;
		this.valueObject.setValue(0);
	}

	public void run() {
		try {
			search(board, side, this.valueObject, this.isTerminating);
		} catch (Exception e) {
			System.out.println("Thread " +  threadName + " interrupted.");
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread (this, threadName);
			t.start();
		}
	}
	
	public void join() {
	    if (t != null)
        try {
          t.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
	}

	public void search(Board board, Side side, ValueObj vt, Terminate t) {
		// ValueObj bestMove = null;
		for (int i = 1; i <= 9; i+=2) {
			this.valueObject = alphaBetaTT(board, side, i, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (t.getIsTerminating()) {
				System.err.println("Early quit");
				break;
			}
		}

		// return bestMove;
	}

	// private ValueObj MTDf(Board board, Side side, ValueObj first, int depth) {
	// 	ValueObj g = first;
	// 	int beta, upperbound = Integer.MAX_VALUE, lowerbound = Integer.MIN_VALUE;
	// 	do {
	// 		if (g.getValue() == lowerbound)
	// 			beta = g.getValue() + 1;
	// 		else
	// 			beta = g.getValue();

	// 		g = alphaBetaTT(board, 1, side, depth, beta - 1, beta);

	// 		if (g.getValue() < beta)
	// 			upperbound = g.getValue();
	// 		else
	// 			lowerbound = g.getValue();
	// 	} while (lowerbound < upperbound);
	// 	return g;
	// }

	private ValueObj alphaBetaTT(Board board, Side side, int depth, int alpha, int beta) {
		ValueObj ttentry = TranspositionTable.get(board);
		if (ttentry != null && ttentry.getDepth() >= depth) {
			if (ttentry.getType() == TTType.EXACT) // stored value is exact
				return ttentry;
			if (ttentry.getType() == TTType.LOWERBOUND && ttentry.getValue() > alpha)
				alpha = ttentry.getValue(); // update lowerbound alpha if needed
			else if (ttentry.getType() == TTType.UPPERBOUND && ttentry.getValue() < beta)
				beta = ttentry.getValue(); // update upperbound beta if needed
			if (alpha >= beta)
				return ttentry; // if lowerbound surpasses upperbound
		}
        
		List<ValueObj> children = getSortedChildren(board, side);
        
		if (depth == 0 || children.size() == 0) {
			ValueObj terminal = new ValueObj();
			terminal.setValue(Evaluation.evaluate(board, side));
			// if (terminal.getValue() <= alpha) // a lowerbound value
			// 	TranspositionTable.put(board, terminal, TTType.LOWERBOUND, depth);
			// else if (terminal.getValue() >= beta)
			// 	TranspositionTable.put(board, terminal, TTType.UPPERBOUND, depth);
			// else
			// 	TranspositionTable.put(board, terminal, TTType.EXACT, depth);
			return terminal;
		}

		ValueObj best = new ValueObj();
		ValueObj value = null;
		best.setValue(Integer.MIN_VALUE);

		for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);
			Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
			value = alphaBetaTT(nextBoard, side.opposite(), depth - 1, -beta, -alpha);
			value.setValue(-value.getValue());

			// Value is a better move than best
			if (value.compareTo(best) >= 0) {
				value.setMove(children.get(i).getMove());
				best = value.clone();
			}
			if (best.getValue() > alpha)
				alpha = best.getValue();
			if (best.getValue() >= beta)
				break; // Cut off the current branch
		}

		if (best.getValue() <= alpha) // a lowerbound value
			TranspositionTable.put(board, best, TTType.LOWERBOUND, depth);
		else if (best.getValue() >= beta) // a upperbound value
			TranspositionTable.put(board, best, TTType.UPPERBOUND, depth);
		else	// a true minimax value
			TranspositionTable.put(board, best, TTType.EXACT, depth);
		return best;
	}

	public List<ValueObj> getSortedChildren(Board board, Side side) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				children.add(new ValueObj(i, Evaluation.evaluate(board, side)));
			}
		}
		//Collections.sort(children);
		return children;
	}

	public ValueObj getBestMove() {
		return this.valueObject;
	}
}


// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
