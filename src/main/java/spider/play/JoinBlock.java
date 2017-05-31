package spider.play;

/**
 * Action to moves a full block from one column to another
 * in a given Position.
 */
public class JoinBlock extends Move
{
  public JoinBlock(int from, int to)
  {
    super(from, to);
  }

  public Position apply(Position p)
  {
    //TODO: implement block move
    return p.joinBlock(from, to);
  }

  public String toString()
  {
    return "join(" + from + "," + to + ")";
  }
}