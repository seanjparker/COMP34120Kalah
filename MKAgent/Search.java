package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class Search {

	public static ValueObj search(Board board, Side side, int depth) {
		return alphaBeta(board, side, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}

	private static ValueObj alphaBeta(Board board, Side side, int depth, double alpha, double beta, boolean maximizingPlayer) {
		ValueObj value = new ValueObj();
		if (depth == 0 || getSortedChildren(board, side, maximizingPlayer).size() == 0) {
			value.setValue(Evaluation.evaluate(board, side, maximizingPlayer));
			return value;
		}
		List<ValueObj> children = getSortedChildren(board, side, maximizingPlayer);
		ValueObj best = new ValueObj();
		best.setValue(Integer.MIN_VALUE);

		for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);
			Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
			value = alphaBeta(nextBoard, side.opposite(), depth - 1, -beta, -alpha, !maximizingPlayer);
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

	public static List<ValueObj> getSortedChildren(Board board, Side side, boolean maximizingPlayer) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				children.add(new ValueObj(i, Evaluation.evaluate(board, side, maximizingPlayer)));
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
