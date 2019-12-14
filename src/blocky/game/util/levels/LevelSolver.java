package blocky.game.util.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import blocky.game.model.BlockyModel;
import blocky.game.model.BlockyModelListener;
import blocky.game.model.SimpleBlockyModel;
import blocky.game.model.gamepieces.GamePiece;
import blocky.game.util.Direction;

/**
 * A class that can be used to solve levels of the Blocky game.
 */
public class LevelSolver implements BlockyModelListener {
  private boolean levelBeat;
  private boolean fatalMoveMade;

  // -----------------------------------------------------------------------------------------------
  // BlockyModelListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void levelBeat() {
    this.levelBeat = true;
  }

  @Override
  public void fatalMoveMade() {
    this.fatalMoveMade = true;
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and returns an optimal solution to the given level, if there is one.
   *
   * @param level level to solve
   * @return list of moves to optimally solve level
   * @throws IllegalArgumentException if given level is null
   * @throws IllegalStateException    if given level does not have a solution
   */
  public List<Direction> solve(Level level) throws IllegalArgumentException, IllegalStateException {
    BlockyModel model = new SimpleBlockyModel();
    model.setListener(this);
    model.loadLevel(level); // will throw IAE if level is null

    try {
      return this.solveModel(model); // will throw ISE if no solution exists
    } catch (StackOverflowError e) {
      // this means a moving block was caught between two blues... fixme remove when fixed
      throw new IllegalStateException("This level contains an instance of infinite recursion.");
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC STATIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Converts the given solution to a string to be used for printing.
   *
   * @param solution solution to a level
   * @return solution as string of moves
   */
  public static String solutionToString(List<Direction> solution) {
    StringBuilder solutionString = new StringBuilder();
    for (Direction dir : solution) solutionString.append(dirToChar(dir));
    return solutionString.toString() + ", " + solution.size();
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and returns an optimal solution to the given model, if there is one.
   *
   * @param model model to solve
   * @return list of moves to optimally solve level
   * @throws IllegalStateException if given model does not have a solution
   */
  private List<Direction> solveModel(BlockyModel model) throws IllegalStateException {
    Configuration firstConfig = new Configuration(layoutAsString(model));

    // creating and initializing HashMap
    Map<Configuration, Map<Direction, BoardStatus>> map = new HashMap<>();
    map.put(firstConfig, new HashMap<>());
    this.searchDirections(model, firstConfig, map);

    // linking all parents/children using BFS
    List<WinningMove> winningMoves = new ArrayList<>();
    LinkedList<Configuration> queue = new LinkedList<>();
    queue.add(firstConfig);
    this.establishRelationships(queue, map, new HashMap<>(), winningMoves);

    // updating solutions from root
    for (WinningMove winningMove : winningMoves) winningMove.updateParents();

    try {
      return firstConfig.solution.toList();
    } catch (NullPointerException e) {
      throw new IllegalStateException("No solution exists for this level.");
    }
  }

  /**
   * Searches for all possible moves from the current position and stores them in the map. Calls
   * itself on found moves until there are no moves left to be found and map is complete.
   *
   * @param model  model to use for searching
   * @param config configuration to search from
   * @param map    map to update with found moves
   */
  private void searchDirections(BlockyModel model,
                                Configuration config,
                                Map<Configuration, Map<Direction, BoardStatus>> map) {

    Direction[] directions = Direction.allDirections();

    for (Direction dir : directions) {
      BlockyModel modelCopy = model.copy();

      this.reset();
      if (modelCopy.move(dir)) {
        BoardStatus boardStatus;

        if (this.levelBeat) {
          // moving this way beat the game
          boardStatus = new WinningMove();
        } else if (this.fatalMoveMade) {
          // moving this way resulted in game ending
          boardStatus = new FatalMove();
        } else {
          // moving this way was a regular move
          boardStatus = new Configuration(layoutAsString(modelCopy));

          if (!map.containsKey(boardStatus)) {
            Configuration newConfig = (Configuration) boardStatus;
            map.put(newConfig, new HashMap<>());
            this.searchDirections(modelCopy, newConfig, map);
          }
        }

        map.get(config).put(dir, boardStatus);
      }
    }
  }

  /**
   * Performs breadth first search to establish parent/child relationship between BoardStatuses.
   *
   * @param queue        queue of configurations to perform BFS on
   * @param map          map to determine relationships from
   * @param visited      marks whether or not a BoardStatus has been visited yet
   * @param winningMoves list of all winning moves for this level
   */
  private void establishRelationships(LinkedList<Configuration> queue,
                                      Map<Configuration, Map<Direction, BoardStatus>> map,
                                      Map<BoardStatus, Boolean> visited,
                                      List<WinningMove> winningMoves) {

    if (queue.isEmpty()) return;

    Configuration config = queue.poll();
    Map<Direction, BoardStatus> children = map.get(config);

    for (Direction direction : children.keySet()) {
      BoardStatus child = children.get(direction);
      if (!visited.containsKey(child)) {
        visited.put(child, true);
        config.addChild(direction, child);
        if (child instanceof Configuration) queue.add((Configuration) child);
        if (child instanceof WinningMove) winningMoves.add((WinningMove) child);
      }
    }

    establishRelationships(queue, map, visited, winningMoves);
  }

  /**
   * Resets stored boolean values to false.
   */
  private void reset() {
    this.levelBeat = false;
    this.fatalMoveMade = false;
  }

  /**
   * Returns the layout as a String for use as key in HashMap.
   *
   * @param model model to represent with String
   * @return String representing model
   */
  private static String layoutAsString(BlockyModel model) {
    StringBuilder layoutString = new StringBuilder();

    List<List<GamePiece>> layout = model.layout();
    for (List<GamePiece> row : layout) {
      for (GamePiece gp : row) layoutString.append(pieceAsString(gp));
    }

    return layoutString.toString();
  }

  /**
   * Determines char to use to represent GamePiece for use in HashMap key.
   *
   * @param gp GamePiece as String
   * @return String representing GamePiece
   */
  private static String pieceAsString(GamePiece gp) {
    String str = gp.uniqueIdentifier();
    if (str.equals("W") || str.equals("X")) return "";
    return str;
  }

  /**
   * Determines and returns a single character to use to represent the given direction.
   *
   * @param dir direction to represent with a character
   * @return char representing direction
   */
  private static char dirToChar(Direction dir) {
    // gets 'L' for left, 'U' for up, etc.
    return dir.toString().toUpperCase().charAt(0);
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE CLASSES
  // -----------------------------------------------------------------------------------------------

  // BoardStatus classes

  /**
   * Represents the status of a game board, whether there is a specific configuration, or if the
   * game has been won, or if a fatal move has been made.
   */
  private abstract class BoardStatus {
    final Map<Configuration, Direction> parents; // INVARIANT: never null
    Solution solution; // INVARIANT: not null once set

    /**
     * Constructs a new BoardStatus object.
     */
    BoardStatus() {
      this.parents = new HashMap<>();
      this.solution = null;
    }

    /**
     * Adds a parent to this BoardStatus. Parents represent Configurations that can reach this
     * BoardStatus. All parents will be notified when this BoardStatus's solution is updated.
     *
     * @param parent    parent of this board status
     * @param direction direction this child was reached from parent
     */
    void addParent(Configuration parent, Direction direction) {
      if (!this.parents.containsKey(parent)) {
        this.parents.put(parent, direction);
        parent.addChild(direction, this);
      }
    }

    /**
     * Lets all parents of this status know that its child has been updated.
     */
    void updateParents() {
      for (Configuration parent : this.parents.keySet()) {
        parent.childUpdated(this.parents.get(parent));
      }
    }
  }

  /**
   * Represents a configuration of the game board where at least one move can still be made.
   */
  private class Configuration extends BoardStatus {
    final String key; // INVARIANT: never null or empty
    final Map<Direction, BoardStatus> children; // INVARIANT: never null

    /**
     * Constructs a new Configuration object.
     *
     * @param key a string representing this configuration for use as key in HashMap
     * @throws IllegalArgumentException if given string is null or empty
     */
    Configuration(String key) throws IllegalArgumentException {
      super();

      if (key == null || key.isEmpty()) {
        throw new IllegalArgumentException("Key must be non-null and non-empty.");
      }

      this.key = key;
      this.children = new HashMap<>();
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Configuration)) return false;
      Configuration that = (Configuration) other;
      return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
      return this.key.hashCode();
    }

    /**
     * Adds a child to this Configuration, and adds this Configuration as parent to child.
     *
     * @param directionOfChild direction that child can be reached from
     * @param child            child to add to this Configuration
     */
    void addChild(Direction directionOfChild, BoardStatus child) {
      if (!this.children.containsKey(directionOfChild)) {
        this.children.put(directionOfChild, child);
        child.addParent(this, directionOfChild);
      }
    }

    /**
     * Notifies this Configuration that its child has updated its solution.
     *
     * @param directionOfChild direction of child that updated
     */
    void childUpdated(Direction directionOfChild) {
      Solution childSolution = this.children.get(directionOfChild).solution;
      if (this.solution == null || ((childSolution.size() + 1 < this.solution.size()))) {
        this.solution = new WinningSolution(directionOfChild, childSolution);
        this.updateParents();
      }
    }
  }

  /**
   * Represents a winning move made by the player, meaning this is the final step of a solution.
   */
  private class WinningMove extends BoardStatus {
    /**
     * Constructs a new WinningMove object.
     */
    WinningMove() {
      super();
      this.solution = new EndOfSolution();
    }
  }

  /**
   * Represents a fatal move made by the player, meaning there is no solution here.
   */
  private class FatalMove extends BoardStatus { }

  // Solution classes (same concept as ConsList)

  /**
   * Represents a solution with a series of directions as steps.
   */
  private abstract class Solution {
    /**
     * Finds and returns the number of steps in this solution.
     *
     * @return size of this solution
     */
    abstract int size();

    /**
     * Converts this solution to a list of directions.
     *
     * @return list of directions representing solution
     */
    abstract List<Direction> toList();
  }

  /**
   * Represents a winning solution.
   */
  private class WinningSolution extends Solution {
    Direction move;
    Solution rest;
    private final int size;

    /**
     * Constructs a new WinningSolution object.
     *
     * @param move the first direction of this solution
     * @param rest the rest of this solution
     */
    WinningSolution(Direction move, Solution rest) {
      this.move = move;
      this.rest = rest;
      this.size = rest.size() + 1;
    }

    @Override
    int size() {
      return this.size;
    }

    @Override
    List<Direction> toList() {
      List<Direction> list = new ArrayList<>();
      list.add(this.move);
      list.addAll(this.rest.toList());
      return list;
    }
  }

  /**
   * Represents the end of a solution.
   */
  private class EndOfSolution extends Solution {
    @Override
    int size() {
      return 0;
    }

    @Override
    List<Direction> toList() {
      return new ArrayList<>();
    }
  }
}
