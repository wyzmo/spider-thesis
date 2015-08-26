/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spider.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mark
 */
public class CardCode
{
  private final String codes;       //treated as char[]
  private final List<String> names; //treated as String array
  
  public CardCode(String codes, List<String> names)
  {
    if ((codes == null) || "".equals(codes))
    {
      throw new IllegalArgumentException
        ("codes must be non null and non-empty");
    }
    this.codes = codes;
    
    if (names == null)
    {
      this.names = new ArrayList<>(codes.length());
      for(int i = 0; i < codes.length(); i++)
      {
        this.names.add(codes.substring(i, i + 1));
      }   
    }
    else
    {
      this.names = names;
    }
    
    if (this.codes.length() != this.names.size())
    {
      throw new IllegalArgumentException
        ("codes and names must be the same length");
    }
  }
  
  public int size() { return codes.length(); }
  public char code(int i) { return codes.charAt(i); }
  public String codes() { return codes; }
  public String name(int i) { return names.get(i); }
  public int codeIndex(char code) { return codes.indexOf(code); }
  public int nameIndex(String name) { return names.indexOf(name); }
}
