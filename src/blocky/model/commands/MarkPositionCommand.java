package blocky.model.commands;

import blocky.model.gamepieces.GamePiece;

/**
 * A command to mark where a certain GamePiece is in a Sprite so it does not move too early.
 */
public class MarkPositionCommand extends ChangedStatesCommand {
  /**
   * Constructs a new MarkPositionCommand targeting the given GamePiece.
   *
   * @param gamePiece GamePiece to mark position of
   */
  public MarkPositionCommand(GamePiece gamePiece) {
    super(gamePiece, gamePiece);
  }
}
