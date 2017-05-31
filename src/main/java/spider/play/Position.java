package spider.play;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import spider.game.Parameters;
import spider.model.Block;
import spider.model.Pack;

/**
 * Position instances are immutable.
 */
public class Position
{
  private final Pack pack;        //shared reference of initial deal
  private final Parameters parms; //shared reference to parameters
  private final ArrayList<Integer> initialHidden; //shared reference

  private ArrayList<Integer> hiddenSizes;
  private ArrayList<ArrayList<Block>> active;

  private int dealsRemaining;
  private int hiddenRemaining;    //convenience sum of hidden sizes
  private int blocksRemaining;    //block not yet completed and removed
  
  private final int hashcode;     //positions are immuatable so constant 
  
  private int computeHash()      
  {
    int hash = 3;                 
    hash = 41 * hash + Objects.hashCode(this.pack);
    hash = 41 * hash + Objects.hashCode(this.parms);
    hash = 41 * hash + Objects.hashCode(this.initialHidden);
    hash = 41 * hash + Objects.hashCode(this.hiddenSizes);
    hash = 41 * hash + Objects.hashCode(this.active);
    hash = 41 * hash + this.dealsRemaining;
    hash = 41 * hash + this.hiddenRemaining;
    hash = 41 * hash + this.blocksRemaining;
    return hash;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Position)) return false;
    Position p = (Position)o;
    return this.pack == p.pack &&
           this.parms == p.parms &&
           this.initialHidden == p.initialHidden &&
           this.blocksRemaining == p.blocksRemaining &&
           this.hiddenRemaining == p.hiddenRemaining &&
           this.dealsRemaining == p.dealsRemaining &&
           this.hiddenSizes.equals(p.hiddenSizes) &&
           this.active.equals(p.active);
  }

  public int hashCode()
  {
    return hashcode;
  }

  /**
   * Constructs the first position of a game.
   * @param pack
   * @param parms
   */
  public Position(Pack pack, Parameters parms)
  {
    this.pack = pack;
    this.parms = parms;
    this.hiddenSizes = new ArrayList<>(parms.columnCount());
    for(int i = 0; i < parms.columnCount(); i++)
    {
      hiddenSizes.add((parms.hiddenSize() / parms.columnCount())
                        + (((parms.hiddenSize() % parms.columnCount()) > i)
                          ? 1 : 0));
    }
    this.initialHidden = new ArrayList<>(hiddenSizes);
    this.hiddenRemaining = parms.hiddenSize();
    this.dealsRemaining = parms.dealCount() - 1; //one deal is dealt
    this.blocksRemaining = parms.blockCount();
    this.active = new ArrayList<>(parms.columnCount());
    int offset = parms.dealSize() - parms.columnCount();
    for(int i = 0; i < parms.columnCount(); i++)
    {
      ArrayList<Block> activeBlocks = new ArrayList<>(1);
      int card = pack.deal().get(offset + i);
      Block b = new Block(pack.rankIndex(card), pack.suitIndex(card));
      activeBlocks.add(b);
      this.active.add(activeBlocks);
    }
    this.hashcode = computeHash();
  }


  
  private Position(Pack pack, Parameters parms,
                   ArrayList<Integer> initialHidden,
                   ArrayList<Integer> hiddenSizes,
                   ArrayList<ArrayList<Block>> active,
                   int dealsRemaining, int hiddenRemaining,
                   int blocksRemaining)
  {
    this.pack = pack;
    this.parms = parms;
    this.initialHidden = initialHidden;
    this.hiddenSizes = hiddenSizes;
    this.active = active;
    this.dealsRemaining = dealsRemaining;
    this.hiddenRemaining = hiddenRemaining;
    this.blocksRemaining = blocksRemaining;   //full blocks
    this.hashcode = computeHash();
  }
  
  public String toString()
  {
    return "hiddenSizes=" + this.hiddenSizes +
           " active=" + this.active +
           " dealsRemaining=" + this.dealsRemaining +
           " hiddenRemaining=" + this.hiddenRemaining +
           " blocksRemaining=" + this.blocksRemaining;
  }

  public ArrayList<ArrayList<Block>> active()
  {
    //TODO: make active unmodifiable list of unmodifiable
    return active;
  }
  
  public ArrayList<Integer> hidden()
  {
    return hiddenSizes;
  }
  
  public int hiddenCount() { return this.hiddenRemaining; }
  public int blocksRemaining() { return this.blocksRemaining; }
  public int dealsRemaining() { return this.dealsRemaining; }
  
  /**
   * Computes number of block joins needed to complete the game.
   * @return 
   */  
  public int joinsRemaining()
  {
    if (blocksRemaining < 1) return 0; //winning position
    int needed = blocksRemaining * (parms.rankCount() - 1);
    for (ArrayList<Block> blocks : active)
    {
      for(Block block : blocks)
      {
        needed -= (block.size() - 1);
      }
    }
    return needed;
  }
  
  public int outOfSequence()
  {
    int oos = 0;
    for (ArrayList<Block> blocks : active)
    {
      for(int i = 1; i < blocks.size(); i++)
      {
        if (blocks.get(i - 1).low() != (1 + blocks.get(i).high())) oos++;
      }
    }
    return oos;
  }
  
  public String cardCode(int rank, int suit)
  {
    return this.pack.cardCode(rank, suit);
  }
  
  public boolean isWon() { return blocksRemaining == 0; }

  public Position deal()
  {
    ArrayList<ArrayList<Block>> updatedActive = new ArrayList<>(active.size());
    int blocksLeft = blocksRemaining;
    int offset = (dealsRemaining - 1) * active.size();
    for(ArrayList<Block> tos : active)
    {
      int card = pack.deal().get(offset++);
      Block b = new Block(pack.rankIndex(card), pack.suitIndex(card));
      ArrayList<Block> to = new ArrayList<>(tos); //copy list of blocks
      if (to.isEmpty())
      {
        to.add(b);
        updatedActive.add(to);
        continue;
      }
      Block last  = last(to);
      Block.Join join = last.join(b);
      if (join.isJoined())
      {
        if (join.joined().size() == pack.rankCount())
        {
          to.remove(last);
          blocksLeft--;
        }
        else
        {
          to.set(to.size() - 1, join.joined());
        }
      }
      else
      {
        to.add(b);
      }
      updatedActive.add(to);
    }
    
//    return new Position(pack, parms, initialHidden,
//                        hiddenSizes, updatedActive,
//                        dealsRemaining - 1, 
//                        hiddenRemaining, blocksLeft);
    return uncoverHidden(updatedActive, dealsRemaining - 1, blocksLeft);
       
  }
  
  private Position uncoverHidden(ArrayList<ArrayList<Block>> updatedActive,
                                 int updatedDeals, int updatedBlocks)
  {
    //now if from is empty need to turn up a hidden card
    //and adjust hiddenSizes;
    //ArrayList<Integer> updatedHidden = this.hiddenSizes;
    ArrayList<Integer> updatedHidden = null;
    int hiddenLeft = this.hiddenRemaining;
    int columns = updatedActive.size();
    
    for(int i = 0; i < columns; i++)
    {
      int hidden = hiddenSizes.get(i);
      ArrayList<Block> column = updatedActive.get(i);
      if (!(column.isEmpty() && (hidden > 0))) continue;

      int offset = parms.dealCount() + this.initialHidden.get(i) - hidden;
      offset *= parms.columnCount();
      offset += i;
      int card = pack.deal().get(offset);
      Block b = new Block(pack.rankIndex(card), pack.suitIndex(card));
      column.add(b);
      if (updatedHidden == null)
      {
        updatedHidden = new ArrayList<>(this.hiddenSizes);
      }
      updatedHidden.set(i, hidden - 1);
      hiddenLeft--;    
    }
    return new Position(pack, parms, initialHidden,
                        (updatedHidden == null) ? this.hiddenSizes : updatedHidden,
                        updatedActive,
                        updatedDeals, 
                        hiddenLeft, updatedBlocks); 
  }
  
  public Position joinBlock(int from, int to)
  {
    return moveBlock(from, to);
  }
  
  public Position moveBlock(int from, int to)
  {
    ArrayList<Block> froms = new ArrayList<>(active.get(from));
    Block fromBlock = froms.remove(froms.size() - 1);
    ArrayList<Block> tos = new ArrayList<>(active.get(to));
    
    int blocksLeft = blocksRemaining;  //this is full blocks
    if (tos.isEmpty())
    {
      tos.add(fromBlock);
    }
    else
    {
      int last = tos.size() - 1; 
      Block toBlock = tos.get(last);  
      if (fromBlock.suit() == toBlock.suit())
      {
        Block.Join join = toBlock.join(fromBlock);
        if (join.joined().size() == pack.rankCount())
        {
          tos.remove(last);
          blocksLeft--;
        }
        else
        {
          tos.set(last, join.joined());
        }
      }
      else
      {
        tos.add(fromBlock);
      }
    }
/*
    //now if from is empty need to turn up a hidden card
    //and adjust hiddenSizes;
    ArrayList<Integer> updatedHidden = this.hiddenSizes;
    int hiddenLeft = this.hiddenRemaining;
    int hidden = hiddenSizes.get(from);
    if (froms.isEmpty() && (hidden > 0))
    {
      int offset = this.initialHidden.get(from) - hidden;
      offset += parms.dealCount() * parms.columnCount();
      offset += from;
      int card = pack.deal().get(offset);
      Block b = new Block(pack.rankIndex(card), pack.suitIndex(card));
      froms.add(b);
      updatedHidden = new ArrayList<>(this.hiddenSizes);
      updatedHidden.set(from, hidden - 1);
      hiddenLeft--;
    }
*/
    ArrayList<ArrayList<Block>> updatedActive = new ArrayList<>(active);
    updatedActive.set(from, froms);
    updatedActive.set(to, tos);

//    return new Position(pack, parms, initialHidden,
//                        updatedHidden, updatedActive,
//                        dealsRemaining, hiddenLeft, blocksLeft);
    return uncoverHidden(updatedActive, dealsRemaining, blocksLeft);
  }

  public Position splitJoin(int from, int to, int count)
  {
    return splitMove(from, to, count);
  }
  
  public Position splitMove(int from, int to, int count)
  {
    ArrayList<Block> froms = new ArrayList<>(active.get(from));
    Block fromBlock = froms.remove(froms.size() - 1);
    ArrayList<Block> tos = new ArrayList<>(active.get(to));
    
    Block.Join split = fromBlock.split(count);
    Block toSplit = split.lower();
    Block fromSplit = split.upper();
    if (toSplit == null) return this;                  //nothing to move
    if (fromSplit == null) return moveBlock(from, to); //full move
    
    froms.add(fromSplit);   
    int blocksLeft = blocksRemaining;  //this is full blocks
    if (tos.isEmpty())
    {
      tos.add(toSplit);
      //froms.add(fromSplit);
      //add one to pieces left
    }
    else
    {
      int last = tos.size() - 1; 
      Block toBlock = tos.get(last);  
      //Block toBlock = last(tos);
      if (fromBlock.suit() == toBlock.suit())
      {
        //Block.Join join = toBlock.join(toSplit);
        //tos.set(last, join.joined());
        //check if full block formed
        //blocksLeft--;
        Block.Join join = toBlock.join(fromBlock);
        if (join.joined().size() == pack.rankCount())
        {
          tos.remove(last);
          blocksLeft--;
        }
        else
        {
          tos.set(last, join.joined());
        }
      }
      else
      {
        tos.add(toSplit);
      }
    }

    //now if from is empty need to turn up a hidden card
    //and adjust hiddenSizes;

    ArrayList<ArrayList<Block>> updated = new ArrayList<>(active);
    updated.set(from, froms);
    updated.set(to, tos);

//    return new Position(pack, parms, initialHidden, hiddenSizes, updated,
//                        dealsRemaining, hiddenRemaining, blocksLeft);
    
    return uncoverHidden(updated, dealsRemaining, blocksLeft);
  }

  public ArrayList<Action<Position>> allMoves()
  {
    ArrayList<Action<Position>> joins = new ArrayList<>();
    ArrayList<Action<Position>> moves = new ArrayList<>();
    ArrayList<Action<Position>> empties = new ArrayList<>();
    
    int emptyColumns = 0;
    
    for(int i = 0; i < active.size(); i++) //for each to column
    {
      ArrayList<Block> column = active.get(i);
      if (column.isEmpty())
      {
        emptyColumns++;
        //no need to generate same moves to different columns at end
        if ((dealsRemaining > 0) || empties.isEmpty())
        {
          allMovesToEmpty(i, empties); 
        }
      }
      else
      {
        allMovesTo(i, column.get(column.size() - 1), joins, moves);
      }
    }
    
    //order the moves: joins, sequence moves, moves to empty, deal.
    joins.addAll(moves);
    joins.addAll(empties);
    //if (dealsRemaining > 0) joins.add(new DealCards());
    //if ((dealsRemaining > 0) && (emptyColumns == 0)) 
    if ((dealsRemaining > 0) && (emptyColumns == 0)) 
    {
      joins.add(new DealCards());
    }

    return joins;
  }

  private void allMovesToEmpty(int to, ArrayList<Action<Position>> moves)
  {
    for(int i = 0; i < active.size(); i++)   //for ach column
    {
      if (i == to) continue;           //skip the column being moved to
      //ArrayList<Block> from = active.get(i);
      Block from = last(active.get(i));
      if (from == null) continue;
      //if (from.isEmpty()) continue;    //from column is empt
      moves.add(new MoveBlock(i, to)); //move full block to empty
      //Block fromBlock = from.get(from.size() - 1);
      for(int count = from.size() - 1; count > 0; count--)
      {
        moves.add(new SplitMove(i, to, count));
      }
    }
  }

  private void allMovesTo(int to, Block toBlock, 
                          ArrayList<Action<Position>> joins,
                          ArrayList<Action<Position>> moves)
  {
    int toRank = toBlock.low();
    if (toRank == 0) return;                  //lowest rank
    for(int i = 0; i < active.size(); i++)
    {
      if (i == to) continue; //skip the column being moved to
      ArrayList<Block> from = active.get(i);
      if (from.isEmpty()) continue;  //from column is empty
      int last = from.size() - 1;
      Block b = from.get(last); //last (bottom) block
      boolean joining = toBlock.suit() == b.suit();        //
      Block.Join join = toBlock.sequence(from.get(last)); //seqeunce last block
      if (join.isSplit())
      {
        if (joining)
        {
          joins.add(new SplitJoin(i, to, join.lower().size()));
        }
        else
        {
          moves.add(new SplitMove(i, to, join.lower().size()));
        }
      }
      else if (join.isJoined())
      {
        if (joining)
        {
          joins.add(new JoinBlock(i, to));
        }
        else
        {
          moves.add(new MoveBlock(i, to));
        }
      }
    }
  }
  
  public <T> T last(List<T> list)
  {
    //return (list.size() > 0) ? list.get(list.size() - 1) : null;
    return ((list == null) || list.isEmpty()) 
            ? null 
            : list.get(list.size() - 1);
  }

  
}