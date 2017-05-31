package spider.game;

import java.util.ArrayList;
import spider.model.Rank;
import spider.model.Suit;

/**
 * Settings define a configuration for a specific variant of Spider
 * solitaire. Setting are used to interpret stored games read from 
 * a file and to write games in progress to a file.
 * 
 * @author Mark
 */
public class Settings
{
  private Rank ranks;
  private Suit suits;
  //private int multiple;
//  private OldParameters parameters;
  //private OldParameters.Board board;
  //private OldParameters.Deck deck;
  //private ArrayList<Integer> deal;
  
  //private String rankCodes;
  //private String suitCodes;
  private Parameters parameters;
  
  public Settings(String rankCodes, String suitCodes, int deckCount,
                  int columnCount, int dealCount)
  {
//    this(new Rank(rankcodes), new Suit(suitcodes), multiples,
//            new Parameters());
    this.ranks = new Rank(rankCodes);
    this.suits = new Suit(suitCodes);
    this.parameters = 
      new Parameters(ranks.size(), suits.size(), deckCount,
                     columnCount, dealCount);
  }
  
  /*
  public Settings(Rank r, Suit s, int multiple, OldParameters.Board b)
  {
    this.rank = r;
    this.suit = s;
    this.parameters = 
      new OldParameters(new OldParameters.Deck(r.size(), s.size(), multiple), b);
  }
  
  public Settings(OldParameters p, Rank r, Suit s)
  {
    this.parameters = p;
    this.rank = r;
    this.suit = s;
  }
  */
  
  public Parameters parameters() { return parameters; }
  public Rank ranks() { return ranks; }
  public Suit suits() { return suits; }
  
  public int encode(char rankCode, char suitCode)
  {
    int r = ranks.codeIndex(rankCode);
    int s = suits.codeIndex(suitCode);
    if ((r < 0) || (s < 0)) 
      throw new IllegalArgumentException(" bad code ");
    return r + s * ranks.size();
  }
}
