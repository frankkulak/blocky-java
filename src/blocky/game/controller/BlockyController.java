package blocky.game.controller;

import blocky.game.model.BlockyModelListener;
import blocky.game.view.BlockyViewListener;

/**
 * Represents a controller for the Blocky game.
 */
public interface BlockyController extends BlockyModelListener, BlockyViewListener {
  /**
   * Launches the game using this controller.
   */
  void launch();
}
