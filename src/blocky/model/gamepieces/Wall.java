package blocky.model.gamepieces;

import blocky.util.Position;

/**
 * Represents a wall surrounding the game board.
 */
public final class Wall extends StationaryGamePiece {
  /**
   * Constructs a new Wall with the given name and Position.
   *
   * @param name     name of this Wall
   * @param position position of this Wall
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public Wall(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof Wall) && super.equals(other);
  }

  @Override
  public Wall copy() {
    return new Wall(this.getName(), this.getPosition());
  }

  @Override
  public String uniqueIdentifier() {
    return "X";
  }

  @Override
  int hashCodeID() {
    return 1;
  }
}
