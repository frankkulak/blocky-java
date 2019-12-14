package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;
import blocky.game.model.gamepieces.GamePiece;
import blocky.game.util.Direction;

/**
 * Represents a command to move a certain block in the specified direction.
 */
public class MoveCommand extends GamePieceCommand {
  private final Direction direction;

  /**
   * Creates new MoveCommand targeting given GamePiece in given Direction.
   *
   * @param gp  GamePiece to move
   * @param dir direction to move in
   */
  public MoveCommand(GamePiece gp, Direction dir) {
    super(gp);
    this.direction = dir;
  }

  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    return model.movePieceAt(this.gamePiece.getPosition(), this.direction);
  }
}
