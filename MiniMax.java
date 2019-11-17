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

	private static ArrayList<Board> getChildren(Board board, Side side) {
	  ArrayList<Board> children = new ArrayList<Board>();
	  Board originalBoard = new Board(board);

	  System.out.println(board.getNoOfHoles());
	  for (int i = 1; i <= board.getNoOfHoles(); i++) {
	    System.out.println(i);
	    if (board.getSeeds(side, i) > 0) {
	      Move newmove = new Move(side, i);
	      Kalah.makeMove(board,newmove);
	      children.add(board);
	      board= new Board(originalBoard);

	    }
	  }
	  System.out.println(children);
	  return children;
	  }
	}

	// TODO(whoever): Write a better value function.
	private double valueFunction(final Board board, final boolean maximizingPlayer) {
		final int player = maximizingPlayer ? 0 : 1;
		final int opponent = !maximizingPlayer ? 1 : 0;
		int numStonesOurPits = 0;
		int numStonesOpponentPits = 0;
		for (int i = 0; i < board[0].length; i++) {
			numStonesOurPits += board[player][i];
			numStonesOpponentPits += board[opponent][i];
		}

		return (board.board[player][0] - board.board[opponent][0]) + (numStonesOurPits - numStonesOpponentPits);
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
