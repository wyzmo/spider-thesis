package spider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spider.analyze.Analyzer;
import spider.analyze.BreadthPNode;
import spider.analyze.PNode;
import spider.game.Game;
import spider.game.OldParameters;
import spider.game.Parameters;
import spider.model.Block;
import spider.model.Pack;
import spider.game.Settings;
import spider.gen.Permutation;
import spider.gen.PermutationInt;
import spider.io.GameLoader;
import spider.io.Log;
import spider.model.Rank;
import spider.model.Suit;
import spider.play.Action;
import spider.play.Board;
import spider.play.Position;

/**
 *
 * @author Mark
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
      //testShuffle();
      //testShuffle(1L);
      //testShuffle(new Settings("123", "A", 2, 1, 5));
      //testShuffle(new Settings("123", "A", 2, 2, 2));

      //testLoad("MSWinnable.txt");
      //testLoad("3games.txt");

      //testPermutation("abc", 7);
      //testPermutation("1234", 25);
      //testPermutation("112233", 91);
      //testPermutation("632541", 91);
      //testPermutationInt(new int[]{1,2,3,4,5,6}, 721);
      //testPermutationInt(new int[]{1,1,2,2,3,3}, 91);

      //testBlock();

      for(String arg : args)
      {
        //testLoad(arg);
        //testAnalyzer(arg);
        //testDepthAnalyzer(arg);
        //testFitnessAnalyzer(arg);
        testBreadthFirstAnalyzer(arg);
      }
      if (args.length > 0) return;
      //analyze2Games();
      //analyze3Games();
      analyze2r4Games();
      //analyze3x4Games();
      //analyze4Games();
      //analyzeGames(new Settings("12", "A", 2, 2, 1), new int[]{0,0,1,1});
      //analyzeGames(new Settings("12", "AB", 1, 2, 1), new int[]{0,1,2,3});
      //analyzeGames(new Settings("123", "A", 2, 2, 2), new int[]{0,0,1,1,2,2});
      //analyzeGames(new Settings("123", "AB", 1, 2, 2), new int[]{0,1,2,3,4,5});
      //analyzeGames(new Settings("1234", "A", 2, 2, 2), new int[]{0,0,1,1,2,2,3,3});
      //analyzeGames(new Settings("1234", "AB", 1, 2, 2), new int[]{0,1,2,3,4,5,6,7});
      
      Log.out.close();

    }

    static OldParameters HARDGAME = new OldParameters(new OldParameters.Deck(13,4,2), new OldParameters.Board(10,6));

    static void testShuffle()
    {
      //OldParameters p = new OldParameters(new OldParameters.Deck(13,4,2), new OldParameters.Board(10,6));
      Parameters p = new Parameters(13,4,2,10,6);
      for(int i = 0; i < 10; i++)
      {
        Game g = new Game(new Settings(Rank.STANDARDCODES, Suit.CODES, 2,10,6));
        System.out.println(g.pack().dealAsString());
        System.out.println(g.pack().dealAsCardString());
      }
    }

    static void testShuffle(long seed)
    {
        Game g = new Game(new Settings(Rank.STANDARDCODES, Suit.CODES, 2,10,6), seed);
        System.out.println(g.pack().dealAsString());
        System.out.println(g.pack().dealAsCardString());
        //System.out.println(g.deck().asInitialBoardString());
    }

//[94, 30, 75, 58, 63, 103, 43, 7, 23, 65, 73, 74, 77, 80, 40, 9, 25, 33, 62, 51, 101, 21, 35, 8, 99, 79, 98, 71, 15, 50, 96, 88, 82, 97, 92, 67, 16, 22, 3, 45, 27, 93, 11, 55, 47, 38, 95, 14, 78, 36, 81, 84, 66, 86, 37, 52, 42, 6, 102, 57, 46, 59, 91, 83, 24, 68, 32, 90, 39, 53, 20, 48, 1, 19, 2, 29, 49, 70, 87, 34, 26, 12, 76, 44, 64, 89, 60, 0, 31, 72, 5, 100, 28, 69, 18, 10, 4, 56, 13, 54, 41, 61, 85, 17]
//[94, 30, 75, 58, 63, 103, 43, 7, 23, 65, 73, 74, 77, 80, 40, 9, 25, 33, 62, 51, 101, 21, 35, 8, 99, 79, 98, 71, 15, 50, 96, 88, 82, 97, 92, 67, 16, 22, 3, 45, 27, 93, 11, 55, 47, 38, 95, 14, 78, 36, 81, 84, 66, 86, 37, 52, 42, 6, 102, 57, 46, 59, 91, 83, 24, 68, 32, 90, 39, 53, 20, 48, 1, 19, 2, 29, 49, 70, 87, 34, 26, 12, 76, 44, 64, 89, 60, 0, 31, 72, 5, 100, 28, 69, 18, 10, 4, 56, 13, 54, 41, 61, 85, 17]

    static void testShuffle(Settings s)
    {
        Game g = new Game(s);
        System.out.println(g.pack().dealAsString());
        System.out.println(g.pack().dealAsCardString());
        //System.out.println(g.deck().asInitialBoardString());
    }

    static String datadir = "C:/Users/Mark/code/spider/data/";
    static void testLoad(String filename)
    {
      GameLoader gl = new GameLoader();

      try
      {
        //List<Game> games = gl.load(datadir + filename);
        List<Game> games = gl.load(filename);
        for(Game g : games)
        {
          Board b = new Board(g);
          System.out.println();
          System.out.println("Start of New game");
          System.out.println(g.pack().dealAsString());
          System.out.println(g.pack().dealAsCardString());
          System.out.println(g.asBoardString());
          System.out.println(g.pack().validationString());
          System.out.println(g.pack().validationFormattedString());

          Position p = new Position(g.pack(), g.settings().parameters());
          ArrayList<Position> tried = new ArrayList<>();

          for(int i = 0; i < 50; i++)
          {
            tried.add(p);
            System.out.println(p);
            if (p.isWon())
            {
              b.draw(p);
              System.out.println("*** Game Won ***");
              break;
            }
            List<Action<Position>> moves = p.allMoves();
            System.out.println(moves);
            b.draw(p);
            if (moves.isEmpty()) break;
            for (Action<Position> move : moves)
            {
              Position next = move.apply(p);
              if (!tried.contains(next))
              {
                System.out.println("Moved: " + move);
                p = next;
                break;
              }
            }
            
          }
 /*         
          System.out.println(p);
          moves = p.allMoves();
          System.out.println(moves);
          b.draw(p);
          if (moves.size() > 1) p = moves.get(0).apply(p);
          
          System.out.println(p);
          moves = p.allMoves();
          System.out.println(moves);
          b.draw(p);
*/          


        }
      }
      catch (FileNotFoundException ex)
      {
        ex.printStackTrace();
      }

    }
    
    static void testAnalyzer(String filename)
    {
      GameLoader gl = new GameLoader();
      try
      {
        List<Game> games = gl.load(filename);
        for(Game g : games)
        {
          Analyzer a = new Analyzer(g);
          a.analyze();
          System.out.println("Result: " + a.winningNode());
        }
      }
      catch (FileNotFoundException ex)
      {
        ex.printStackTrace();
      }

    }
    
    static void testDepthAnalyzer(String filename)
    {
      GameLoader gl = new GameLoader();
      try
      {
        List<Game> games = gl.load(filename);
        for(Game g : games)
        {
          System.out.println(g.asBoardString());
          Analyzer a = new Analyzer(g);
          a.depthFirst();
          System.out.println("Result: " + a.winningDepthPNode());
        }
      }
      catch (FileNotFoundException ex)
      {
        ex.printStackTrace();
      }

    }
    
    static void testFitnessAnalyzer(String filename)
    {
      GameLoader gl = new GameLoader();
      try
      {
        List<Game> games = gl.load(filename);
        for(Game g : games)
        {
          System.out.println(g.asBoardString());
          Analyzer a = new Analyzer(g);
          a.fitness();
          System.out.println("Result: " + a.winningFitnessPNode());
        }
      }
      catch (FileNotFoundException ex)
      {
        ex.printStackTrace();
      }

    }
    
    static void testBreadthFirstAnalyzer(String filename)
    {
      List<Game> games = null;
      try
      {
        GameLoader gl = new GameLoader();
        games = gl.load(filename);
      }
      catch (FileNotFoundException e)
      {
        System.err.println(e);
      }
      if (games == null) return;
      int wins =0;
      int count = 0;
      ArrayList<String> results = new ArrayList<>(games.size());
      for(Game g : games)
      {
        count++;
        Long seed = g.seed();
        if (seed != null) System.out.println("!r(" + seed + ")");
        System.out.println(g.asBoardString());
        System.out.println(g.pack().validationFormattedString());
        Analyzer a = new Analyzer(g);
        a.breadthFirst();
        PNode<?> win = a.win();
        StringBuilder sb = new StringBuilder();
        sb.append(seed).append(':');          //this should be game name
        sb.append(a.sizeTried()).append(':');
        if (win == null) sb.append("F:::::::");
        else
        {
          wins++;
          sb.append("T:");
          int[] info = win.moveInfo();
          for (int i : info)
          {
            sb.append(i).append(':');
          }
        }
        sb.append(g.pack().dealAsCardString());
        String result = sb.toString();
        System.out.println("Result: " + result);
        Log.out.println(result);
        Log.out.flush();
        results.add(result);
      }
      System.out.println();
      System.out.println("----Summary----");
      for(String result : results) System.out.println(result);
      System.out.print(wins + " of " + count);
    }
    
    static void analyze2r4Games()
    {
      ArrayList<String> results = new ArrayList<>(16);
      results.add(analyzeGames(new Settings("12", "A", 2, 1, 1), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 1, 2), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 1, 3), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 1, 4), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 2, 1), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 2, 2), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 3, 1), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "A", 2, 4, 1), new int[]{0,0,1,1}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 1, 1), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 1, 2), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 1, 3), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 1, 4), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 2, 1), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 2, 2), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 3, 1), new int[]{0,1,2,3}));
      results.add(analyzeGames(new Settings("12", "AB", 1, 4, 1), new int[]{0,1,2,3}));
      System.out.println("------------------------------------");
      for(String s : results) { System.out.println(s); }
    }
    
    static void analyze3r6Games()
    {
      ArrayList<String> results = new ArrayList<>(28);
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 3), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 4), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 5), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 6), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 3), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 3, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 3, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 4, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 5, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 6, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 2), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 3), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 4), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 5), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 6), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 2), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 3), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 3, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 3, 2), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 4, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 5, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 6, 1), new int[]{0,1,2,3,4,5}));
      System.out.println("------------------------------------");
      for(String s : results) { System.out.println(s); }
    }
    
    static void analyze3x4Games()
    {
      ArrayList<String> results = new ArrayList<>(6);
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 3), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 4), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 5), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 6), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 7), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 8), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 9), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 10), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 11), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 1, 12), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 3), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 4), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 5), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 2, 6), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 3, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 3, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 3, 3), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 3, 4), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 4, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 4, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 4, 3), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 5, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 5, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 6, 1), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 4, 6, 2), new int[]{0,0,0,0,1,1,1,1,2,2,2,2}));

      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 3), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 4), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 5), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 6), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 7), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 8), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 9), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 10), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 11), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 1, 12), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 3), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 4), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 5), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 2, 6), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 3, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 3, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 3, 3), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 3, 4), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 4, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 4, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 4, 3), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 5, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 5, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 6, 1), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
      results.add(analyzeGames(new Settings("123", "AB", 2, 6, 2), new int[]{0,0,1,1,2,2,3,3,4,4,5,5}));
           
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 3), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 4), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 5), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 6), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 7), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 8), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 9), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 10), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 11), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 1, 12), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 3), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 4), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 5), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 2, 6), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 3, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 3, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 3, 3), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 3, 4), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 4, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 4, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 4, 3), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 5, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 5, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 6, 1), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));
      results.add(analyzeGames(new Settings("123", "ABCD", 1, 6, 2), new int[]{0,1,2,3,4,5,6,7,8,9,10,11}));

      
      System.out.println("------------------------------------");
      for(String s : results) { System.out.println(s); }
    }
    
    static void analyze4Games()
    {
      ArrayList<String> results = new ArrayList<>(6);
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 3), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 4), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 5), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 1, 6), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 2, 3), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 3, 1), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "A", 2, 3, 2), new int[]{0,0,1,1,2,2}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 2), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 3), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 4), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 5), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 1, 6), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 2), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 2, 3), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 3, 1), new int[]{0,1,2,3,4,5}));
      results.add(analyzeGames(new Settings("123", "AB", 1, 3, 2), new int[]{0,1,2,3,4,5}));
      System.out.println("------------------------------------");
      for(String s : results) { System.out.println(s); }
    }
    
    static String analyzeGames(Settings s, int[] cards)
    {
      //Settings s = new Settings("123", "A", 2, 2, 2);
      PermutationInt pi = new PermutationInt(cards);
      System.out.println("Analyzing: " + Arrays.toString(cards) + " " +
                         pi.isInitial() + " " + pi.isFirst());
      int wins = 0;
      int moves = 0;
      int[] counts = new int[11];
      int i = 0;
      do
      {
        Pack pack = new Pack(s, pi.currentAsList());
        Game g = new Game(s, pack);
        System.out.println(i + ". " + pack.deal());
        System.out.println(g.asBoardString());
        Analyzer a = new Analyzer(g);
        a.breadthFirst();
        PNode<?> win = a.win();
        moves = 0;
        if (win != null)
        {
          wins++;
          moves = win.length() - 1;
          counts[Math.min(moves, counts.length - 1)]++;
        }
        System.out.println("Result: " + win + " " + moves);
        System.out.println();
        pi.next(); //next permutation
        i++;
        //if (pi.isInitial()) break;
      }
      while (!pi.isInitial());
      
      String result = "!s(" + 
                         s.parameters().rankCount() + "," +
                         s.parameters().suitCount() + "," +
                         s.parameters().deckCount() + "," +
                         s.parameters().columnCount() + "," +
                         s.parameters().dealCount() + ") " +
                         wins + " of " + i + " " +
                         Arrays.toString(counts);
      System.out.println(result);
      return result;
    }

    static void testPermutation(String s, int stop)
    {
      Permutation p = new Permutation(s);
      for(int i = 0; i < stop; i++, p.next())
      {
        System.out.println(i + ". " + p.current());
      }
    }
    
    
    static void testPermutationInt(int[] ia, int stop)
    {
      PermutationInt p = new PermutationInt(ia);
      for(int i = 0; i < stop; i++, p.next())
      {
        System.out.println(i + ". " + Arrays.toString(p.current()));
      }
    }

    static void testBlock()
    {
      Block b0 = new Block(0,0);
      Block b1 = new Block(1,0);
      Block b2 = new Block(2,0);
      Block.Join j10 = b1.join(b0);
      Block.Join j20 = b2.join(j10.joined());
      Block b3 = new Block(3,0);
      Block b30 = b3.join(j20.joined()).joined();

      System.out.println(b0 + " " + b1 + " " + b2 + " " + j10.joined());
      System.out.println(j20.joined());
      System.out.println(b30);
      System.out.print("[");
      for(Block b : b30)
      {
        System.out.print(b);
        System.out.print(",");
      }
      System.out.println("]");
      Block.Join s0 = b30.split(0);
      Block.Join s1 = b30.split(1);
      Block.Join s2 = b30.split(2);
      Block.Join s3 = b30.split(3);
      Block.Join s4 = b30.split(4);
      System.out.println(s0.split() + " " + s1.split() + " " + s2.split() + " " + s3.split() + " " + s4.split());

      System.out.println(j10 + " " + j20 + " " + s0 + " " + s1 + " " + s2 + " " + s3 + " " + s4);

      s0 = b30.splitAt(0);
      s1 = b30.splitAt(1);
      s2 = b30.splitAt(2);
      s3 = b30.splitAt(3);
      s4 = b30.splitAt(-1);
      //System.out.println(s0.split() + " " + s1.split() + " " + s2.split() + " " + s3.split() + " " + s4.split());

      System.out.println("SplitAt: " + s0 + " " + s1 + " " + s2 + " " + s3 + " " + s4);
    }

}
