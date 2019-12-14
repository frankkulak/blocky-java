package blocky;

import java.util.List;

import blocky.controller.BlockyController;
import blocky.controller.SimpleBlockyController;
import blocky.model.BlockyModel;
import blocky.model.SimpleBlockyModel;
import blocky.util.levels.Level;
import blocky.util.levels.LevelSet;
import blocky.util.Direction;
import blocky.util.levels.LevelFileReader;
import blocky.util.levels.LevelGenerator;
import blocky.util.levels.LevelSolver;
import blocky.util.levels.RandomLevelSet;
import blocky.view.BlockyView;
import blocky.view.GraphicsQuality;
import blocky.view.TextBlockyView;
import blocky.view.VisualBlockyView;

/**
 * Blocky is a fun level-based puzzle game where the player must figure out how to navigate their
 * block through obstacles in order to get to the exit. Levels must be completed with the least
 * number of moves possible in order to advance.
 *
 * @author Frank Kulak
 * @version 1.0
 * @since 2019-04-12
 */
public class Blocky {
  /**
   * <p>Main method to run the Blocky game using command line input.</p>
   *
   * <p>Examples of command line input:</p>
   * <p> - "run -view VIEW_TYPE -file FILE_NAME"</p>
   * <p> - "run -view VIEW_TYPE SIZE QUALITY -random SIZE MOVES"</p>
   * <p> - "solve -file FILE_NAME"</p>
   * <p> - "gen SIZE MOVES QUANTITY"</p>
   *
   * <p>VIEW_TYPE = one of: "text", "dark", "light"</p>
   * <p>SIZE      = size of view in pixels (only required for visual view)</p>
   * <p>QUALITY   = quality of view to display (only required for visual view)</p>
   * <p>FILE_NAME = name of level input file between "levelData/" and "Levels.txt"
   *               (EX: "intro" will be understood as "levelData/introLevels.txt")</p>
   * <p>SIZE      = size of game boards to generate
   *               (EX: "5" will result in 5x5 game board being generated)</p>
   * <p>MOVES     = minimum number of moves required to solve random levels
   *               (EX: "5" will result in levels that require 5+ moves to solve)</p>
   * <p>QUANTITY  = number of levels to generate
   *               (EX: "10" will randomly generate and print 10 levels)</p>
   *
   * @param args list of command line arguments
   */
  public static void main(String[] args) {
    switch(args[0]) {
      case "run":
        runGame(parseView(args), parseLevelSet(args));
        break;
      case "solve":
        findSolutions(parseLevelSet(args));
        break;
      case "gen":
        parseGenerationArgs(args);
        break;
      default:
        throw new IllegalArgumentException("Unexpected token found: " + args[0]);
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PARSING INPUT
  // -----------------------------------------------------------------------------------------------

  /**
   * Parses command line input for view information. Input should contain the subarray {[i] =
   * "-view", [i + 1] = VIEW_TYPE} where VIEW_TYPE is either "text", "dark", or "light" ("text"
   * corresponds with a command-line-based text view, and "dark" and "light" correspond to a visual
   * view with either a dark or light theme). If the view type is either "light" or "dark", there
   * must be an additional two arguments at [i + 2] and [i + 3]: the size of the window to use for
   * the view, and the quality to render the view at. The size must be given as an integer, and the
   * quality must be one of {"low", "med", "high", "best"}.
   *
   * @param args list of command line arguments
   * @return view parsed from command line input
   * @throws IllegalArgumentException if input is malformatted
   */
  private static BlockyView parseView(String[] args) throws IllegalArgumentException {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-view")) {
        try {
          GraphicsQuality gq;
          boolean dark = false;
          switch (args[i + 1]) {
            case "text":
              return new TextBlockyView(System.in, System.out);
            case "dark": // visual view with dark theme
              dark = true;
            case "light": // visual view with light theme
              gq = parseGraphicsQuality(args[i + 3]);
              return new VisualBlockyView(dark, Integer.parseInt(args[i + 2]), gq);
            default:
              throw new IllegalArgumentException("Invalid view type: " + args[i + 1]);
          }
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalArgumentException("Invalid number of tokens following \"-view\".");
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Found string where integer was expected.");
        }
      }
    }

    throw new IllegalArgumentException("No view specified.");
  }

  /**
   * Parses and returns GraphicsQuality from the given string.
   *
   * @param str string to parse GraphicsQuality from
   * @return GraphicsQuality parsed from string
   * @throws IllegalArgumentException if string could not be parsed as GraphicsQuality
   */
  private static GraphicsQuality parseGraphicsQuality(String str) throws IllegalArgumentException {
    switch (str) {
      case "low":
        return GraphicsQuality.LOW;
      case "med":
        return GraphicsQuality.MED;
      case "high":
        return GraphicsQuality.HIGH;
      case "best":
        return GraphicsQuality.BEST;
      default:
        throw new IllegalArgumentException("Could not parse " + str + " as a graphics quality.");
    }
  }

  /**
   * Parses level set info given in command line input and returns corresponding level set. Input
   * should contain one of the following subarrays: {[i] = "-file", [i + 1] = FILE_NAME} or {[i] =
   * "-random", [i + 1] = SIZE, [i + 2] = MOVES}.
   *
   * @param args list of command line arguments
   * @return level set parsed from command line input
   * @throws IllegalArgumentException if input is malformatted
   */
  private static LevelSet parseLevelSet(String[] args) throws IllegalArgumentException {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-file")) {
        try {
          String fileName = "levelData/" + args[i + 1];
          try {
            return LevelFileReader.parseLevelFile(fileName);
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("File " + fileName + " not found.");
          } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Error in " + fileName + ": " + e.getMessage());
          }
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalArgumentException("Expected text file name after \"-file\".");
        }
      } else if (args[i].equals("-random")) {
        try {
          int size = Integer.parseInt(args[i + 1]);
          int moves = Integer.parseInt(args[i + 2]);
          LevelGenerator generator = new LevelGenerator(size, moves);
          return new RandomLevelSet(generator);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
          throw new IllegalArgumentException("Expected two integer tokens after \"-random\".");
        }
      }
    }

    throw new IllegalArgumentException("No level set data source specified.");
  }

  /**
   * Parses command line input for generating random levels. Input array should have four elements:
   * {[0] = "gen", [1] = SIZE, [2] = MOVES, [3] = QUANTITY}.
   *
   * @param args list of command line arguments
   * @throws IllegalArgumentException if input is malformatted
   */
  private static void parseGenerationArgs(String[] args) throws IllegalArgumentException {
    try {
      int size = Integer.parseInt(args[1]);
      int moves = Integer.parseInt(args[2]);
      int quantity = Integer.parseInt(args[3]);
      generateRandomLevels(size, moves, quantity);
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Expected three integer tokens after \"gen\".");
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PLAYING THE GAME
  // -----------------------------------------------------------------------------------------------

  /**
   * Runs the game with the given view and level set.
   *
   * @param view   view to render game with
   * @param levels level set to use for game
   */
  private static void runGame(BlockyView view, LevelSet levels) {
    BlockyModel model = new SimpleBlockyModel();
    BlockyController controller = new SimpleBlockyController(model, view, levels);

    // starting the game
    controller.launch();
  }

  // -----------------------------------------------------------------------------------------------
  // FINDING SOLUTIONS
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and prints solutions to every level in the given level set.
   *
   * @param levels level set to find solutions for
   */
  private static void findSolutions(LevelSet levels) {
    for (int i = 1; i <= levels.size(); i++) {
      Level level = levels.goToLevel(i);
      List<Direction> solution = level.getSolution();
      int moves = solution.size();
      String solutionString = LevelSolver.solutionToString(solution);
      System.out.println("Level " + i + ": Moves = " + solutionString + ", " + moves);
    }
  }

  // -----------------------------------------------------------------------------------------------
  // GENERATING RANDOM LEVELS
  // -----------------------------------------------------------------------------------------------

  /**
   * Generates {@param quantity} levels of size {@param size} which require at least {@param moves}
   * moves in order to be solved.
   *
   * @param size     size of levels to generate
   * @param moves    minimum number of moves required to solve levels
   * @param quantity number of levels to generate
   */
  private static void generateRandomLevels(int size, int moves, int quantity) {
    LevelGenerator generator = new LevelGenerator(size, moves);
    for (int i = 0; i < quantity; i++) System.out.println(generator.generateLevelString() + "\n");
  }
}
