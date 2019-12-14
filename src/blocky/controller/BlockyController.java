package blocky.controller;

import blocky.model.BlockyModelListener;
import blocky.view.BlockyViewListener;

/**
 * Represents a controller for the Blocky game.
 */
public interface BlockyController extends BlockyModelListener, BlockyViewListener {
  /**
   * Launches the game using this controller.
   */
  void launch();
}
