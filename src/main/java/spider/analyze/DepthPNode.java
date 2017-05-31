package spider.analyze;

import java.util.ArrayList;
import java.util.Collections;
import spider.play.Action;
import spider.play.Position;

/**
 *
 * @author Mark
 */
public class DepthPNode //extends PositionNode
{
  
  private DepthPNode previous;
  private Position position;
  private Action<Position> move;
  private ArrayList<Action<Position>> moves;
  private final int depth;
  
  public DepthPNode(Position position)
  {
    this(null, position, null, 0);
  }
  
  public DepthPNode(DepthPNode previous, Position position,
                    Action<Position> move, int depth)
  {
    this.previous = previous; //null for root node
    this.position = position;
    this.move = move;         //null for initial mode TODO: make initial action
    this.moves = position.allMoves();
    Collections.reverse(moves);       //use end of list as first
    this.depth = depth;
  }
  
  
  public Position position() { return this.position; }
  public DepthPNode previous() { return this.previous; }
  public int depth() { return this.depth; }
  
  public Action<Position> move() { return move; }
  public ArrayList<Action<Position>> moves() { return moves; } //temporary while debugging
//  { 
//    return moves.isEmpty()
//           ? null
//           : moves.get(moves.size() - 1);
//  }
  
  public boolean hasNext() { return !moves.isEmpty(); }
  public DepthPNode next()
  {
    if (!hasNext()) return null;
    Action<Position> nextMove = moves.remove(moves.size() - 1);
    Position nextPosition = nextMove.apply(this.position);
    return new DepthPNode(this, nextPosition, nextMove, this.depth + 1);
  }
  
  public int movesRemaining() { return moves.size(); }
}
