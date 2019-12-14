package blocky.game.model.gamepieces;

import blocky.game.model.commands.ComboCommand;
import blocky.game.model.commands.ModelCommand;
import blocky.game.model.commands.MoveCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a block that causes moving blocks to reverse direction when hit.
 */
public final class BlueBlock extends StationaryGamePiece {
  /**
   * Constructs a new BlueBlock with the given name and Position.
   *
   * @param name     name of this BlueBlock
   * @param position position of this BlueBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public BlueBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof BlueBlock) && super.equals(other);
  }

  @Override
  public BlueBlock copy() {
    return new BlueBlock(this.getName(), this.getPosition());
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    // stop block, then reverse its direction
    return new ComboCommand(super.hitBy(gp, dir), new MoveCommand(gp, dir.opposite()));
  }

  @Override
  public String uniqueIdentifier() {
    return "B";
  }

  @Override
  int hashCodeID() {
    return 9;
  }
}
