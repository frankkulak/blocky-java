package blocky.game.model.gamepieces;

import blocky.game.model.commands.DoNothingCommand;
import blocky.game.model.commands.ModelCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a game piece that cannot be entered / stops moving blocks once it's hit.
 */
abstract class SolidGamePiece extends GamePiece {
  /**
   * Constructs a new SolidGamePiece with the given name and Position.
   *
   * @param name     name of this SolidGamePiece
   * @param position position of this SolidGamePiece
   * @throws IllegalArgumentException if given position is null or invalid
   */
  SolidGamePiece(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean canBeEntered() {
    return false;
  }

  @Override
  public ModelCommand beEnteredBy(MovingGamePiece gp) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Tried to enter a solid game piece.");
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    return new DoNothingCommand();
  }

  @Override
  public GamePiece renderAs() {
    return this;
  }
}
