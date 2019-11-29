import MKAgent.*;

public class Test{
  public static void main(String[] args) throws InvalidMessageException{
    Board b = new Board(7,7);
    Protocol.interpretStateMsg ("CHANGE;1;7,0,8,8,8,8,8,1,0,9,8,8,8,8,8,1;YOU"+'\n', b);
    System.out.println(b);
    Side s = Side.NORTH;
    // System.out.println(m.getChildren(b, s));
    ValueObj nextMove = Search.search(b, s, 1);
    // System.out.println(m.valueFunction(b, false));
    System.out.println("MOVE;"+nextMove.getMove());
  }
}
