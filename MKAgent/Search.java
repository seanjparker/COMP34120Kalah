package MKAgent;

import java.util.ArrayList;
import java.lang.Math;

public class Search {

	public static ValueObj search(Board board, Side side, int depth) {
		return alphaBeta(board, side, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}

	private static ValueObj alphaBeta(Board board, Side side, int depth, double alpha, double beta, boolean maximizingPlayer) {
		if (depth == 0 || getChildren(board, side) == null) {
			ValueObj value = new ValueObj();
			value.setValue(Evaluation.evaluate(board, side, maximizingPlayer));
			return value;
		}
		if (maximizingPlayer) {
			ValueObj value = new ValueObj();
			value.setValue(Integer.MIN_VALUE);
			int max = Integer.MIN_VALUE;
			int newMax;
			int move = 0;
			ArrayList<Board> children = getChildren(board, side);

			for (int i = 0; i < children.size(); i++) {

				if (children.get(i) != null) {

					newMax = alphaBeta(children.get(i), side.opposite(), depth - 1, alpha, beta, false).getValue();
					alpha = Math.max(alpha, value.getValue());
					if (alpha >= beta) {
						break;
					}
					if (newMax > max) {
						max = newMax;
						move = i;
						value.setValue(newMax);
						value.setMove(move + 1);
					}
				}

			}

			return value;
		} else {
			ValueObj value = new ValueObj();
			value.setValue(Integer.MAX_VALUE);

			ArrayList<Board> children = getChildren(board, side);

			int min = Integer.MAX_VALUE;
			int newMin;
			int move;
			for (int i = 0; i < children.size(); i++) {

				if (children.get(i) != null) {

					newMin = alphaBeta(children.get(i), side.opposite(), depth - 1, alpha, beta, true).getValue();
					beta = Math.min(beta, value.getValue());
					if (alpha >= beta) {
						break;
					}
					if (newMin < min) {
						min = newMin;
						move = i;
						value.setValue(newMin);
						value.setMove(move + 1);
					}
				}
			}

			return value;
		}
	}

	public static ArrayList<Board> getChildren(Board board, Side side) {
		ArrayList<Board> children = new ArrayList<Board>();
		Board originalBoard = new Board(board);
		boolean hasChildren = false;
		for (int i = 1; i <= originalBoard.getNoOfHoles(); i++) {
			if (Kalah.isLegalMove(originalBoard, new Move(side, i))) {
				Move newmove = new Move(side, i);
				Kalah.makeMove(originalBoard, newmove);
				children.add(originalBoard);
				originalBoard = new Board(board);
				hasChildren = true;
			} else {
				children.add(null);
			}
		}
		if (!hasChildren)
			children = null;
		return children;
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
