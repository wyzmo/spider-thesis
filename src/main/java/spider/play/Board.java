package spider.play;

import java.util.ArrayList;
import spider.game.Game;
import spider.model.Block;

public class Board
{
  private final Game initial;
  public Board(Game initial)
  {
    this.initial = initial;
  }
  
  public void draw(Position p)
  {
    System.out.println(drawString(p)); 
  }
  
  private static final String[] HIDDENCOUNTS = new String[]
  {
    " 0 "," 1 "," 2 "," 3 "," 4 "," 5 "," 6 "," 7 "," 8 "," 9 ",
    "10 ","11 ","12 ","13 ","14 ","15 ","16 ","17 ","18 ","19 ",
    "++ ",
  };
  private static final int HIDDENCOUNTSMAX = HIDDENCOUNTS.length - 1;
  
  public String drawString(Position p)
  {
    StringBuilder sb = new StringBuilder();
    for(int hidden : p.hidden())
    {
      sb.append(HIDDENCOUNTS[Math.min(hidden,HIDDENCOUNTSMAX)]);
    }
    sb.append('\n');
    boolean more = true;
    for(int i = 0; more; i++)
    {
      more = false;
      columns:
      for(ArrayList<Block> blocks : p.active())
      {
          int j = i;                //find the correct block
          Block block;
          for(Block b : blocks)
          {
            if (j < b.size())
            {
              block = b;
              sb.append(p.cardCode(b.high() - j, b.suit()))
                .append(' ');
              more = true;
              continue columns;
            } //found containing block
            j -= b.size();           //index into next block
          }
          sb.append(" - ");
      }
      
      sb.append('\n');
    }
    
    return sb.toString();
  }
}