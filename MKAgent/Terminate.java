package MKAgent;

public class Terminate {
  private boolean isTerminating;

  public Terminate() {
      this.isTerminating = false;
  }

  public void setIsTerminating(boolean t){
      this.isTerminating = t;
  }
  public boolean getIsTerminating(){
      return this.isTerminating;
  }
}