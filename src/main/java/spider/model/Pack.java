package spider.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import spider.game.Settings;
//import spider.game.OldParameters;
//import spider.game.Settings;

/**
 * Pack instances represent a pack of cards used for a game of 
 * Spider Solitaire. The cards are ordered in an initial arrangement
 * known as the deal. 
 * 
 * The Pack also holds card codes for the ranks and the suits used
 * for display purposes. If not supplied a default set is used.
 * 
 * @author Mark
 */
public class Pack
{
  private final Rank rankCodes;
  private final Suit suitCodes;
  private final int rankCount;
  private final int suitCount;
  private final int deckCount;
  private final int deckSize;
  private final int packSize;
  
  //private final Settings settings;         //   
  //private final OldParameters.Deck deck;   //ranks, suits, multiples
  private final List<Integer> deal;        //arangement of cards
  //private final int pack;

  private final int[] validation;
  private final boolean isValid;
  
  /**
   * Creates a pack that can be used with the supplied parameters.
   * Creates an initial deal of the cards in sequence.
   * 
   * @param parms 
   */
  public Pack(int rankCount, int suitCount, int deckCount)
  {
    this(rankCount, suitCount, deckCount, null);
  }
  
  public Pack(int rankCount, int suitCount, int deckCount, 
              List<Integer> deal)
  {
    this(new Rank(rankCount), new Suit(suitCount), deckCount, deal);
  }
  
  public Pack(Rank rankCodes, Suit suitCodes, int deckCount)
  {
    this(rankCodes, suitCodes, deckCount, null);
  }
  
  public Pack(Settings settings, List<Integer> deal)
  {
    this(settings.ranks(), settings.suits(), 
         settings.parameters().deckCount(), deal);
  }
  
  public Pack(Rank rankCodes, Suit suitCodes, int deckCount,
              List<Integer> deal)
  {
    this.rankCodes = rankCodes;
    this.suitCodes = suitCodes;
    this.rankCount = rankCodes.size();
    this.suitCount = suitCodes.size();
    this.deckCount = deckCount;
    this.deckSize = rankCount * suitCount;
    this.packSize = deckSize * deckCount;
    
    if (deal == null)
    {
      this.deal = initialDeal(this.deckSize, this.deckCount); 
      this.validation = new int[this.deckSize];
      Arrays.fill(this.validation, this.deckCount);
      this.isValid = true;
    }
    else
    {
      this.deal = deal;
      this.validation = validate(this.deal, this.deckSize);
      this.isValid = checkValid(this.validation, this.deckCount);
    }
  }
  
 
  
  private boolean checkValid(int[] validation, int checkValue)
  {
    for(int i = 0; i < validation.length; i++)
    {
      if (validation[i] != checkValue) return false;
    }
    return true;
  }
  
  public final int[] validate(List<Integer> deal, int deckSize)
  {
    int[] counts = new int[deckSize];
    for(Integer i : deal)
    {
      counts[i.intValue()]++;
    }
    return counts;
  }
  
  public final boolean isValid() { return isValid; }
  
  private ArrayList<Integer> initialDeal(int deckSize, int deckCount)
  {
    ArrayList<Integer> result  = new ArrayList<>(deckSize * deckCount);
    for(int i = 0; i < deckCount; i++)
      for(int j = 0; j < deckSize; j++)
      {
        result.add(j);
      }
    return result;
  }
  
  /**
   * Provides a copy of the current deal.
   * 
   * @return a list of card values representing the initial deal 
   */
  public List<Integer> deal() 
  { 
    return new ArrayList<>(deal); 
  }
  
  public void shuffle()
  {
    shuffle(new Random());
  }
  
  public void shuffle(Random r)
  {
    Collections.shuffle(deal, r);
  }
   
  public final Rank rankCodes() { return this.rankCodes; }
  public final Suit suitCodes() { return this.suitCodes; }
  public final int rankCount() { return this.rankCount; }
  public final int suitCount() { return this.suitCount; }
  public final int deckCount() { return this.deckCount; }
  public final int deckSize() { return this.deckSize; }
  public final int packSize() { return this.packSize; }
  
  public int suitIndex(int card)
  {
    return card / this.rankCount;
  }
  
  public int rankIndex(int card)
  {
    return card % this.rankCount;
  }
  
  public String cardCode(int rank, int suit)
  {
    StringBuilder sb = new StringBuilder(2);
    sb.append(rankCodes.code(rank))
      .append(suitCodes.code(suit));
    return sb.toString();
  }
  
  public String dealAsString()
  {
    return deal.toString();
  }
  
  public String dealAsCardString()
  {
    StringBuilder sb = new StringBuilder(this.packSize * 2);
    for(int i = 0; i < this.packSize; i++)
    {
      int card = deal.get(i);
      sb.append(this.rankCodes.code(rankIndex(card)))
        .append(this.suitCodes.code(suitIndex(card)));
    }
    return sb.toString();
  }
  
  public String validationString()
  {
    return Arrays.toString(validation);
  }
  
  public String validationFormattedString()
  {
    StringBuilder sb = new StringBuilder();
   
    sb.append(' ').append(this.rankCodes.codes()); //header line
    for(int i = 0; i < validation.length; i++)
    {
      int s = i / this.rankCount;
      if ((i % this.rankCount) == 0)
      {
        sb.append('\n').append(this.suitCodes.code(s));
      }
      int v = validation[i];
      //sb.append((v > 9) ? '+' : v); //converts char ot int 43
      if(v > 9) { sb.append((char)'+'); }
      else { sb.append(v); }
    }
    return sb.append('\n').toString();
  }
  
}
