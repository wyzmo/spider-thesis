package spider.play;

/**
 * Action to moves a full block from one column to another
 * in a given Position.
 */
public class MoveBlock extends Move
{
  public MoveBlock(int from, int to)
  {
    super(from, to);
  }

  public Position apply(Position p)
  {
    //TODO: implement block move
    return p.moveBlock(from, to);
  }

  public String toString()
  {
    return "move(" + from + "," + to + ")";
  }
}