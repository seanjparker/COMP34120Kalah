package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class Search {

	public static ValueObj search(Board board, Side side, int depth, Terminate t) {
		ValueObj bestMove = null;
		for (int i = 1; i <= depth; i++) {
			bestMove = MTDf(board, side, bestMove, i);
			if (t.getIsTerminating()) break;
		}
		return bestMove;
	}

	private static ValueObj MTDf(Board board, Side side, ValueObj first, int depth) {
		ValueObj g = first;
		int beta, upperbound = Integer.MAX_VALUE, lowerbound = Integer.MIN_VALUE;
		do {
			if (g.getValue() == lowerbound)
				beta = g.getValue() + 1;
			else
				beta = g.getValue();
			
			g = alphaBetaTT(board, beta - 1, beta, depth);

			if (g.getValue() < beta)
				upperbound = g.getValue();
			else
				lowerbound = g.getValue();
		} while (lowerbound < upperbound);
		return g;
	}

	private static alphaBetaTT(Board board, int depth, int alpha, int beta) {
		ValueObj value = new ValueObj();
		TTEntry tte = getTTEntry(board.hashValue());
		if (tte != null && tte.depth >= depth) {
			if (tte.type == EXACT) // stored value is exact
				return tte.move;
			if (tte.type == LOWERBOUND && tte.value > alpha)
				alpha = tte.value; // update lowerbound alpha if needed
			else if (tte.type == UPPERBOUND && tte.value < beta)
				beta = tte.value; // update upperbound beta if needed
			if (alpha >= beta)
				return tte.move; // if lowerbound surpasses upperbound
		}

		if (depth == 0 || getSortedChildren(board, side).size() == 0) {
			value.setValue(Evaluation.evaluate(board, side));
			if (value.getValue() <= alpha) // a lowerbound value
				storeTTEntry(board.hashCode(), value, LOWERBOUND, depth);
			else if (value.getValue() >= beta)
				storeTTEntry(board.hashCode(), value, UPPERBOUND, depth);
			else
				storeTTEntry(board.hashCode(), value, EXACT, depth);
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
			storeTTEntry(board.hashCode(), best, LOWERBOUND, depth);
		else if (best.getValue() >= beta) // a upperbound value
			storeTTEntry(board.hashCode(), best, UPPERBOUND, depth);
		else	// a true minimax value
			storeTTEntry(board.hashCode(), best, EXACT, depth);
		
		return best;
	}

	private static ValueObj alphaBeta(Board board, Side side, int depth, double alpha, double beta) {
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
	}

	public static List<ValueObj> getSortedChildren(Board board, Side side) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				children.add(new ValueObj(i, Evaluation.quickEval(board, side)));
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
