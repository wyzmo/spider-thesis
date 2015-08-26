package spider.analyze;
import spider.play.Action;
import spider.play.DealCards;
import spider.play.JoinBlock;
import spider.play.MoveBlock;
import spider.play.Position;
import spider.play.SplitJoin;
import spider.play.SplitMove;

/**
 *
 * @author Mark
 */
public abstract class PNode<T extends PNode>
{
  private T previous;
  private Position position;
  private Action<Position> move;
  
  /**
   * Constructs a root node from an initial Position.
   * @param position 
   */
  public PNode(Position position)
  {
    this(null, position, null);
  }
  
  public PNode(T previous, 
               Position position,
               Action<Position> move)
  {
    this.previous = previous; //null for root node
    this.position = position;
    this.move = move;         //TODO: make initial action
  }
  
  public T create(Position position)
  {
    return create(null, position, null);
  }
  
  public abstract T create(T previous, 
                           Position position, 
                           Action<Position> move);
  
  public Position position() { return this.position; }
  public T previous() { return this.previous; }  
  public Action<Position> move() { return move; }
  
  public int length()
  {
    int i = 0;
    PNode<?> p = this;
    do
    {
      i++;
      p = p.previous();
    }
    while (p != null);
    return i;
  }
  public int moves() { return length() - 1; }
  
  public int[] moveInfo()
  {
    int[] info = new int[6];
    info[0] = -1;
    PNode<?> p = this;
    do
    {
      info[0]++;
      if (p.move() instanceof JoinBlock) info[1]++;
      else if (p.move() instanceof MoveBlock) info[2]++;
      else if (p.move() instanceof SplitJoin) info[3]++;
      else if (p.move() instanceof SplitMove) info[4]++;
      else if (p.move() instanceof DealCards) info[5]++;
      p = p.previous();
    }
    while (p != null);
    return info;
  }
}
