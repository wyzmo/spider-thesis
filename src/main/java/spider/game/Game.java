package spider.game;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
import java.util.List;
import java.util.Random;
import spider.model.Pack;
import spider.model.Rank;
import spider.model.Suit;

/**
 *
 * @author Mark
 */
public class Game
{
  public static List<String> arrayFromString(String s)
  {
    if (s == null) return new ArrayList<>(0);
    String[] sa = s.split(",");
    return Arrays.asList(sa);
  }
  
  //public static final Random RANDOMIZER = new Random();
  private final Random randomizer;
  private final Long seed;          //initial seed used for randomizer
  private final Settings settings;
  private final Pack pack;
  
  public Game(Settings parms)
  {
    this(parms, null, null);
  }
  
  public Game(Settings parms, long seed)
  {
    this(parms, null, new Long(seed));
  }
  
  public Game(Settings parms, Pack pack)
  {
    this(parms, pack, null);
  }
  
  //TODO: maybe this should just be passed a deal instead of pack?
  private Game(Settings settings, Pack pack, Long seed)
  {
    if (settings == null)
    {
      throw new IllegalArgumentException("Settings must be specified");
    }
    this.settings = settings;
    if (pack == null)
    {
      if (seed == null)
      {
        this.randomizer = new Random();
        this.seed = Long.valueOf(this.randomizer.nextLong());
      }
      else
      {
        this.seed = seed.longValue();
        this.randomizer = new Random(this.seed);     
      }
      this.pack = shuffle(settings, randomizer);
    }
    else //pack supplied 
    {
      this.randomizer = null;
      this.seed = null;
      this.pack = pack;
    }
  }
  
  private Pack shuffle(Settings s, Random r)
  {
//    List<Integer> deal = new Pack(s).deal(); 
//    Collections.shuffle(deal, r);
//    return new Pack(s, deal);  
    Pack p = new Pack(s.ranks(), s.suits(), s.parameters().deckCount());
    p.shuffle(r);
    return p;
  }
  
  public Pack pack() { return pack; }
  public Settings settings() { return settings; }
  public Long seed() { return seed; }
   
  //move to game
  
  public String asBoardString()
  {
    int columns = settings.parameters().columnCount();
    int cards = pack.packSize();
    int ranks = pack.rankCount();
    int suits = pack.suitCount();
    Rank rank = pack.rankCodes();
    Suit suit = pack.suitCodes();
    int deckSize = pack.deckSize();
    List<Integer> deal = pack.deal();
    StringBuilder sb = new StringBuilder(cards + (cards / columns) + 2);
    for(int i = cards - (cards % columns); i >= 0; i -= columns)
    {
      if (i == cards) continue; 
      int end = Math.min(i + columns, cards);
      for(int j = i; j < end; j++)
      {
        int card = deal.get(j);
        int cardindex = card % deckSize;
        int suitindex = cardindex / ranks; // card / (suits * multiples)
        int rankindex = cardindex % ranks;
        sb.append(rank.code(rankindex))
          .append(suit.code(suitindex));
      }
      sb.append('\n');
    } 
    sb.append('\n');
    return sb.toString();
  }
  
  
}
