package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;
import blocky.game.model.gamepieces.Empty;
import blocky.game.model.gamepieces.GamePiece;
import blocky.game.util.Position;

/**
 * A command to delete a game piece.
 */
public class DeleteCommand extends ChangedStatesCommand {
  /**
   * Creates new DeleteCommand targeting given GamePiece.
   *
   * @param gp GamePiece to delete
   */
  public DeleteCommand(GamePiece gp) {
    super(gp, new Empty(gp.getName(), gp.getPosition()));
  }

  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    // updating sprite
    super.execute(model);

    // actually deleting piece
    Position pos = this.gamePiece.getPosition();
    boolean pieceChange = model.deletePieceAt(pos);
    return this.gamePiece.willDelete().execute(model) || pieceChange;
  }
}
