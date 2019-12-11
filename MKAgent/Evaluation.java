package MKAgent;

import java.util.List;
import java.util.ArrayList;

public class Evaluation {

	private static int h1;
	private static int h2;
	private static int h3;
	private static int h4;
	private static int h5;
	private static int h6;
	private static int h7;
	private static int h8;

	static {
		Evaluation.h1 = 3;
		Evaluation.h2 = 2;
		Evaluation.h3 = 1;
		Evaluation.h4 = 18;
		Evaluation.h5 = 1000;
		Evaluation.h6 = 3;
	}

	/* 
	* Value function takes into account the following:
	* Stones on our side - Stones on their side
	* Stones in our kalahah - Stones in their kalahah
	* Clusters stones near kalahah
	* Emphasis on extra turns
	* Max score when have >half stones
	* Check for vunerable stones
	*/
  public static int evaluate(final Board board, final Side side) {
	  	int detail = 2000;
		double v1 = ((scorePitDifference(board, side)+98)/(98.0*2)) * detail;
		System.err.println(side);
		System.err.print(board);
		// System.err.println(v1);
		double v2 = ((seedDifference(board, side)+20)/(20.0*2)) *detail;
		// System.err.println(v2);
		double v3 = (extraMove(board, side)/7.0) * detail;
		double v4 = (1 - (extraMove(board, side.opposite())/7.0)) * detail;
		// System.err.println(v3);
		// System.err.println(v4);
		double v5 = ((clusterToScorePit(board, side)+70)/(70.0*2) ) *detail;
		// System.err.println(v4);
		double v6 = (1.0-((clusterToScorePit(board, side.opposite())+70)/(70.0*2))) *detail ;
		// Syste.err.println(v5);
		double v7 = (haveHalfStoneTotal(board, side)/1.0) * detail;
		// System.err.println(v6);
		double v8 = (1.0-haveHalfStoneTotal(board, side.opposite())) * detail;
		// System.err.println(v7);
		double v9 = (1-(captureStones(board, side.opposite())/20.0)) * detail;
		// System.err.println();
		// System.err.println(captureStones(board, side));
		// System.err.println(captureStones(board, side.opposite()));
		double v10 = (captureStones(board, side)/20.0) * detail;
		// System.err.println(v9);
		// System.err.println(v10);

		// System.err.printf("pit diff = %d, seed diff = %d, extra move = %d, cluster = %d, -cluster = %d, half total = %d, -halftotal = %d, cap = %d, -cap = %d\n\n", v1, v2, v3, v4, v5, v6, v7, v8, v9);

		// System.err.println(v1*0.4 +((v2+v3+v4+v5+v6+v7)/6)*0.6);
		int result = (int) Math.round(
						(
							v1 * 0.5 +
							v2 * 0.0 +
							(
								v3 * 0.5 +
								v4 * 0.5
							) * 0.2 + 
							(
								v9 * 0.5 +
								v10 * 0.5
							) * 0.2 + 
							(
								v5 * 0.5 + 
								v6 * 0.5
							) * 0.1 
						)*0.49
						+(
						 (v6+v7)/2
						)*0.51);
		System.err.println(result);
		return result;

		// int v1 = scorePitDifference(board, side) * 5;
		// int v2 = seedDifference(board, side) / 1;
		// int v3 = extraMove(board, side) * 2;
		// int v4 = clusterToScorePit(board, side) / 1;
		// int v5 = -clusterToScorePit(board, side.opposite()) / 2;
		// int v6 = haveHalfStoneTotal(board, side) * 10000;
		// int v7 = -haveHalfStoneTotal(board, side.opposite()) * 10000;
		// int v8 = -captureStones(board, side) * 5;
		// int v9 = captureStones(board, side.opposite()) / 1;

		// // System.err.printf("pit diff = %d, seed diff = %d, extra move = %d, cluster = %d, -cluster = %d, half total = %d, -halftotal = %d, cap = %d, -cap = %d\n\n", v1, v2, v3, v4, v5, v6, v7, v8, v9);

		// return v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8 + v9;
	}

	public static int quickEval(final Board board, final Side side) {
		int v1 = scorePitDifference(board, side) * 3;
		int v3 = extraMove(board, side) * 4;
		int v8 = -captureStones(board, side) * 2;
		int v6 = haveHalfStoneTotal(board, side) * 10000;
		int v7 = -haveHalfStoneTotal(board, side.opposite()) * 10000;
		return v1 + v3 + v6 + v7 + v8;
	}

	// Heuristic 1
	private static int scorePitDifference(final Board board, final Side side) {
		return board.getSeedsInStore(side) - board.getSeedsInStore(side.opposite());
	}

	// Heuristic 2
	private static int seedDifference(final Board board, final Side side) {
		int numStonesOurPits = 0;
		int numStonesOpponentPits = 0;
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			numStonesOurPits += board.getSeeds(side, i);
			numStonesOpponentPits += board.getSeeds(side.opposite(), i);
		}
		return numStonesOurPits - numStonesOpponentPits;
	}

	// Heuristic 3
	private static int extraMove(final Board board, final Side side){
		int value = 0;
		int holes = board.getNoOfHoles();

		Board copy = new Board(board);
		for (int i = 1; i <= holes; i++) {
			if (Kalah.makeMove(copy, new Move(side, i)) == side) value++;
			copy = new Board(board);
		}
		return value;
	}

	// Heuristic 4
	private static int clusterToScorePit(final Board board, final Side side) {
		int value = 0;
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			value += board.getSeeds(side, i) * (4- i);
		}
		return value;
	}

	// Heuristic 5
	private static int haveHalfStoneTotal(final Board board, final Side side) {
		return board.getSeedsInStore(side) > 49 ? 1 : 0;
	}

	// Heuristic 6
	private static int captureStones(final Board board, final Side side) {
		int holes = board.getNoOfHoles();
		int wholeBoardCapture = 0, leftCapture = 0, rightCapture = 0;
		List<Integer> capturableStones = new ArrayList<>();

		for (int i = 1; i <= holes; i++) {
			// Check if capture possible that goes around whole board
			if (board.getSeeds(side, i) == 2 * holes + 1) {
				wholeBoardCapture = Math.max(wholeBoardCapture, board.getSeedsOp(side, i) + 1);
			}

			// Finds the wholes where the opponent has 0 seeds and we have >0 that are vunerable to capture
			if (board.getSeeds(side, i) == 0 && board.getSeeds(side.opposite(), 8-i) != 0) {
				// System.err.println("Hole: " + i);
				// System.err.println("Us: " + board.getSeeds(side, i));
				// System.err.println("Them: " + board.getSeeds(side.opposite(), 8-i));
				capturableStones.add(i);
			}
		}

		for (int seed : capturableStones) {
			// Find the maximum number of seeds that can be captured on the same side
			for (int i = 1; i < seed; i++) {
				if (board.getSeeds(side, i) == seed - i) {
					
					leftCapture = Math.max(leftCapture, board.getSeeds(side.opposite(), 8-seed) + 1);
				}
			}

			// Find the maximum number of seeds that can be captured going around the board
			for (int i = seed + 1; i <= holes; i++) {
				if (board.getSeeds(side, i) == 2 * holes + 1 - (i - seed)) {
					rightCapture = Math.max(rightCapture, board.getSeeds(side.opposite(), 8-seed) + 1);
				}
			}
		}
		return Math.max(wholeBoardCapture, Math.max(leftCapture, rightCapture));
	}

}