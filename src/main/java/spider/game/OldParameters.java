/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spider.game;

/**
 *
 * @author Mark
 */
public final class OldParameters
{
  public static final class Deck
  {
    private int ranks;
    private int suits;
    private int multiples;
    private int cards;

    
    public Deck(int ranks, int suits, int multiples)
    {
      this.ranks = ranks;
      this.suits = suits;
      this.multiples = multiples;
      cards = ranks * suits * multiples;
    }
    
    public int ranks() { return ranks; }
    public int suits() { return suits; }
    public int multiples() { return multiples; }
    public int cards() { return cards; }
  }
  
  public static final class Board
  {
    private int columns;
    private int deals;
    
    public Board(int columns, int deals)
    {
      this.columns = columns;
      this.deals = deals;
    }
    
    public int columns() { return columns; }
    public int deals() { return deals; }
  }
  
  private final Deck deck;
  private final Board board;
  private final int dealt;
  private final int hidden;
  
  public OldParameters(Deck deck, Board board)
  {
    if ((deck == null) || (board == null))
    {
      throw new IllegalArgumentException
              ("Deck and board must be specified");
    }
    this.deck = deck;
    this.board = board;
    dealt = board.columns * board.deals;
    hidden = deck.cards - dealt;
    if (hidden < 0)
    {
      throw new IllegalArgumentException
              ("Number of deals greater than number of cards.");
    }
  }
  
  public Deck deck() { return deck; }
  public Board board() { return board; }
  
}
