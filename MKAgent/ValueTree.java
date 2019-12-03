package MKAgent;

import java.util.List;

import java.util.ArrayList;

public class ValueTree {
  private int depth;
  private ValueTree parent;
  private List<ValueTree> children;
  private ValueObj content;

  public ValueTree(int d, ValueTree p) {
      this.depth = d;
      this.parent = p;
      this.content = new ValueObj(-1, Integer.MIN_VALUE);
  }

  public void setValueObj(int move, int value) {
    this.content.setMove(move);
    this.content.setValue(value);
    this.parent.updateValue(this.content);
  }

  public ValueObj getValueObj() {
    return this.content;
  }

  public void setChildren(int noChildren){
    for(int i = 0; i<noChildren; i++){
        this.children.add(new ValueTree(this.depth+1, this));
    }
  }

  public ValueTree getChildren(int i){
    return this.children.get(i);
  }

  public void updateValue(ValueObj newVO){
        if(this.content.compareTo(newVO) > 0){
            setValueObj(newVO.getMove(), newVO.getValue());
        }
  }
}
