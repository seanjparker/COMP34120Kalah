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
		Evaluation.h1 = 5;
		Evaluation.h2 = 1;
		Evaluation.h3 = 2;
		Evaluation.h4 = 1; // Maybe 18?
		Evaluation.h5 = 2;
		Evaluation.h6 = 1000;
		Evaluation.h7 = 5;
		Evaluation.h8 = 1;
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
  public static int evaluate(final Board board, final Side side, final boolean maximisingPlayer) {
		int v1 = scorePitDifference(board, side) * h1;
		int v2 = seedDifference(board, side) / h2;
		int v3 = extraMove(board, side) * h3;
		int v4 = clusterToScorePit(board, side) / h4;
		int v5 = -clusterToScorePit(board, side.opposite()) / h5;
		int v6 = haveHalfStoneTotal(board, side) * h6;
		int v7 = -haveHalfStoneTotal(board, side.opposite()) * h6;
		int v8 = -captureStones(board, side) * h7;
		int v9 = captureStones(board, side.opposite()) / h8;

		// System.err.printf("pit diff = %d, seed diff = %d, extra move = %d, cluster = %d, -cluster = %d, half total = %d, -halftotal = %d, cap = %d, -cap = %d\n\n", v1, v2, v3, v4, v5, v6, v7, v8, v9);

		int value = v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8 + v9;
		// value *= maximisingPlayer ? 1 : -1;
    return value; 
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
	private static int extraMove(final Board board, final Side side) {
		int value = 0;
		int holes = board.getNoOfHoles();

		for (int i = 1; i <= holes; i++) {
			int seeds = board.getSeeds(side, i);
			if (seeds == holes + 1 - i) value++;
		}
		return value;
	}

	// Heuristic 4
	private static int clusterToScorePit(final Board board, final Side side) {
		int value = 0;
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
			value += board.getSeeds(side, i) * (i - 1);
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
			if (board.getSeeds(side.opposite(), i) == 2 * holes + 1) {
				wholeBoardCapture = Math.max(wholeBoardCapture, board.getSeedsOp(side, i) + 1);
			}

			// Finds the wholes where the opponent has 0 seeds and we have >0 that are vunerable to capture
			if (board.getSeeds(side.opposite(), i) == 0 && board.getSeedsOp(side.opposite(), i) != 0) {
				capturableStones.add(i);
			}
		}

		for (int seed : capturableStones) {
			// Find the maximum number of seeds that can be captured on the same side
			for (int i = 1; i < seed; i++) {
				if (board.getSeeds(side.opposite(), i) == seed - i) {
					leftCapture = Math.max(leftCapture, board.getSeedsOp(side.opposite(), i) + 1);
				}
			}

			// Find the maximum number of seeds that can be captured going around the board
			for (int i = seed + 1; i <= holes; i++) {
				if (board.getSeeds(side.opposite(), i) == 2 * holes + 1 - (i - seed)) {
					rightCapture = Math.max(rightCapture, board.getSeedsOp(side.opposite(), i) + 1);
				}
			}
		}
		return Math.max(wholeBoardCapture, Math.max(leftCapture, rightCapture));
	}

}