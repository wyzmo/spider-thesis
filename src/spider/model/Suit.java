/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spider.model;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Mark
 */
public class Suit extends CardCode
{
  public static final String DEFAULTCODES =
          "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String CODES = "CDHS";
  public static final List<String> NAMES = 
    Arrays.asList("Clubs", "Diamond", "Hearts", "Spades");
  
  public Suit()
  {
    super(CODES, NAMES);   //Use default values
  }
  
  public Suit(int n)
  {
    this(DEFAULTCODES.substring(0, n));   //Use default values
  }
  
  public Suit(String codes)
  {
    super(codes, null);
  }
  
  public Suit(String codes, List<String> names)
  {
    super(codes, names);
  }
}
