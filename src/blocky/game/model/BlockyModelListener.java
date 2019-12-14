package blocky.game.model;

/**
 * Represents a listener of a BlockyModel.
 */
public interface BlockyModelListener {
  /**
   * Notifies listener that current level has been beat.
   */
  void levelBeat();

  /**
   * Notifies listener that a fatal move has been made. Fatal moves include hitting blocks that
   * cause the level to restart, the game being in an invalid state, and reloading the level.
   */
  void fatalMoveMade(); // fixme add String message
}
