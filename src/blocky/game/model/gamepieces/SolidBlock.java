package blocky.game.model.gamepieces;

import blocky.game.util.Position;

/**
 * Represents a block that will stop any block that hits it.
 */
public final class SolidBlock extends StationaryGamePiece {
  /**
   * Constructs a new SolidBlock with the given name and Position.
   *
   * @param name     name of this SolidBlock
   * @param position position of this SolidBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public SolidBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof SolidBlock) && super.equals(other);
  }

  @Override
  public SolidBlock copy() {
    return new SolidBlock(this.getName(), this.getPosition());
  }

  @Override
  public String uniqueIdentifier() {
    return "S";
  }

  @Override
  int hashCodeID() {
    return 4;
  }
}
