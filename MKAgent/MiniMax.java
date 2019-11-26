package MKAgent;
import java.util.ArrayList;



public class MiniMax {

	public static ValueObj minimax(Board board, Side side, int depth, boolean maximizingPlayer) {
		if (depth == 0 || getChildren(board,side) == null) {
			ValueObj value= new ValueObj();
			value.setValue(valueFunction(board, side, maximizingPlayer));
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

					newMax= minimax(children.get(i), side.opposite(), depth - 1, false).getValue();
					if(newMax>max){
						max=newMax;
						move=i;
						value.setValue(newMax);
						value.setMove(move+1);
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

					newMin= minimax(children.get(i), side.opposite(), depth - 1, true).getValue();
					if(newMin<min){
						min=newMin;
						move=i;
						value.setValue(newMin);
						value.setMove(move +1);
					}
				}
			}

			return value;
		}
	}

	public static ArrayList<Board> getChildren(Board board, Side side) {
	  ArrayList<Board> children = new ArrayList<Board>();
	  Board originalBoard = new Board(board);
    boolean hasChildren=false;
	  for (int i = 1; i <= originalBoard.getNoOfHoles(); i++) {
	    if (Kalah.isLegalMove(originalBoard, new Move(side, i))) {
	      Move newmove = new Move(side, i);
	      Kalah.makeMove(originalBoard,newmove);
	      children.add(originalBoard);
	      originalBoard= new Board(board);
				hasChildren=true;
	    }else{
				children.add(null);
			}
	  }
		if(!hasChildren)
			children=null;
	  return children;
	}


	// Value function takes into account the following:
	// Stones on our side - Stones on their side => H1
	// Stones in our kalahah - Stones in their kalahah => H2
	// Number of repeat turns possible from our current position => H3
	public static double valueFunction(final Board board, final Side side, final boolean maximisingPlayer) {
		int numStonesOurPits = 0;
		int numStonesOpponentPits = 0;
		int extraMove = 0;
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			numStonesOurPits += board.getSeeds(side, i);
			numStonesOpponentPits += board.getSeeds(side.opposite(), i);

			if (board.getSeeds(side, i) == 8 - i) {
				extraMove = 1;
			}
		}

		double heuristicTotal = 1.0 * (board.getSeedsInStore(side) - board.getSeedsInStore(side.opposite()))
			+ 1.25 * (numStonesOurPits - numStonesOpponentPits)
			+ 4.0 * extraMove;

		heuristicTotal *= maximisingPlayer ? 1.0 : -1.0;
		return heuristicTotal;
	}
}

// The assumptions are:
// We have the 'Board' passed in from the API.
// We have the function called 'children' which returns all the Boards
// that can be accessed from this chosen Board.
