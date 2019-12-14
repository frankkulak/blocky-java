package blocky.model.gamepieces;

import blocky.model.commands.ModelCommand;
import blocky.util.Direction;
import blocky.util.Position;

/**
 * Represents a piece that, when entered by the player, indicates that the game has been won.
 */
public final class WinningPiece extends StationaryGamePiece {
  /**
   * Constructs a new WinningPiece with the given name and Position.
   *
   * @param name     name of this WinningPiece
   * @param position position of this WinningPiece
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public WinningPiece(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof WinningPiece) && super.equals(other);
  }

  @Override
  public WinningPiece copy() {
    return new WinningPiece(this.getName(), this.getPosition());
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    return gp.hitWinningPiece();
  }

  @Override
  public String uniqueIdentifier() {
    return "W";
  }

  @Override
  int hashCodeID() {
    return 2;
  }
}
