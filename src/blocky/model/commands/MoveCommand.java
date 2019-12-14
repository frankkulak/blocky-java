package blocky.model.commands;

import blocky.model.CommandBlockyModel;
import blocky.model.gamepieces.GamePiece;
import blocky.util.Direction;

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
