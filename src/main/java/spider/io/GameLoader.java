package spider.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spider.game.Game;
import spider.model.Pack;
import spider.game.Settings;

/**
 *
 * @author Mark
 */
public class GameLoader          //TODO: <T extends Game> use GameFactory
{

  public List<Game> load(String filename) throws FileNotFoundException
  {
    File file = new File(filename);
    if (!file.isFile())
    {
      throw new IllegalArgumentException("Not a file: " + filename);
    }
    List<Game> result = new ArrayList<>();
    BufferedReader in = new BufferedReader(new FileReader(file));
    try
    {
      boolean readingData = false;
      Settings settings = null;

      ArrayList<String> deal = new ArrayList<>();
      for(String line = in.readLine();
          line != null;
          line = in.readLine())
      {
        line = line.trim();
        if (readingData)
        {
          if ((line.length() == 0)) //blank line
          {
            Pack p = createDeck(deal, settings);
            result.add(new Game(settings, p));
            //result.add(createDeck(deal, parms));
            deal.trimToSize();
            deal.clear();
            readingData = false;
          }
          else { deal.add(line); }
          continue;
        }
        //not yet reading the cards of a deal
        if (line.length() == 0) continue; //skip blank lines
        switch (line.charAt(0))
        {
          case '#' : continue; //comment line
          case '!' :
            if (line.startsWith("!s"))
            {
              int start = line.indexOf('(');
              int end = line.lastIndexOf(')');
              if ((start < 2) || (end < 3))
              {
                System.out.println("bad settings command: " + line);
                continue;
              }
              String[] sa = line.substring(start + 1, end).split(", *");
              if (sa.length != 5)
              {
                System.out.println("bad settings command: " + line);
                continue;
              }
              settings =
              //parms =
                new Settings(sa[0],sa[1],
                //new Parameters(sa[0].length(), sa[1].length(),
                             Integer.parseInt(sa[2]),
                             Integer.parseInt(sa[3]),
                             Integer.parseInt(sa[4]));
              System.out.println(Arrays.toString(sa));

            }
            else if (line.startsWith("!r"))
            {
              String rest = line.substring(2).trim();
              if (rest.length() > 0)
              {
                int dash = rest.indexOf('-');
                long start = 0;
                long end = 0;
                try
                {
                  if (dash < 0)
                  {
                    start = Long.parseLong(rest);
                    end = start;
                  }
                  else
                  {
                    start = Long.parseLong(rest.substring(0, dash));
                    end = Long.parseLong(rest.substring(dash + 1));
                  }
                  do
                  {
                    result.add(new Game(settings, start));
                  }
                  while (start++ < end);
                }
                catch(NumberFormatException e)
                {
                  System.out.println("!r needs numeric parameter not ==>" + rest);
                }
              }
              else
              {
                result.add(new Game(settings));
              }
            }
            continue;

          default: // must be the start of data
            System.out.println("DataStart: " + line);
            deal.add(line);
            readingData = true;
        }

      }
      if (readingData)
      {
        Pack p = createDeck(deal, settings);
        result.add(new Game(settings, p));
      }
    }
    catch (IOException e)
    {
      System.out.println("GameLoader Error:" + e);
    }

    return result;
  }

  public Pack createDeck(ArrayList<String> deal, Settings settings)
  {
    StringBuilder sb = new StringBuilder();
    for(int i = deal.size(); i > 0; i--)
    {
      sb.append(deal.get(i - 1));
    }
    ArrayList<Integer> d = new ArrayList<>(sb.length());
    for(int i = 0; i < sb.length(); i += 2)
    {
      d.add(Integer.valueOf(settings.encode(sb.charAt(i), sb.charAt(i+1))));
    }
    //Parameters p = settings.parameters();
    return new Pack(settings.ranks(), settings.suits(),
                    settings.parameters().deckCount(), d);
  }
}
