import MKAgent.*;

public class Test{
  public static void main(String[] args) throws InvalidMessageException{
    Board b = new Board(6,6);
    Protocol.interpretStateMsg ("CHANGE;1;7,7,2,7,7,7,7,0,8,8,8,8,8,6;OPP"+'\n', b);
    Side s = Side.SOUTH;
    MiniMax m = new MiniMax();
    ValueObj nextMove = m.minimax(b, s, 5, true);
    System.out.println("MOVE;"+nextMove.getMove());
  }
}
