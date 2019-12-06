package MKAgent;

public static class TranspositionTable {
  private static HashMap<Board, ValueObj> transposTable = new HashMap<Board, ValueObj>();
  
  public static ValueObj get(Board board) {
		return transposTable.get(board);
	}

	public static void put(Board board, ValueObj value, TTType type, int depth) {
		transposTable.put(board, new ValueObj(value.getMove(), value.getValue(), type, depth));
	}
}