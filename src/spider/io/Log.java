package spider.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Mark
 */
public class Log
{
  public static final PrintWriter out;

  static
  {
    PrintWriter temp = null;
    String filename = System.getProperty("log");
    System.out.println("Attempting Logging to " + filename);
    if (filename != null)
    {
      try
      {
        temp = new PrintWriter(filename);
        System.out.println("Logging to " + filename);
      }
      catch(FileNotFoundException e)
      {
        System.out.println("Log could not open " + filename +
          ". Using System.out instead.");
      }
    }
    out = (temp == null)
          ? new PrintWriter(System.out, true)
          : temp;
  }

  private Log() {}

}
