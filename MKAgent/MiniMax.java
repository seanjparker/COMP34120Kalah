package MKAgent;
import java.util.ArrayList;



public class MiniMax {
	
	public static ValueObj minimax(Board board, Side side, int depth, boolean maximizingPlayer) {
		if (depth == 0 || getChildren(board,side) == null) {
			ValueObj value= new ValueObj();
			value.setValue(valueFunction(board, maximizingPlayer));
			return value;
		}
		if (maximizingPlayer == true) {
			ValueObj value= new ValueObj();
			value.setValue(-100000000);
			double max = -1000000;
			double newMax;
			int move = 0;
			ArrayList<Board> children = getChildren(board,side);
			for (int i = 0; i < children.size(); i++) {
				if(children.get(i)!=null){
					newMax=Math.max(value.getValue(), minimax(children.get(i), side, depth - 1, false).getValue());
					if(newMax>max){
						max=newMax;
						move=i;
						value.setValue(newMax);
						value.setMove(move);
					}
				}
			}
			return value;
		} else {
			ValueObj value= new ValueObj();
			value.setValue(100000000);
			ArrayList<Board> children = getChildren(board,side);
			double min = 10000000;
			double newMin;
			int move;
			for (int i = 0; i < children.size(); i++) {
				if(children.get(i)!=null){
					newMin=Math.min(value.getValue(), minimax(children.get(i), side, depth - 1, true).getValue());
					if(newMin<min){
						min=newMin;
						move=i;
						value.setValue(newMin);
						value.setMove(move);
					}
				}
			}
			return value;
		}
	}

	private static ArrayList<Board> getChildren(Board board, Side side) {
	  ArrayList<Board> children = new ArrayList<Board>();
	  Board originalBoard = new Board(board);

	  for (int i = 1; i <= board.getNoOfHoles(); i++) {
	    if (board.getSeeds(side, i) > 0) {
	      Move newmove = new Move(side, i);
	      Kalah.makeMove(board,newmove);
	      children.add(board);
	      board= new Board(originalBoard);

	    }else{
				children.add(null);
			}
	  }
	  return children;
	}
	

	// TODO(whoever): Write a better value function.
	private static double valueFunction(final Board board, final boolean maximizingPlayer) {
		final int player = maximizingPlayer ? 0 : 1;
		final int opponent = !maximizingPlayer ? 1 : 0;
		int numStonesOurPits = 0;
		int numStonesOpponentPits = 0;
		for (int i = 0; i < board.getBoard()[0].length; i++) {
			numStonesOurPits += board.getBoard()[player][i];
			numStonesOpponentPits += board.getBoard()[opponent][i];
		}

		return (board.getBoard()[player][0] - board.getBoard()[opponent][0]) + (numStonesOurPits - numStonesOpponentPits);
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
