/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spider.model;

/**
 *
 * @author Mark
 */
public class Card
{

  private final int rank;
  private final int color;

  public Card(int rank, int color)
  {
    this.rank = rank;
    this.color = color;
  }

  public int rank()
  {
    return rank;
  }

  public int color()
  {
    return color;
  }

}
