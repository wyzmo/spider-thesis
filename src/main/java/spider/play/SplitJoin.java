package spider.play;

public final class SplitJoin extends Split
{
  //final int count;    //lower split count

  public SplitJoin(int from, int to, int count)
  {
    super(from, to, count);
    //this.count = count;
  }

  public Position apply(Position p)
  {
    return p.splitJoin(from, to, count);
  }

  public String toString()
  {
    return "join(" + from + "," + to + "," + count + ")";
  }

}