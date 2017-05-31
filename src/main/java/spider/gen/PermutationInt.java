package spider.gen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Mark
 */
public class PermutationInt
{
  /**
   * Knuth 7.2.1.2 Algorithm L
   */

  private int[] ia;                 //current permutation
  private final int[] first;        //first in sort order
  private final int[] initial;      //array supplied in constructor

  public PermutationInt(int[] initial)
  {
    this.ia = initial.clone();      //copy 
    this.first = initial.clone();   //another copy to sort
    Arrays.sort(this.first);
    this.initial = initial;         //initial supplied by user
  }
  
  public int[] current() { return ia.clone(); }
  public int[] initial() { return initial; }
  
  public List<Integer> currentAsList()
  { 
    ArrayList<Integer> list = new ArrayList<>(ia.length);
    for(int i : ia)
    { 
      list.add(Integer.valueOf(i)); 
    }
    return list; 
  }
  
  public boolean isInitial() { return Arrays.equals(ia, initial); }
  public boolean isFirst() { return Arrays.equals(ia, first); }
  
  public void next()
  {
    int j = ia.length - 2;
    while((j >= 0) && (ia[j] >= ia[j + 1])) j--;
    if (j < 0) 
    {
      ia = first.clone();
      return;
    }
    int el = ia.length - 1;
    while (ia[j] >= ia[el]) el--;
    swap(j, el);
    int k = j + 1;
    el = ia.length - 1;
    while(k < el)
    {
      swap(k, el);
      k++;
      el--;
    } 
  }
  
  private void swap(int i, int j)
  {
    int temp = ia[i];
    ia[i] = ia[j];
    ia[j] = temp;
  }
}
