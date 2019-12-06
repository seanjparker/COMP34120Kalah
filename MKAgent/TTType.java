package MKAgent;

public enum TTType {
  UPPERBOUND (1),
  EXACT      (0),
  LOWERBOUND (-1);

  private final int value;

  TTType(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}