package MKAgent;

public class ValueObj implements Comparable<Object> {
  private int value;
  private int move;
  private TTType type;
  private int depth;

  public ValueObj() {
  }

  public ValueObj(int move, int value, TTType type, int depth) {
    this.move = move;
    this.value = value;
    this.type = type;
    this.depth = depth;
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

  public TTType getType() {
    return type;
  }

  public int getDepth() {
    return depth;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setMove(int move) {
    this.move = move;
  }

  public void setType(TTType type) {
    this.type = type;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public ValueObj clone() {
    return new ValueObj(this.move, this.value);
  }

  public int compareTo(Object o) {
    ValueObj other = (ValueObj) o;
    return this.value == other.getValue() ? 0 : this.value > other.getValue() ? 1 : -1;
  }

  public String toString() {
    return "Value = " + value + ", move = " + move + ", depth = " + type;
  }
}
