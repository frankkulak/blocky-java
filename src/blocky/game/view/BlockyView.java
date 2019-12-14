package blocky.game.view;

import blocky.game.model.BlockyModel;

/**
 * Represents a view for the Blocky game.
 */
public interface BlockyView {
  /**
   * Sets listener of this view to given listener, or some default if null.
   *
   * @param listener listener to set this view's listener to
   * @throws IllegalArgumentException if given listener is null
   */
  void setListener(BlockyViewListener listener) throws IllegalArgumentException;

  /**
   * Asks the view to start rendering the game.
   *
   * @param model model to use for initial rendering
   * @throws IllegalArgumentException if given model is null
   */
  void start(BlockyModel model) throws IllegalArgumentException;

  /**
   * Notifies this view that a new level has been loaded into the given model.
   *
   * @param model model to use for rendering
   * @throws IllegalArgumentException if given model is null
   */
  void levelLoaded(BlockyModel model) throws IllegalArgumentException;

  /**
   * Notifies this view that a move has been made in the given model.
   *
   * @param model model to use for rendering
   * @throws IllegalArgumentException if given model is null
   */
  void moveMade(BlockyModel model) throws IllegalArgumentException;

  /**
   * Requests that listener display given text.
   *
   * @param text text to display to user
   */
  void display(String text);
}
