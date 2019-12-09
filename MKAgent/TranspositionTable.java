package MKAgent;

import java.util.Hashtable;

public class TranspositionTable {
  
  // Set up the transposition table to have an initial size of 7^7 with a load factor of 95%
  private static Hashtable<Board, ValueObj> transposTable = new Hashtable<Board, ValueObj>(823543, 0.95f);
  
  public static ValueObj get(Board board) {
		return transposTable.get(board);
  }

  public static void put(Board board, ValueObj value, TTType type, int depth) {
    transposTable.put(board, new ValueObj(value.getMove(), value.getValue(), type, depth));
  }
}