package blocky.util.levels;

import java.util.ArrayList;
import java.util.List;

import blocky.model.gamepieces.*;
import blocky.util.Direction;
import blocky.util.Position;

/**
 * Represents a playable level of the Blocky game.
 */
public class Level {
  private final int id; // INVARIANT: always > 0
  private final List<List<GamePiece>> initLayout; // INVARIANT: never null
  private final Position initPlayerPosition; // INVARIANT: never null, within bounds of board
  private List<Direction> solution; // INVARIANT: not null once set

  /**
   * Constructs new Level with given id, layout, and player position.
   *
   * @param id  id of level
   * @param lo  initial level layout
   * @param pos initial player position
   * @throws IllegalArgumentException if layout or position are null, if position is incorrect or
   *                                  off the board, or if ID is < 1
   */
  private Level(int id, List<List<GamePiece>> lo, Position pos) throws IllegalArgumentException {
    if (lo == null || pos == null) {
      throw new IllegalArgumentException("Initial layout and position must be non-null.");
    } else if (id < 1) {
      throw new IllegalArgumentException("Level ID must be > 0.");
    }

    // checking that player position is on the board and correct
    try {
      GamePiece player = pos.getItem(lo);
      if (!(player.renderAs() instanceof PlayerBlock)) {
        throw new IllegalArgumentException("Player index for level " + id + " is incorrect.");
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Player position is not on game board.");
    }

    this.id = id;
    this.initLayout = lo;
    this.initPlayerPosition = pos;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("-level\n-player ");
    Position pos = this.initPlayerPosition;
    stringBuilder.append(pos.row).append(" ").append(pos.col).append("\n");

    for (List<GamePiece> row : this.initLayout) {
      for (GamePiece gp : row) stringBuilder.append(gamePieceToChar(gp.renderAs()));
      stringBuilder.append('\n');
    }

    stringBuilder.append("-/level");
    return stringBuilder.toString();
  }

  // -----------------------------------------------------------------------------------------------
  // BUILDER
  // -----------------------------------------------------------------------------------------------

  /**
   * Builder for creating Level objects.
   */
  public static class Builder {
    private int id;
    private List<List<GamePiece>> layout;
    private Position playerPosition;
    private boolean playerAdded;

    /**
     * Constructs a new Level.Builder object.
     */
    public Builder() {
      // all of these must be changed or else the constructor will throw
      this.id = 0;
      this.layout = new ArrayList<>();
      this.playerPosition = new Position(0, 0);
      this.playerAdded = false;
    }

    /**
     * Sets the ID of the Level to be built.
     *
     * @param id id of level to be built
     * @return this builder
     */
    public Builder setID(int id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a row to this Level to which GamePieces can be added.
     *
     * @return this builder
     */
    public Builder addRow() {
      this.layout.add(new ArrayList<>());
      return this;
    }

    /**
     * Adds the given GamePiece to the current row of this Level.
     *
     * @param gp GamePiece to add to row
     * @return this builder
     * @throws IllegalArgumentException if given GamePiece is null
     * @throws IllegalStateException    if there is currently no row to add to, or if trying to add
     *                                  a player when a player already exists
     */
    public Builder addPieceToRow(GamePiece gp)
            throws IllegalArgumentException, IllegalStateException {

      if (gp == null) {
        throw new IllegalArgumentException("Tried to add null GamePiece to Level.");
      }

      if (this.layout.size() == 0) {
        throw new IllegalStateException("Tried to add GamePiece before row in Level.Builder.");
      }

      if (gp instanceof PlayerBlock) {
        if (this.playerAdded) throw new IllegalStateException("Tried to add a second PlayerBlock.");
        this.playerAdded = true;
      }

      this.layout.get(this.layout.size() - 1).add(gp);
      return this;
    }

    /**
     * Sets the player position for this Level.
     *
     * @param pos position of player
     * @return this builder
     */
    public Builder setPlayerPosition(Position pos) {
      this.playerPosition = pos;
      return this;
    }

    /**
     * Attempts to build a Level object using the given information.
     *
     * @return built Level
     * @throws IllegalStateException if there are any issues with building Level as specified
     */
    public Level build() throws IllegalStateException {
      try {
        return new Level(this.id, this.layout, this.playerPosition);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("Could not build level: " + e.getMessage());
      }
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns ID of this level.
   *
   * @return ID of level
   */
  public int getID() {
    return this.id;
  }

  /**
   * Finds and returns an optimal solution to this level.
   *
   * @return optimal solution for this level
   * @throws IllegalStateException if this level is unsolvable
   */
  public List<Direction> getSolution() throws IllegalStateException {
    if (this.solution == null) {
      LevelSolver solver = new LevelSolver();
      this.solution = solver.solve(this);
    }

    // wrapping solution so that it cannot be mutated
    return new ArrayList<>(this.solution);
  }

  /**
   * Returns number of moves needed for optimal solution of this level.
   *
   * @return number of moves needed for optimal solution
   */
  public int getMoves() {
    return this.getSolution().size();
  }

  /**
   * Returns copy of initial position of player in this level.
   *
   * @return copy of initial player position
   */
  public Position getPlayerPosition() {
    // copying position so that it cannot be mutated
    return this.initPlayerPosition.copy();
  }

  /**
   * Returns copy of initial layout of this level; runs in O(n^2) time.
   *
   * @return copy of initial layout of this level
   */
  public List<List<GamePiece>> getLayout() {
    List<List<GamePiece>> layout = new ArrayList<>();

    // copying all rows to layout
    for (List<GamePiece> row : this.initLayout) {
      // creating new row to place in layout
      List<GamePiece> newRow = new ArrayList<>();
      // copying all pieces in row to new row
      for (GamePiece gp : row) newRow.add(gp.copy());
      // adding new row to layout
      layout.add(newRow);
    }

    return layout;
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Determines and returns character to use to represent game piece in level data file.
   *
   * @param gp game piece to convert to character
   * @return char representing given game piece
   * @throws IllegalArgumentException if given game piece is null or unrecognized
   */
  private static char gamePieceToChar(GamePiece gp) throws IllegalArgumentException {
    if (gp instanceof Wall) {
      return 'X';
    } else if (gp instanceof Empty) {
      return '-';
    } else if (gp instanceof WinningPiece) {
      return 'W';
    } else if (gp instanceof PlayerBlock) {
      return 'P';
    } else if (gp instanceof SolidBlock) {
      return 'S';
    } else if (gp instanceof PopBlock) {
      return 'O';
    } else if (gp instanceof CrackedBlock) {
      return 'C';
    } else if (gp instanceof YellowBlock) {
      return 'Y';
    } else if (gp instanceof RedBlock) {
      return 'R';
    } else if (gp instanceof BlueBlock) {
      return 'B';
    } else {
      throw new IllegalArgumentException("GamePiece not recognized.");
    }
  }
}
