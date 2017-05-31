package spider.play;

public class DealCards implements Action<Position>
{
  public Position apply(Position p)
  {
    return p.deal();
  }

  public String toString()
  {
      return "deal()";
  }
}