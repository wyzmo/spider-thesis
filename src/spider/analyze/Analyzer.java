package spider.analyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import spider.game.Game;
import spider.model.Block;
import spider.play.Action;
import spider.play.Board;
import spider.play.MoveBlock;
import spider.play.Position;
import spider.play.Split;
import spider.play.SplitMove;

/**
 *
 * @author Mark
 */
public class Analyzer
{
  private final Game game;
  //ArrayList<Position> tried;
  private PNode<?> win;
  private int sizeTried;
  
  private PositionNode winningNode;
  private DepthPNode winningDepthPNode;
  private FitnessPNode winningFitnessPNode;
  
  private int nodeCount = 0;

  
  public Analyzer(Game game)
  {
    this.game = game;
  }
  
  public void analyze()
  {
    analyze(new Position(game.pack(), game.settings().parameters()));
  }
  
  public PNode<?> win() { return this.win; }
  public int sizeTried() { return this.sizeTried; }
  public PositionNode winningNode() { return this.winningNode; }
  public DepthPNode winningDepthPNode() { return this.winningDepthPNode; }
  public FitnessPNode winningFitnessPNode() { return this.winningFitnessPNode; }
  
  public void analyze(Position p)
  {
    Board b = new Board(game);
    b.draw(p);
    System.out.println(p);
    HashSet<Position> tried = new HashSet<>();
    //tried.add(p);
    PositionNode pn = new PositionNode(p);
    ArrayList<PositionNode> positions = new ArrayList<>();
    positions.add(pn);
    
    analyze(pn, tried);
    
    ArrayList<PositionNode> result = new ArrayList<>();
    pn = winningNode;
    while(pn != null)
    {
      result.add(pn);
      pn = pn.previous();
    }
    Collections.reverse(result);
    int count = 0;
    for(PositionNode step : result)
    {
      System.out.println(count++ + ". " + step.move());
      b.draw(step.position());
    }
      
  }
  
  public void analyze(PositionNode initial, HashSet<Position> tried)
  {
    ArrayList<PositionNode> next = new ArrayList<>();
    next.add(initial);
    while(!next.isEmpty())
    {
      System.out.println("next:" + next.size() + " tried:" + tried.size());
      ArrayList<PositionNode> current = next;
      next = new ArrayList<>();
      for(PositionNode pn : current)
      {
        Position p = pn.position();
        if (!tried.add(p)) continue;  //already looked at this position
        if (p.isWon())
        {
          winningNode = pn;
          return;
        }
        //List<Action<Position>> moves = p.allMoves();
        generateNext(pn, next, p.allMoves());
      }
    }
  }
  
  private void generateNext(PositionNode pn, 
                            ArrayList<PositionNode> nextList, 
                            List<Action<Position>> moves)
  {
    Position p = pn.position();
    for(Action<Position> move : moves)
    {
      if (move instanceof Split) continue;
      Position next = move.apply(p);
      nextList.add(new PositionNode(pn, next, move));
    }
  }
  
  public void depthFirst()
  {
    depthFirst(new Position(game.pack(), game.settings().parameters()));
  }
  
  public void depthFirst(Position initial)
  {
    Board b = new Board(game);
    b.draw(initial);
    System.out.println(initial);
    this.nodeCount++;
    winningDepthPNode = depthFirst(new DepthPNode(initial));
    drawGame();
  }

  private DepthPNode depthFirst(DepthPNode depthPNode)
  {
    HashSet<Position> tried = new HashSet<>();
    DepthPNode current = depthPNode;
    int counter = 0;
    while (current != null)
    {
      Position p = current.position();
      if (p.isWon())
      {
        return current;
      }
      if ((++counter) % 17384 == 0)
      {
        System.out.println((counter / 17384) + ". " + p);
        System.out.println(nodeCount + " " + tried.size() + " " + 
                           current.depth() + " " + 
                           current.position().joinsRemaining() + " " +
                           current.moves());
        new Board(game).draw(p);
        if (tried.size() > 2000000) tried.clear();
      }
      
      
      //current = depthFirstNext(current);
      current = depthFirstNext(current, tried);

    }
    return null;
  }

  //private DepthPNode depthFirstNext(DepthPNode initial)
  private DepthPNode depthFirstNext(DepthPNode initial, HashSet<Position> tried)
  {
    DepthPNode current = initial;
    while(current != null)   
    {
      int moves = current.depth() + current.position().hiddenCount();
      if (current.hasNext() && (moves < 52))
      {
        nodeCount++;
        DepthPNode next = current.next();
        //if (!alreadyTried(next.position(), current)) return next;
        //if (tried.add(next.position())) return next;
        if ((!alreadyTried(next.position(), current)) &&
             tried.add(next.position())) return next;
        //return next;
      }
      else
      {
        //back track to find first node with more moves to try
        current = current.previous();
      }
    }
    return null;
  }

  private boolean alreadyTried(Position position, DepthPNode initial)
  {
    DepthPNode current = initial;
    while(current != null)
    {
      if (current.position().equals(position)) 
      {
        return true;
      }
      current = current.previous();
    }
    return false;
  }
  
  /*************************** Fitness Algorithm ***/
  
  public void fitness()
  {
    fitness(new Position(game.pack(), game.settings().parameters()));
  }
  
  public void fitness(Position initial)
  {
    Board b = new Board(game);
    b.draw(initial);
    System.out.println(initial);
    this.nodeCount++;
    winningFitnessPNode = fitness(new FitnessPNode(initial));
    //drawGame();
  }

  private ArrayList<FitnessPNode> primary;
  private ArrayList<FitnessPNode> secondary;  //incubator
  private int poolsize = 10000;
  private int reduction = 3;   //max weakest to eliminate
  private int bestReign = 12000; //number of times before mutation attempt
  private int mutationFrequency = 100;
  private int swapcount = 4096;
  private int generations = 10000000; //number of loops
  private int extra = 64; //
  private Random rand = new Random(1);
  
  private FitnessPNode fitness(FitnessPNode pNode)
  {
    primary = new ArrayList<>(poolsize + extra); //litle overrun added
    secondary = new ArrayList<>(poolsize + extra); //litle overrun added
    for(int i = 0; i < poolsize; i++)
    {
      primary.add(pNode);
      secondary.add(pNode);
    }

    FitnessPNode bestSoFar = pNode;
    int bestUnchanged = 0;
    
    int generation = 0;
    while(generation++ < generations)       //replace with termination 
    {
      if (rand.nextInt(mutationFrequency) == 0)
      {
        //System.out.println("Mutating primary");
        mutate(primary);
      }
      if (rand.nextInt(mutationFrequency) == 0)
      {
        //System.out.println("Mutating secondary");
        mutate(secondary);
      }
      int primaryPick = rand.nextInt(primary.size());
      int secondaryPick = rand.nextInt(secondary.size());
      FitnessPNode current = primary.get(primaryPick);
      fitnessNext(current, primary); 
      FitnessPNode current2 = secondary.get(secondaryPick);
      fitnessNext(current2, secondary);
      FitnessPNode bestPrimary = primary.get(0);
      FitnessPNode bestSecondary = secondary.get(0);
      FitnessPNode best = (bestPrimary.fitness() <= bestSecondary.fitness()) ? bestPrimary : bestSecondary;
      if (bestSecondary.fitness() <= bestPrimary.fitness())
      {
        int replace = rand.nextInt(primary.size());
        if (replace > 0) primary.set(replace, secondary.remove(0));
      }
      FitnessPNode worst = primary.get(Math.min(primary.size() - 1, 255));
      if (best.position().isWon())
      {
        drawGame(best);
        return best;
      }
/*      
      if ((generation % swapcount) == 0)
      {
        int from = rand.nextInt(primary.size());
        if (from > 0) primary.addAll(from, secondary);
        int to = secondary.size() - from;
        if (to > 0) secondary.removeAll(secondary.subList(0, to));
      }
 * */
 
      if ((generation % 1000) == 0)
      {
      System.out.println(generation + ". best: " + 
                         bestPrimary.fitness() + " " +  
                         bestPrimary.joinsRemaining() + " " +
                         bestPrimary.hiddenCount() + " second:" +
                         bestSecondary.fitness() + " " +  
                         bestSecondary.joinsRemaining() + " " +
                         bestSecondary.hiddenCount() + " worst:" +
                         worst.fitness() + " " +  
                         worst.joinsRemaining() + " " +
                         worst.hiddenCount());
      }
//      if (best == bestSoFar) bestUnchanged++;
//      else { bestSoFar = best; bestUnchanged = 0; }
/*      
      if ((best.fitness() == worst.fitness()) || 
          (bestUnchanged > bestReign))
      {
        System.out.println("Mutating ==>");
        bestUnchanged = 0;
        ArrayList<FitnessPNode> path = new ArrayList<>();
        //int mutants = poolsize / 10;       //decimate
        int mutants = poolsize / 2;
        for(int i = 0; i < mutants; i++)
        {
          path.clear();
          int index = rand.nextInt(primary.size());
          if (index == 0) continue;
          FitnessPNode mutant = primary.get(index);
          while (mutant != null)
          {
            path.add(mutant);
            mutant = mutant.previous();
          }
          if (path.size() > 1)
          {
            primary.set(index, path.get(rand.nextInt(path.size())));
          }
        }
      }
*/
    }
    return null;
  }

  private void fitnessNext(FitnessPNode initial, ArrayList<FitnessPNode> pool)
  {
    //shrink pool back to limit (or go one less);
    int keep = poolsize - rand.nextInt(reduction);  //prune weakest
    while(pool.size() > keep) { pool.remove(pool.size() - 1); }
    //Collections.reverse(pool);
    
    FitnessPNode current = initial;
    Position p = initial.position();
    ArrayList<Action<Position>> moves = p.allMoves();
    //while (rand.nextInt(3) != 0)
    do
    {
      if (moves.isEmpty()) break;
    
      Action<Position> randomMove = moves.get(rand.nextInt(moves.size()));
      p = randomMove.apply(p);
      current = new FitnessPNode(current, p, randomMove);
      pool.add(current);
      moves = p.allMoves();
    }
    while (rand.nextInt(3) != 0);
    
    for(Action<Position> move : moves)  
    {
      Position next = move.apply(p);
      pool.add(new FitnessPNode(current, next, move));
    }
    Collections.sort(pool, FITNESSCOMPARATOR);
    
  }
  
  private void mutate(ArrayList<FitnessPNode> pool)
  {
    if (rand.nextInt(20) > 0)
    {
      simpleMutate(pool);
      return;
    }
    ArrayList<FitnessPNode> path = new ArrayList<>();
    int mutants = poolsize / 25;       //half decimate (4%)
    //int mutants = poolsize / 2;
    for(int i = 0; i < mutants; i++)
    {
      path.clear();
      int index = rand.nextInt(pool.size());
      if (index < 100) continue;
      FitnessPNode mutant = pool.get(index);
      while (mutant != null)
      {
        path.add(mutant);
        mutant = mutant.previous();
      }
      if (path.size() > 1)
      {
        pool.set(index, path.get(rand.nextInt(path.size())));
      }
    }
  }
  
  private void simpleMutate(ArrayList<FitnessPNode> pool)
  {
    int mutants = poolsize / 2;       //back up half the nodes
    //int mutants = poolsize / 2;
    for(int i = 0; i < mutants; i++)
    {
      int index = rand.nextInt(pool.size());
      if (index < 200) continue;
      FitnessPNode mutant = pool.get(index).previous();
      if (mutant != null)
      {
        pool.set(index, mutant);
      }
    }
  }

  private static Comparator<FitnessPNode> FITNESSCOMPARATOR =
          new Comparator<FitnessPNode>()
          {
            public int compare(FitnessPNode p1, FitnessPNode p2)
            {
              return p1.fitness() - p2.fitness();
            }
          };
  
  
  
  
  public void drawGame()
  {
    Board b = new Board(game);
    ArrayList<DepthPNode> result = new ArrayList<>();
    DepthPNode pn = winningDepthPNode;
    while(pn != null)
    {
      result.add(pn);
      pn = pn.previous();
    }
    Collections.reverse(result);
    int count = 0;
    for(DepthPNode step : result)
    {
      System.out.println(count++ + ". " + step.move());
      b.draw(step.position());
    }
  }
  
  public void drawGame(PNode<?> endNode)
  {
    Board b = new Board(game);
    ArrayList<PNode<?>> result = new ArrayList<>();
    PNode<?> pn = endNode;
    while(pn != null)
    {
      result.add(pn);
      pn = pn.previous();
    }
    Collections.reverse(result);
    int count = 0;
    for(PNode<?> step : result)
    {
      System.out.println(count++ + ". " + step.move());
      b.draw(step.position());
    }
  }
  
  
  /****** Breadth first with pruning **********/
  //private static final int DEFAULT_MAX_POSITIONS = 64001;
  private static final int DEFAULT_MAX_POSITIONS = 64; //in thousands

  private final int MaxPositions = 
          Integer.getInteger("maxpos", DEFAULT_MAX_POSITIONS);
  
  private final int StallMax = 
          Integer.getInteger("stallmax", 11);

  
  public void breadthFirst()
  {
    breadthFirst(new Position(game.pack(), game.settings().parameters()));
  }
  
  public void breadthFirst(Position p)
  {
    int useMax = Integer.getInteger("usemax", 0);
    if (useMax > 0)
    {
      this.sizeTried = (useMax + 1) / 1000;
      this.win = breadthFirst(p, useMax + 1);
      return;
    }
    int[] sizes = new int[]
      {MaxPositions, MaxPositions * 2, MaxPositions / 2, MaxPositions * 4};
    for (int i = 0; i < sizes.length; i++)
    {
      this.sizeTried = sizes[i];
      int maxPositions = 1 + (sizes[i] * 1000);
      this.win = breadthFirst(p, maxPositions); 
      if (this.win != null) break;
    }
  }
  
  /**
   * Analyzes a position using a breadth first search. When the search
   * generates too many nodes to fit into memory each nodes will be 
   * assigned a fitness as in the fitness search and the nodes with
   * the lowest fitness will be pruned.
   * 
   * @param p 
   */
  public BreadthPNode breadthFirst(Position p, int maxSize)
  {
    System.out.println("Starting breadth first analysis ...");
    Board b = new Board(game);
    b.draw(p);
    //System.out.println(p);
       
    BreadthPNode pn = new BreadthPNode(p);
       
    ArrayList<BreadthPNode> positions = new ArrayList<>(maxSize);
    ArrayList<BreadthPNode> nextPositions = new ArrayList<>(maxSize);
    HashSet<Position> tried = new HashSet<>(maxSize);
    //ArrayList<Position> avoid = new ArrayList<>();
    
    positions.add(pn);
    
    BreadthPNode winner = null;
    BreadthPNode best = null;
    BreadthPNode worst = null;
    int level = 0;
    int triedSize = 0;
    int bestFitness = Integer.MAX_VALUE;
    int bestFitnessCount = 0;
    do
    {
      best = positions.get(0);    //best path if no winner found
      worst = positions.get(positions.size() - 1); //last kept position
      //int bestAggregate = best.aggregate();
      int bestAggregate = best.blocks() + best.joins() + best.hidden();
      int worstAggregate = worst.blocks() + worst.joins() + worst.hidden();
      
      System.out.println(level + ". " + 
                         positions.size() + " " + 
                         triedSize + " Best: " + 
                         bestAggregate + " " +
                         best.deals() + " " + 
                         best.blocks() + " "  + 
                         best.joins() + " " +
                         best.hidden() + " " + 
                         best.outOfSequence() + " Worst: " +
                         worstAggregate + " " +
                         worst.deals() + " " + 
                         worst.blocks() + " "  + 
                         worst.joins() + " " +
                         worst.hidden() + " " + 
                         worst.outOfSequence()
                         ); 
      if (bestAggregate < bestFitness)
      {
        bestFitness = bestAggregate;
        bestFitnessCount = 0;
      }
      else 
      {
        if (++bestFitnessCount > StallMax) break;   //game appears stalled
      }
      winner = breadthFirst(positions, nextPositions, tried);
      if (winner != null) break;  //found the winning path
      
      //swap current and next
      ArrayList<BreadthPNode> temp = positions;
      positions = nextPositions;
      nextPositions = temp;
      nextPositions.clear();      //empty next (old positions)
      triedSize = tried.size();
      tried.clear();              //tried set
      
      prune(positions, maxSize);    //get rid of least promising moves;
      
      level++;
    }
    while(!positions.isEmpty());
    
    if (winner != null) drawGame(winner);
    else drawGame(best);
    
    return winner;
      
  }
  
  public BreadthPNode breadthFirst(ArrayList<BreadthPNode> positions, 
                                   ArrayList<BreadthPNode> nextPositions,
                                   HashSet<Position> tried)
  {
    tried.clear();                 //reuse set for efficiency
    nextPositions.clear();
    
    for(BreadthPNode pn : positions)
    {
      //BreadthPNode pn = positions.remove(i - 1); //pop last node
      ArrayList<BreadthPNode> nextList = generateNext(pn);
      for(BreadthPNode next : nextList)
      { 
        Position p = next.position();
        if (p.isWon()) return next;      //we have a winner
        if (tried.add(next.position()))
        {
          nextPositions.add(next); 
        }
      }
    }
    return null;                         //no winner yet
  }
  
  /**
   * Creates a list of all non cyclic positions that gan be generated
   * from the current position.
   * @param pn the current position 
   * @return 
   */
  private ArrayList<BreadthPNode> generateNext(BreadthPNode pn)
  {
    Position p = pn.position();
    ArrayList<BreadthPNode> result = new ArrayList<>();
    BreadthPNode prior = pn.previous();
    for(Action<Position> move : p.allMoves())
    {
      if (canSkipMove(pn.position(), move)) continue; //unnecessary move
      Position next = move.apply(p);
      if (isCycle(next, prior)) continue;
      result.add(new BreadthPNode(pn, next, move));
    }
    return result;
  }
  
  private boolean canSkipMove(Position p, Action<Position> action)
  {
    //If there are deals left any move might reposition the cards
    //to improve the position after the deal.
    if (p.dealsRemaining() > 0) return false;
    //if (!(action instanceof SplitMove)) return false;
    if (action instanceof MoveBlock) return canSkipMoveBlock(p, (MoveBlock)action);
    if (action instanceof SplitMove) return canSkipSplitMove(p, (SplitMove)action);
    return false;
  }
  
  /**
   * Skips moving a block to an empty column if the from will be empty
   * after the move. That is, skips shifting a single block to another
   * column when there are no cards to uncover. The skipped from column
   * will have one block and zero hidden. 
   * @param p current position
   * @param move the move to consider skipping
   * @return true to skip the move, false otherwise
   */
  private boolean canSkipMoveBlock(Position p, MoveBlock move)
  {
    int from = move.from();
    int to = move.to();
    ArrayList<ArrayList<Block>> columns = p.active();
    if (!columns.get(to).isEmpty()) return false;
    if (p.hidden().get(from).intValue() > 0) return false;
    return columns.get(from).size() == 1;
  }
  
  private boolean canSkipSplitMove(Position p, SplitMove move)
  {
    int from = move.from();
    int to = move.to();
    ArrayList<ArrayList<Block>> columns = p.active();
    ArrayList<Block> froms = columns.get(from);
    Block split = froms.get(froms.size() - 1);
    int high = split.low() + move.count() - 1;
    int size = columns.size();
    for(int i = 0; i < size; i++)
    {
      if ((i == from) || (i == to)) continue;
      ArrayList<Block> blocks = columns.get(i);
      int blockcount = blocks.size();
      if (blockcount == 0) continue;
      Block last = blocks.get(blockcount - 1);
      if (last.high() != high) continue;
      if (blockcount == 1)
      { 
        if (p.hidden().get(i).intValue() == 0) continue;
        return false;
      }
      if (blocks.get(blockcount - 2).low() != (high + 1)) return false;
    }   
    return true;
  }

  private boolean isCycle(Position next, BreadthPNode prior)
  {
    if (prior == null) return false;
    BreadthPNode previous = prior;
    int hidden = next.hiddenCount();
    int deals = next.dealsRemaining();
    while ((previous != null) &&
           (previous.position().hiddenCount() == hidden) &&
           (previous.position().dealsRemaining() == deals))
    {
      if (previous.position().equals(next)) return true;
      previous = previous.previous();
    }
    return false;
  }

  private int prune(ArrayList<BreadthPNode> positions, int maxSize)
  {
    Collections.sort(positions, BreadthPNode.COMPARATOR);
    int size = positions.size();
    if (size < maxSize) return 0;
    positions.subList(maxSize - 1, size).clear();
    return 1 + size - maxSize;
  }


}
