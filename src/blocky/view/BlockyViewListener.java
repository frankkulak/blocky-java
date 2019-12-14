package blocky.view;

import blocky.util.Direction;

/**
 * Represents a listener of a BlockyView. Intended for use with the controller so as to let it know
 * when the user can make another move (when animations have completed).
 */
public interface BlockyViewListener {
  /**
   * Notifies this listener that the view has finished rendering. The listener should now allow the
   * user to continue making moves.
   */
  void finishedRendering();

  /**
   * Performs a move in the specified direction.
   *
   * @param dir direction to move in
   * @throws IllegalArgumentException if given direction is null
   * @throws IllegalStateException    if cannot currently move in given direction
   */
  void move(Direction dir) throws IllegalArgumentException, IllegalStateException;

  /**
   * Goes to the next level.
   *
   * @throws IllegalStateException if currently on last level
   */
  void nextLevel() throws IllegalStateException;

  /**
   * Goes back to the previous level.
   *
   * @throws IllegalStateException if currently on first level
   */
  void prevLevel() throws IllegalStateException;

  /**
   * Goes to the specified level (with indexing starting at 1).
   *
   * @param level level to go to
   * @throws IllegalArgumentException if level does not exist in set
   */
  void goToLevel(int level) throws IllegalArgumentException;

  /**
   * Restarts the current level.
   */
  void restartLevel();

  /**
   * Solves and displays solution to current level.
   */
  void solveLevel();

  /**
   * Prints the current level to the console (developer use only).
   */
  void printLevel();

  /**
   * Displays a help menu.
   */
  void help();

  /**
   * Terminates the game with the given message.
   *
   * @param message message to display before termination
   */
  void terminate(String message);
}
