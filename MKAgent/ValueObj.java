package MKAgent;

public class ValueObj implements Comparable<Object> {
  private int value;
  private int move;

  public ValueObj() {
  }

  public ValueObj(int move, int value) {
    this.move = move;
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public int getMove() {
    return move;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setMove(int move) {
    this.move = move;
  }

  public ValueObj clone() {
    return new ValueObj(this.move, this.value);
  }

  public int compareTo(Object o) {
    ValueObj other = (ValueObj) o;
    return this.value == other.getValue() ? 0 : this.value > other.getValue() ? 1 : -1;
  }
}
