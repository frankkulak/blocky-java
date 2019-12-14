package blocky.game.model.gamepieces;

import blocky.game.model.commands.ComboCommand;
import blocky.game.model.commands.ModelCommand;
import blocky.game.model.commands.DeleteCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a block that will cause the game to start over if hit by the player.
 */
public final class RedBlock extends StationaryGamePiece {
  /**
   * Constructs a new RedBlock with the given name and Position.
   *
   * @param name     name of this RedBlock
   * @param position position of this RedBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public RedBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof RedBlock) && super.equals(other);
  }

  @Override
  public RedBlock copy() {
    return new RedBlock(this.getName(), this.getPosition());
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    return new ComboCommand(super.hitBy(gp, dir), new DeleteCommand(gp));
  }

  @Override
  public String uniqueIdentifier() {
    return "R";
  }

  @Override
  int hashCodeID() {
    return 7;
  }
}
