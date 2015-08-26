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
public class Rank extends CardCode
{
  public static final String DEFAULTCODES = 
          "123456789abcdefghijklmnopqrstuvwxyz";
  public static final String STANDARDCODES = "A23456789TJQK";
  public static final List<String> STANDARDNAMES = 
    Arrays.asList
      ("Ace", 
       "2", "3", "4", "5", "6", "7", "8", "9", 
       "Ten", "Jack", "Queen", "King"
      );
  
  public Rank()
  {
    super(STANDARDCODES, STANDARDNAMES);   //Use default values
  }
  
  public Rank(int n)
  {
    this(DEFAULTCODES.substring(0, n));   //Use default values
  }
  
  public Rank(String codes)
  {
    super(codes, null);
  }
  
  public Rank(String codes, List<String> names)
  {
    super(codes, names);
  }
}
