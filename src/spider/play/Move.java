package spider.play;

/**
 * Base class for moves that change positions by moving
 * blocks between columns.
 *
 */
public abstract class Move implements Action<Position>
{
  final int from;
  final int to;

  Move(int from, int to)
  {
    this.from = from;
    this.to = to;
  }
  
  public final int from() { return from; }
  public final int to() { return to; }
}