package spider.play;

/**
 * Base class for moves that change positions by moving
 * blocks between columns.
 *
 */
public abstract class Split extends Move
{
  final int count;

  Split(int from, int to, int count)
  {
    super(from, to);
    this.count = count;
  }
  
  public final int count() { return count; }
}