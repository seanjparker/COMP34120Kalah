import MKAgent.*;

public class Test{
  public static void main(String[] args) throws InvalidMessageException{
    Board b = new Board(7,7);
    Protocol.interpretStateMsg ("CHANGE;1;0,0,0,0,0,0,2,42,1,1,0,5,0,0,2,37;OPP"+'\n', b);
    System.out.println(b);
    Side s = Side.SOUTH;
    MiniMax m = new MiniMax();
    // System.out.println(m.getChildren(b, s));
     ValueObj nextMove = m.minimax(b, s, 5, false);
    // System.out.println(m.valueFunction(b, false));
     System.out.println("MOVE;"+nextMove.getMove());
  }
}
