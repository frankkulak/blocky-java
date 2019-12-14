package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;
import blocky.game.model.gamepieces.GamePiece;

/**
 * A command to let the model know that some block has changed states.
 */
public class ChangedStatesCommand extends GamePieceCommand {
  final GamePiece after;

  /**
   * Constructs a new ChangedStatesCommand that represents a GamePiece becoming another GamePiece.
   *
   * @param before GamePiece that is changing states
   * @param after  GamePiece that before is becoming
   */
  public ChangedStatesCommand(GamePiece before, GamePiece after) {
    super(before);
    this.after = after;
  }

  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    model.updateSpriteRender(this.gamePiece, this.after);
    return true;
  }
}
