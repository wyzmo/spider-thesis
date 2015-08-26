package spider.analyze;

import spider.play.Action;
import spider.play.Position;

/**
 *
 * @author Mark
 */
public class FitnessPNode extends PNode<FitnessPNode>
{
  private final int joinCount;
  private final int hiddenCount;
  private final int fitness;
    
  @Override
  public FitnessPNode create(FitnessPNode previous, Position position, Action<Position> move)
  {
    return new FitnessPNode(previous, position, move);
  }
    
  public FitnessPNode(Position position)
  {
    this(null, position, null);
  }
  
  public FitnessPNode(FitnessPNode previous, 
                      Position position,
                      Action<Position> move)
  {
    super(previous, position, move);
    this.joinCount = position.joinsRemaining();
    this.hiddenCount = position.hiddenCount();
    this.fitness = (joinCount * 1) + 
                   (hiddenCount * 5) + 
                   (position.blocksRemaining() * 8);
    //+ non empty columns
  }
  
  public int joinsRemaining() { return joinCount; }
  public int hiddenCount() { return hiddenCount; }
  public int fitness() { return fitness; }

}
