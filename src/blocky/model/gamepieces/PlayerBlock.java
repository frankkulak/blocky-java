package blocky.model.gamepieces;

import blocky.model.commands.ModelCommand;
import blocky.model.commands.FatalMoveCommand;
import blocky.model.commands.WinGameCommand;
import blocky.util.Position;

/**
 * Represents the player's block in the Blocky game.
 */
public final class PlayerBlock extends MovingGamePiece {
  /**
   * Constructs a new PlayerBlock with the given name and Position.
   *
   * @param name     name of this PlayerBlock
   * @param position position of this PlayerBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public PlayerBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof PlayerBlock) && super.equals(other);
  }

  @Override
  public PlayerBlock copy() {
    return new PlayerBlock(this. getName(), this.getPosition());
  }

  @Override
  public ModelCommand willDelete() {
    super.willDelete();
    return new FatalMoveCommand();
  }

  @Override
  public ModelCommand hitWinningPiece() {
    return new WinGameCommand();
  }

  @Override
  public String uniqueIdentifier() {
    return "P";
  }

  @Override
  int hashCodeID() {
    return 3;
  }
}
