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
	private boolean isWaiting;


	Search(String name, Board board, Side side, Terminate it, ValueObj vt, boolean isW) {
		this.threadName = name;
		this.board= board;
		this.side = side;
		this.isTerminating = it;
		this.valueObject = vt;
		this.valueObject.setValue(0);
		this.isWaiting = isW;
	}

	public void run() {
		try {
			search(board, side, this.valueObject, this.isTerminating, this.isWaiting);
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

	public void search(Board board, Side side, ValueObj vt, Terminate t, boolean is) {
		// ValueObj bestMove = null;

		List<Board> children = getChildren(board, side.opposite());
		for (int i = 1; i <= 7; i++) {

			// if(is){
			// 	for (int j = 0; j < children.size(); j++) {
			// 		if(children.get(j).getSeedsInStore(side)> board.getSeedsInStore(side) &&
			// 			children.get(j).getSeeds(side.opposite(), 1) == board.getSeeds(side.opposite(), 1)){
			// 				alphaBetaTT(board, side, i, -1000000, 1000000, false);
			// 		}else{
			// 			alphaBetaTT(board, side, i, -1000000, 1000000, false);
			// 		}
			// 	}
			// }else{
				this.valueObject = alphaBetaTT(board, side, i, -1000000, 1000000, false);
			// }
			// this.valueObject.setDepth(i);
			System.err.println(this.valueObject);
			if (t.getIsTerminating()) {
				System.err.println("Early quit");
				break;
			}
		}


		// this.valueObject = alphaBetaTT(board, side, 7, -1000000, 1000000, false);
		// System.err.println(this.valueObject);
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

	private ValueObj alphaBetaTT(Board board, Side side, int depth, int alpha, int beta, boolean samePlayer) {
		ValueObj ttentry = TranspositionTable.get(board);
		if (ttentry != null && ttentry.getDepth() >= depth) {
		// if (ttentry != null && ttentry.getDepth() == depth) {
			if (ttentry.getType() == TTType.EXACT) {// stored value is exact
				System.err.println("Caught Trans: " + ttentry);
				// System.err.println("with board: " + board);
				return ttentry;
			}
		}
		// 	if (ttentry.getType() == TTType.LOWERBOUND && ttentry.getValue() > alpha){
		// 		alpha = ttentry.getValue(); // update lowerbound alpha if needed
		// 	}
				
		// 	else if (ttentry.getType() == TTType.UPPERBOUND && ttentry.getValue() < beta){
		// 		beta = ttentry.getValue(); // update lowerbound alpha if needed
		// 	}

		// 	if (alpha >= beta){
		// 		return ttentry; // if lowerbound surpasses upperbound
		// 	}
				
		// }

		List<ValueObj> children = getSortedChildren(board, side);

		if (depth == 0 || children.size() == 0) {
			ValueObj terminal = new ValueObj();
			terminal.setDepth(depth);
			terminal.setValue(Evaluation.evaluate(board, side));
			// if(!samePlayer){

			// 	if (terminal.getValue() <= alpha){ // a lowerbound value
			// 		System.err.println("Storing Lowerbound: " + terminal);
			// 		TranspositionTable.put(board, terminal, TTType.LOWERBOUND, depth);
			// 	}
			// 	else if (terminal.getValue() >= beta)
			// 		TranspositionTable.put(board, terminal, TTType.UPPERBOUND, depth);
			// 	else{
					// TranspositionTable.put(board, terminal, TTType.EXACT, depth);
				// }
				// if (terminal.getValue() >= alpha && terminal.getValue() <= beta)
				// 	TranspositionTable.put(board, terminal, TTType.EXACT, depth);

				// if(ttentry == null || ttentry.getDepth() <= depth)
				// 	TranspositionTable.put(board, terminal, TTType.EXACT, depth);
			// }
			
			
			return terminal;
		}

		if (samePlayer) {
			Board nextBoard = new Board(board);
			ValueObj tempMove = alphaBetaTT(board, side.opposite(), depth - 1, -beta, -alpha, false);
				 tempMove.setValue(-tempMove.getValue());
			return tempMove;
		}


		ValueObj best = new ValueObj();
	ValueObj value = null;
	best.setValue(-1000000);
	for (int i = 0; i < children.size(); i++) {
			Board nextBoard = new Board(board);
			// Side nextTurn = Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));
			// if(nextTurn == side.opposite()){
			// 	value = alphaBetaTT(nextBoard, side.opposite(), depth - 1, -beta, -alpha);
			// 	value.setValue(-value.getValue());
			// }else{
			// 	value = alphaBetaTT(nextBoard, side, depth - 1, alpha, beta);
			// }

			Side nextSide = Kalah.makeMove(nextBoard, new Move(side, children.get(i).getMove()));

			value = alphaBetaTT(
				nextBoard,
				side.opposite(),
				nextSide == side ? depth : depth - 1,
				-beta,
				-alpha,
				nextSide == side
			);
			value.setValue(-value.getValue());

			// Value is a better move than best
			if (value.compareTo(best) >= 0) {
				value.setMove(children.get(i).getMove());
				best = value.clone();
				best.setDepth(depth);
				if(best.getValue() == 997 && best.getMove() == 5)
					System.err.println("HEEEEEY");
				if(best.getValue() == 997 && best.getMove() == 6)
					System.err.println("HEEEEEY6666666");
			}
			if (best.getValue() > alpha){
				alpha = best.getValue();
			}

			 if (alpha > beta){
				ValueObj ttentryO = TranspositionTable.get(board);	
				if(ttentryO == null || ttentryO.getDepth() <= depth){
					// if (best.getValue() <= alpha) // a lowerbound value
					// 	TranspositionTable.put(board, best, TTType.LOWERBOUND, depth);
					// else if (best.getValue() >= beta) // a upperbound value
					// 	TranspositionTable.put(board, best, TTType.UPPERBOUND, depth);
					// else	// a true minimax value
						TranspositionTable.put(board, best, TTType.EXACT, depth);
				}
				return best; // Cut off the current branch
			}
		}
		// if(!samePlayer){

			ValueObj ttentryO = TranspositionTable.get(board);	
		if(ttentryO == null || ttentryO.getDepth() <= depth){
			// if (best.getValue() <= alpha) // a lowerbound value
			// 	TranspositionTable.put(board, best, TTType.LOWERBOUND, depth);
			// else if (best.getValue() >= beta) // a upperbound value
			// 	TranspositionTable.put(board, best, TTType.UPPERBOUND, depth);
			// else	// a true minimax value
				TranspositionTable.put(board, best, TTType.EXACT, depth);
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
		//Collections.sort(children);
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
