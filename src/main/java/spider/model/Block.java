package spider.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Blocks are immutable. All operations that return
 * a Block instance return a new Block, the same Block,
 * or null.
 *
 * @author Mark
 */
public class Block implements Iterable<Block>
{
  private final int low;
  private final int high;
  private final int suit;
  private final int hash;    //immutable so never changes

  public Block(int rank, int suit)
  {
    this(rank, rank, suit);
  }

  /**
   * private to ensure valid arguments.
   */
  private Block(int high, int low, int suit)
  {
    this.low = low;
    this.high = high;
    this.suit = suit;
    
    int temp = 3;
    temp = 41 * temp + this.low;
    temp = 41 * temp + this.high;
    temp = 41 * temp + this.suit;
    this.hash = temp;
  }

  public int low() { return low; }
  public int high() { return high; }
  public int suit() { return suit; }

  public int hashCode()
  {
    return this.hash;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Block)) return false;
    Block b = (Block)o;
    return (this.low == b.low) &&
           (this.high == b.high) &&
           (this.suit == b.suit);
  }

  /**
   * there are four possible results from a join.
   *
   *  null,null : can't join suits differ
   *  null,old  : can't join no overlap
   *  new',null : full join occurred
   *  new',old' : split join (both old and new)
   */
  public static class Join
  {
    private final Block joined; //newly modified Block
    private final Block split;  //newly split block
    private Join(Block joined, Block split)
    {
      this.joined = joined;
      this.split = split;
    }

    public boolean isWrongSuit()
    {
      return (joined == null) && (split == null);
    }

    public boolean isDisjoint()
    {
      return (joined == null) && (split != null);
    }

    public boolean isJoined()
    {
      return joined != null;
    }

    public boolean isSplit()
    {
      return !(joined == null || split == null);
    }

    public Block joined() { return joined; }
    public Block split() { return split; }
    public Block lower() { return joined; }
    public Block upper() { return split; }

    public String toString()
    {
      return "[" + joined + "," + split + "]";
    }
  }

  private static final Join SUITSDIFFER = new Join(null, null);

  public Join join(Block other)
  {
    if (this.suit != other.suit) return SUITSDIFFER;
    Block joined = null;
    Block split = null;
    int below = low - 1;                //connection point
    if (other.high == below)
    {
      joined = new Block(this.high, other.low, this.suit);
    }
    else if ((other.high > below) && (other.low <= below))
    {
      joined = new Block(this.high, other.low, this.suit);
      split = new Block(other.high, this.low, this.suit);
    }
    else
    {
      split = other;
    }
    return new Join(joined, split);
  }

  public Join sequence(Block other)
  {
//    //if (this.suit != other.suit) return SUITSDIFFER;
//    Block joined = null;
//    Block split = null;
    int below = low - 1;                //connection point
    if (other.high == below)
    {
//      //joined = new Block(this.high, other.low, this.suit);
//      joined = other;                   //simple move
      return new Join(other, null);
    }
//    else if ((other.high > below) && (other.low <= below))
//    {
      Join splitOther = other.splitAt(below);
      return (splitOther.isSplit())
             ? splitOther
             : new Join(null, other);
      //joined = new Block(this.high, other.low, this.suit);
      //split = new Block(other.high, this.low, this.suit);
//    }
//    else
//    {
//      //split = other;
//    }
//    return new Join(joined, split);
  }

  /**
   * @param count the size of the lower block split
   */
  public Join split(int count)
  {
    Block lower = null;
    Block upper = null;
    if (count >= size())
    {
      lower = this;
    }
    else if (count <= 0)
    {
      upper = this;
    }
    else
    {
      lower = new Block(this.low + count - 1, this.low, suit);
      upper = new Block(this.high, low + count, suit);
    }
    return new Join(lower, upper);
  }

  /**
   * @param rank the high rank of the lower split
   */
  public Join splitAt(int rank)
  {
    return split(1 + rank - low);
  }

  public Iterator<Block> iterator()
  {
    return this.new Splitter();
  }

  public Iterator<Block> splitter()
  {
    return this.new Splitter();
  }

  //Spliterator
  private class Splitter implements Iterator<Block>
  {
    int index = Block.this.high;   //current split position + 1

    public boolean hasNext()
    {
      return index > Block.this.low;
    }

    public Block next()
    {
      if (!(index > Block.this.low)) throw new NoSuchElementException();
      return new Block(--index, Block.this.low, suit);
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

  }

  public int size() { return 1 + high - low; }

  public String toString()
  {
    return "b(" + high + "," + low + "," + suit + ")";
  }
}
