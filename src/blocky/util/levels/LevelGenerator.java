package blocky.util.levels;

import java.util.List;
import java.util.Random;

import blocky.util.Direction;
import blocky.util.Position;

/**
 * A class used for generating levels of the Blocky game.
 */
public class LevelGenerator {
  private final int size;
  private final int minMoves;

  /**
   * Constructs a new LevelGenerator object with the given size and minimum number of moves.
   *
   * @param size     size of the game board to be generated
   * @param minMoves minimum number of moves levels should have
   * @throws IllegalArgumentException if either argument is <= 0
   */
  public LevelGenerator(int size, int minMoves) throws IllegalArgumentException {
    if (size < 1 || minMoves < 1) throw new IllegalArgumentException("Tried to construct " +
            "LevelGenerator with size < 1 or minMoves < 1.");

    this.size = size;
    this.minMoves = minMoves;
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Generates a random solvable level for the Blocky game.

   * @return random level
   * @throws RuntimeException if fails to generate level that meets requirements
   */
  public Level generateLevel() throws RuntimeException {
    Level level = null;

    // fixme this can be infinite if level with size and minMoves is impossible
    while (level == null) {
      try {
        level = this.generateLevelHelper();
      } catch (IllegalStateException e) {
        // intentionally left blank
      }
    }

    return level;
  }

  /**
   * Generates a random level in string format so that it can be inserted into a txt file.
   *
   * @return random level in string format
   */
  public String generateLevelString() {
    return this.generateLevel().toString();
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Generates a random level and returns it only if it is solvable and requires the minimum number
   * of moves, else it will throw an IllegalStateException.
   *
   * @return randomly generated level
   * @throws IllegalStateException if generated level is not solvable, or if the solution is less
   *                               moves than required by this generator
   */
  private Level generateLevelHelper() throws IllegalStateException {
    Random rand = new Random();
    StringBuilder levelString = new StringBuilder("-level\n-player ");

    // generating random player position
    Position playerPos = new Position(rand.nextInt(size) + 1, rand.nextInt(size) + 1);
    levelString.append(playerPos.row).append(" ").append(playerPos.col).append("\n");

    // generating level
    this.generateRowOfWalls(levelString); // top row of walls
    this.generateIntermediateRows(levelString, playerPos); // game pieces
    this.generateRowOfWalls(levelString); // bottom row of walls
    levelString.append("-/level"); // final tag

    // creating level from string
    Level level = LevelFileReader.parseLevel(levelString.toString());

    // making sure there is a solution and that this level requires correct number of moves
    List<Direction> solution = level.getSolution(); // might throw ISE if unsolvable
    if (solution.size() < this.minMoves) throw new IllegalStateException("Not enough moves.");
    return level;
  }

  /**
   * Adds a row of walls to the given string builder, followed by a new line.
   *
   * @param levelString string builder to add to
   */
  private void generateRowOfWalls(StringBuilder levelString) {
    for (int col = 0; col < this.size + 2; col++) levelString.append("X");
    levelString.append("\n");
  }

  /**
   * Generates all intermediate (all but first and last, since they are walls) rows for the given
   * string builder, with each new row followed by a new line.
   *
   * @param levelString string builder to add to
   * @param playerPos   position of player
   */
  private void generateIntermediateRows(StringBuilder levelString, Position playerPos) {
    int layoutSize = this.size + 2;

    for (int row = 1; row < layoutSize - 1; row++) {
      levelString.append("X");

      for (int col = 1; col < layoutSize - 1; col++) {
        Position pos = new Position(row, col);
        if (pos.equals(playerPos)) {
          levelString.append("P");
        } else {
          levelString.append(randomGamePiece());
        }
      }

      levelString.append((row == this.size / 2 + 1) ? "W\n" : "X\n");
    }
  }

  /**
   * Returns a random string representing a game piece to add to the string builder.
   *
   * @return random game piece
   */
  private static String randomGamePiece() {
    String[] pieces = {"S", "O", "C", "Y", "B", "R"};
    Random rand = new Random();
    return (rand.nextInt(4) < 3 ? "-" : pieces[rand.nextInt(pieces.length)]);
  }
}
