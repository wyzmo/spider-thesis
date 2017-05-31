package spider.game;

/**
 * Holds the game parameters and the values calculated from those
 * parameters. There are five games parameters that define the
 * pack of cards and board configuration used in a game of
 * Spider Solitaire.
 *
 * @author Mark
 */
public class Parameters
{
  private final int ranks;
  private final int suits;
  private final int decks;
  private final int columns;
  private final int deals;

  //computed values
  private final int deckSize;      //ranks*suits
  private final int packSize;      //ranks*suits*decks

  private final int dealt;         //columns*deals
  private final int hidden;        //packSize - dealt
  private final int blockCount;    //suits*decks
  private final int moveCount;     //packSize - blockCount

  public Parameters(int ranks, int suits, int decks)
  {
    this(ranks, suits, decks, 1, 1);
  }

  public Parameters(int ranks, int suits, int decks, //cards
                        int columns, int deals)      //board
  {
    this.ranks = ranks;
    this.suits = suits;
    this.decks = decks;
    this.columns = columns;
    this.deals = deals;

    this.deckSize = this.ranks * this.suits;
    this.blockCount = this.suits * this.decks;
    this.packSize = this.deckSize * this.decks;
    this.moveCount = this.packSize - this.blockCount;

    this.dealt = this.columns * this.deals;
    this.hidden = this.packSize - this.dealt;
//    if (this.hidden < 0)
//    {
//      throw new IllegalArgumentException
//       ("Number of cards to deal (columns * deals)" +
//        " is greater than pack size.");
//    }

  }

  public boolean ok()
  {
    return (ranks > 0) && (suits > 0) && (decks > 0) &&
           (columns > 0) && (deals > 0) &&
           (hidden >= 0);
  }

  public int rankCount() { return ranks; }
  public int suitCount() { return suits; }
  public int deckCount() { return decks; }
  public int deckSize() { return deckSize; }
  public int packSize() { return packSize; }
  public int columnCount() { return columns; }
  public int dealCount() { return deals; }
  public int dealSize() { return dealt; }
  public int hiddenSize() { return hidden; }
  public int blockCount() { return blockCount; }
  public int moveCount() { return moveCount; }
}
