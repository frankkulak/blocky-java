package blocky.game.model.gamepieces;

import blocky.game.util.Position;

/**
 * Represents an empty space on the board.
 */
public final class Empty extends OpenGamePiece {
  /**
   * Constructs a new Empty game piece with the given name and Position.
   *
   * @param name     name of this Empty game piece
   * @param position position of this Empty game piece
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public Empty(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof Empty) && super.equals(other);
  }

  @Override
  public Empty copy() {
    Empty newPiece = new Empty(this.getName(), this.getPosition());
    if (this.pieceInside != null) newPiece.pieceInside = (MovingGamePiece) this.pieceInside.copy();
    return newPiece;
  }

  @Override
  public String uniqueIdentifier() {
    return "-" + super.uniqueIdentifier();
  }

  @Override
  int hashCodeID() {
    return 0;
  }
}
