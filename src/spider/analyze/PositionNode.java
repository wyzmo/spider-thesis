package spider.analyze;

import spider.play.Action;
import spider.play.Position;

/**
 * A basic PNode with no special behaviors.
 * @author Mark
 */
public class PositionNode extends PNode<PositionNode>
{
  
  /**
   * Establishes an initial position.
   * @param position 
   */
  public PositionNode(Position position)
  {
    this(null, position, null);    
  }
  
  public PositionNode(PositionNode previous, 
                      Position position, 
                      Action<Position> move)
  {
    //this.previous = previous;
    //this.position = position;
    //this.move = move;
    super(previous, position, move);
  }
  
  //public Position position() { return this.position; }
  //public PositionNode previous() { return this.previous; }
  //public Action<Position> move() { return this.move; }

  @Override
  public PositionNode create(PositionNode previous, Position position, Action<Position> move)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
