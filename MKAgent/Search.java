package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.lang.Math;

class Search {

	private Board board;
	private Side side;
	private ValueObj valueObject;


	Search(Board board, Side side, ValueObj vt) {
		this.board= board;
		this.side = side;
		this.valueObject = vt;
		this.valueObject.setValue(0);
	}

	public void search(Board board, Side side, ValueObj vt) {
		if(board.getSeedsInStore(Side.SOUTH)<50 ||board.getSeedsInStore(Side.NORTH)<50 ){
			this.valueObject = alphaBetaTT(board, side, 10, -1000000, 1000000, false,false);
		}
		else{
			this.valueObject = alphaBetaTT(board, side, 1, -1000000, 1000000, false,true);
		}
	}

	private ValueObj alphaBetaTT(Board board, Side side, int depth, int alpha, int beta, boolean samePlayer,boolean extraMoveEval) {


		List<ValueObj> children = getSortedChildren(board, side);

		if (depth == 0 || children.size() == 0) {
			ValueObj terminal = new ValueObj();
			if(extraMoveEval==false){
				terminal.setValue(Evaluation.evaluate(board, side));
			}else{
				terminal.setValue(Evaluation.extraMove(board, side));
			}
			return terminal;
		}

		if (samePlayer) {
			Board nextBoard = new Board(board);
			ValueObj tempMove = alphaBetaTT(board, side.opposite(), depth - 1, -beta, -alpha, false,extraMoveEval);
				 tempMove.setValue(-tempMove.getValue());
			return tempMove;
		}


		ValueObj best = new ValueObj();
	ValueObj value = null;
	best.setValue(-1000000);
	for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);

			Side nextSide = Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));

			value = alphaBetaTT(
				nextBoard,
				side.opposite(),
				nextSide == side ? depth : depth - 1,
				-beta,
				-alpha,
				nextSide == side,
				extraMoveEval
			);
			value.setValue(-value.getValue());
			
			if (value.compareTo(best) >= 0) {
				value.setMove(children.get(i).getMove());
				best = value.clone();

			}
			if (best.getValue() > alpha){
				alpha = best.getValue();}

			 if (alpha > beta){
			 return best; // Cut off the current branch
			}

		}
		return best;
	}

	public List<ValueObj> getSortedChildren(Board board, Side side) {
		List<ValueObj> children = new ArrayList<ValueObj>();
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			if (board.getSeeds(side, i) != 0) {
				children.add(new ValueObj(i, Evaluation.evaluate(board, side)));
			}
		}
		return children;
	}

	public static ArrayList<Board> getChildren(Board board, Side side) {
		ArrayList<Board> children = new ArrayList<Board>();
		Board originalBoard = new Board(board);
	  	boolean hasChildren=false;
		for (int i = 1; i <= originalBoard.getNoOfHoles(); i++) {
		  	if (originalBoard.getSeeds(side, i) > 0) {
				Move newmove = new Move(side, i);
				Kalah.makeMove(originalBoard,newmove);
				children.add(originalBoard);
				originalBoard= new Board(board);
				hasChildren=true;
			}
		}
		if(!hasChildren)
			children=null;
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
