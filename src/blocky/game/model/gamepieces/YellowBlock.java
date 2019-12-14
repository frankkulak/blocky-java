package blocky.game.model.gamepieces;

import blocky.game.model.commands.ModelCommand;
import blocky.game.model.commands.DeleteCommand;
import blocky.game.util.Position;

/**
 * Represents a block that can be hit and will continue moving in that direction.
 */
public final class YellowBlock extends MovingGamePiece {
  /**
   * Constructs a new YellowBlock with the given name and Position.
   *
   * @param name     name of this YellowBlock
   * @param position position of this YellowBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public YellowBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof YellowBlock) && super.equals(other);
  }

  @Override
  public YellowBlock copy() {
    return new YellowBlock(this.getName(), this.getPosition());
  }

  @Override
  public ModelCommand hitWinningPiece() {
    return new DeleteCommand(this);
  }

  @Override
  public String uniqueIdentifier() {
    return "Y";
  }

  @Override
  int hashCodeID() {
    return 8;
  }
}
