package spider.play;

/**
 * Action to moves a full block from one column to another
 * in a given Position.
 */
public class SplitMove extends Split
{
  public SplitMove(int from, int to, int count)
  {
    super(from, to, count);
  }

  public Position apply(Position p)
  {
    return p.splitMove(from, to, count);
  }

  public String toString()
  {
    return "move(" + from + "," + to + "," + count + ")";
  }
}