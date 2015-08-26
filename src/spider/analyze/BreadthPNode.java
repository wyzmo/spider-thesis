package spider.analyze;

import java.util.Comparator;
import spider.play.Action;
import spider.play.Position;

/**
 *
 * @author Mark
 */
public class BreadthPNode extends PNode<BreadthPNode>
{ 
  private final int joins;
  private final int hidden;
  private final int blocks;
  private final int outOfSequence;
  
  @Override
  public BreadthPNode create(BreadthPNode previous, Position position, Action<Position> move)
  {
    return new BreadthPNode(previous, position, move);
  }
  
  public BreadthPNode(Position position)
  {
    this(null, position, null);
  }
  
  public BreadthPNode(BreadthPNode previous, 
                      Position position,
                      Action<Position> move)
  {
    super(previous, position, move);
    
    this.joins = position.joinsRemaining();
    this.hidden = position.hiddenCount();
    this.blocks = position.blocksRemaining();
    this.outOfSequence = position.outOfSequence();    
  }
  
  public int deals() { return position().dealsRemaining(); }
  public int joins() { return joins; }
  public int hidden() { return hidden; }
  public int blocks() { return blocks; }
  public int aggregate() { return blocks + joins + hidden; }
  public int outOfSequence() { return outOfSequence; }
  
  public static Comparator<BreadthPNode> COMPARATOR =
    new Comparator<BreadthPNode>()
    {
      public int compare(BreadthPNode p1, BreadthPNode p2)
      {
        int delta;
        
        delta = p1.aggregate() - p2.aggregate();
        if (delta != 0) return delta; 
        
        delta = p1.hidden - p2.hidden; //give lowering hidden precedence
        if (delta != 0) return delta;
             
        delta = p1.joins - p2.joins;
        if (delta != 0) return delta;       
        
        delta = p1.outOfSequence - p2.outOfSequence;
        if (delta != 0) return delta;
        
        return 0;
      }
    };
  
  public static Comparator<BreadthPNode> JOINSCOMPARATOR =
    new Comparator<BreadthPNode>()
    {
      public int compare(BreadthPNode p1, BreadthPNode p2)
      {
        int delta;
        
        delta = p1.joins - p2.joins;
        if (delta != 0) return delta;
           
        delta = p1.hidden - p2.hidden;
        if (delta != 0) return delta;
        
        delta = p1.outOfSequence - p2.outOfSequence;
        if (delta != 0) return delta;
        
        return 0;
      }
    };
  
  public static Comparator<BreadthPNode> HIDDENCOMPARATOR =
    new Comparator<BreadthPNode>()
    {
      public int compare(BreadthPNode p1, BreadthPNode p2)
      {
        int delta;
                  
        delta = p1.hidden - p2.hidden;
        if (delta != 0) return delta;
        
        delta = p1.joins - p2.joins;
        if (delta != 0) return delta;
        
        delta = p1.blocks - p2.blocks;
        if (delta != 0) return delta;
        
        delta = p1.outOfSequence - p2.outOfSequence;
        if (delta != 0) return delta;
        
        return 0;
      }
    };

  
  
  
}
