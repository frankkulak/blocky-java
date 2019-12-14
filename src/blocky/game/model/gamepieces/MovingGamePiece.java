package blocky.game.model.gamepieces;

import blocky.game.model.commands.ComboCommand;
import blocky.game.model.commands.MarkPositionCommand;
import blocky.game.model.commands.ModelCommand;
import blocky.game.model.commands.MoveCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a solid game piece that can move.
 */
public abstract class MovingGamePiece extends SolidGamePiece {
  /**
   * Constructs a new MovingGamePiece with the given name and position.
   *
   * @param name     name of this MovingGamePiece
   * @param position position of this MovingGamePiece
   * @throws IllegalArgumentException if position is null or invalid
   */
  MovingGamePiece(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    return new ComboCommand(new MarkPositionCommand(this), new MoveCommand(this, dir));
  }

  @Override
  public MovingGamePiece extractMovingPiece() {
    return this;
  }

  @Override
  public GamePiece replacementAfterExtraction() {
    return new Empty(this.getName(), this.getPosition());
  }

  @Override
  public ModelCommand willDelete() {
    return super.willDelete();
  }

  /**
   * Notifies this block that it has hit a winning piece so that it can determine and return what
   * the game should do about it.
   *
   * @return command representing what to do
   */
  public abstract ModelCommand hitWinningPiece();
}
