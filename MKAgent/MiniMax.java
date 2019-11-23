import java.util.ArrayList;

import MKAgent.Board;

public class MiniMax {
	public ValueObj minimax(Board board, Side side, int depth, boolean maximizingPlayer) {
		if (depth == 0 || Board.children() == null) {
			return valueFunction(Board);
		}
		if (maximizingPlayer == true) {
			ValueObj value= new ValueObj();
			value.setValue(-100000000);
			Board[] children = board.children();
			int max;
			int newMax;
			int move;
			ArrayList<Board> children = getChildren(board,side);
			for (int i = 0; i < children.length; i++) {
				if(children.get(i)!=null){
					newMax=max(value.getValue, minimax(children.get(i), depth - 1, false));
					if(newMax>max){
						max=newMax;
						move=i;
					}
					value.setValue(newMax);
					value.setMove(move);
				}
			}
			return value;
		} else {
			ValueObj value= new ValueObj();
			value.setValue(100000000);
			ArrayList<Board> children = getChildren(board,side);
			int min;
			int newMin;
			int move;
			Board[] children = Board.children();
			for (int i = 0; i < children.length; i++) {
				if(children.get(i)!=null){
					newMin=min(value.getValue, minimax(children.get(i), depth - 1, true));
					if(newMin<min){
						min=newMin;
						move=i;
					}
					value.setValue(newMin);
					value.setMove(move);
				}
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

	    }else{
				children.add(null);
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
		for (int i = 0; i < board.getBoard()[0].length; i++) {
			numStonesOurPits += board.getBoard()[player][i];
			numStonesOpponentPits += board.getBoard()[opponent][i];
		}

		return (board.getBoard[player][0] - board.getBoard[opponent][0]) + (numStonesOurPits - numStonesOpponentPits);
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
