import java.util.ArrayList;

import MKAgent.Board;

public class MiniMax {
	public int minimax(Board board, Side side, int depth, boolean maximizingPlayer) {
		if (depth == 0 || Board.children() == null) {
			return valueFunction(Board);
		}
		if (maximizingPlayer == true) {
			int value = -1000000000;
			Board[] children = board.children();
			for (int i = 0; i < children.length(); i++) {
				value = max(value, minimax(children[i], depth - 1, false));
			}
			return value;
		} else {
			int value = +1000000000;
			Board[] children = Board.children();
			for (int i = 0; i < children.length(); i++) {
				value = min(value, minimax(children[i], depth - 1, false));
			}
			return value;
		}
	}

	private Board[] getchildren(Board board, Side side) {
		ArrayList<Board> children = new ArrayList<StrBoarding>();
		for (int i = 0; i < board.getNoOfHoles() / 2; i++) {
			if (board.getSeeds(side, i) > 0) {
				Move newmove = new Move(side, i);
				Kalah kalah = new Kalah(board);
			}
		}
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
