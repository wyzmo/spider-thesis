/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spider.gen;
import java.util.Arrays;

/**
 *
 * @author Mark
 */
public class Permutation
{
  /**
   * Knuth 7.2.1.2 Algorithm L
   */
  private final StringBuilder sb;
  private final String first;     //lexigraphically first permutation
  private final String initial;   //supplied by constructor           
  String current;
  int last;
  
  public Permutation(String initial)
  {
    this.sb = new StringBuilder(initial);
    this.first = Permutation.first(initial);
    this.initial = initial;
  }
  
  public static String first(String initial)
  {
    char[] ca = initial.toCharArray();
    Arrays.sort(ca);
    return new String(ca);
  }
  
  public String current() { return sb.toString(); }
  public String initial() { return initial; }
  
  public void next()
  {
    int j = sb.length() - 2;
    while((j >= 0) && (sb.charAt(j) >= sb.charAt(j + 1))) j--;
    if (j < 0) 
    {
      sb.setLength(0);
      sb.append(first);
      return;
    }
    int el = sb.length() - 1;
    while (sb.charAt(j) >= sb.charAt(el)) el--;
    swap(j, el);
    int k = j + 1;
    el = sb.length() - 1;
    while(k < el)
    {
      swap(k, el);
      k++;
      el--;
    } 
  }
  
  private void swap(int i, int j)
  {
    char temp = sb.charAt(i);
    sb.setCharAt(i, sb.charAt(j));
    sb.setCharAt(j, temp);
  }
}
