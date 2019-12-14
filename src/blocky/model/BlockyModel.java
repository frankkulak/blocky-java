package blocky.model;

import java.util.List;

import blocky.model.gamepieces.GamePiece;
import blocky.util.levels.Level;
import blocky.util.Direction;

/**
 * Represents a model for the Blocky game. This interface intended for use with the controller.
 */
public interface BlockyModel {
  /**
   * Restarts current level.
   *
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  void restartLevel() throws IllegalStateException;

  /**
   * Loads and begins a new level into this model.
   *
   * @param level level to load into model
   * @throws IllegalArgumentException if given level is null
   */
  void loadLevel(Level level) throws IllegalArgumentException;

  /**
   * Returns the index of the level currently loaded into this model.
   *
   * @return index of current level
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  int levelIndex() throws IllegalStateException;

  /**
   * Returns the number of columns in the current level.
   *
   * @return number of columns in current level
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  int levelWidth() throws IllegalStateException;

  /**
   * Returns the number of rows in the current level.
   *
   * @return number of rows in current level
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  int levelHeight() throws IllegalStateException;

  /**
   * Moves player block in given direction, if possible, returns if move was made successfully.
   *
   * @return whether or not move was successful
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  boolean move(Direction dir) throws IllegalStateException;

  /**
   * Returns the number of moves the player has made since the last level change / reset.
   *
   * @return number of moves made by the player
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  int movesMade() throws IllegalStateException;

  /**
   * Determines and returns whether this model has made at most the least number of moves required
   * to solve the current level (that is, it returns whether an optimal solution was found). Should
   * only be called once the current level has been beaten. This method exists rather than a boolean
   * parameter being passed in the levelBeat() listener method due to issues with recursion.
   *
   * @return whether or not optimal solution was found
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  boolean foundOptimalSolution() throws IllegalStateException;

  /**
   * Returns a copy of the current layout of the game board.
   *
   * @return layout of board
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  List<List<GamePiece>> layout() throws IllegalStateException;

  /**
   * Initializes and returns a list of all sprites in this model.
   *
   * @return list of sprites in this model
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  List<Sprite> sprites() throws IllegalStateException;

  /**
   * Finds and returns the current step that the model is at.
   *
   * @return current step to render up to
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  int curStep() throws IllegalStateException;

  /**
   * Sets the listener of this model. Model may be unusable until listener is provided.
   *
   * @param listener listener to set this model's listener to
   * @throws IllegalArgumentException if given listener is null
   */
  void setListener(BlockyModelListener listener) throws IllegalArgumentException;

  /**
   * Returns a copy of this BlockyModel with all values identical but different instances.
   *
   * @return copy of this model
   */
  BlockyModel copy();
}
