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
import blocky.util.GraphicsQuality;
import blocky.view.TextBlockyView;
import blocky.view.VisualBlockyView;

/**
 * Blocky is a puzzle game in which the player must navigate a block through obstacles in order to
 * reach the exit. Additionally, levels must be completed with the least number of moves possible.
 *
 * @author Frank Kulak
 * @version 1.0
 * @since 2019-04-12
 */
public class Blocky {
  /**
   * Main method to run the Blocky game using command line input. See README.md for detailed
   * information regarding run configurations.
   *
   * @param args list of command line arguments
   */
  public static void main(String[] args) {
    try {
      String firstArg = args[0];
      switch (firstArg) {
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
          throw new IllegalArgumentException("Unexpected token found: " + firstArg);
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Must provide run configurations.");
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PARSING INPUT
  // -----------------------------------------------------------------------------------------------

  /**
   * Parses command line input for view information. See README.md for detailed information
   * regarding run configurations.
   *
   * @param args list of command line arguments
   * @return view parsed from command line input
   * @throws IllegalArgumentException if input is malformatted
   */
  private static BlockyView parseView(String[] args) throws IllegalArgumentException {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-view")) {
        try {
          String type = args[i + 1];
          boolean dark = false;
          switch (type) {
            case "text": // text view
              return new TextBlockyView(System.in, System.out);
            case "dark": // visual view with dark theme
              dark = true;
              // intentionally no break here
            case "light": // visual view with light theme
              int size = Integer.parseInt(args[i + 2]);
              GraphicsQuality gq = GraphicsQuality.parseString(args[i + 3]);
              return new VisualBlockyView(dark, size, gq);
            default:
              throw new IllegalArgumentException("Invalid view type: " + type);
          }
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalArgumentException("Invalid number of tokens following \"-view\".");
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Non-integer given for size of view.");
        }
      }
    }

    throw new IllegalArgumentException("No view specified.");
  }

  /**
   * Parses command line input for level set information. See README.md for detailed information
   * regarding run configurations.
   *
   * @param args list of command line arguments
   * @return level set parsed from command line input
   * @throws IllegalArgumentException if input is malformatted
   */
  private static LevelSet parseLevelSet(String[] args) throws IllegalArgumentException {
    for (int i = 0; i < args.length; i++) {
      String token = args[i];
      if (token.equals("-file")) {
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
          throw new IllegalArgumentException("Expected filename after \"-file\".");
        }
      } else if (token.equals("-random")) {
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
   * Parses command line input for level generation information. See README.md for detailed
   * information regarding run configurations.
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
  // PLAYING THE GAME (run)
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
    controller.launch();
  }

  // -----------------------------------------------------------------------------------------------
  // FINDING SOLUTIONS (solve)
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and prints solutions to every level in the given LevelSet.
   *
   * @param levels LevelSet to find solutions for
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
  // GENERATING RANDOM LEVELS (gen)
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
