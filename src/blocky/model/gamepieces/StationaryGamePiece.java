package blocky.model.gamepieces;

import blocky.util.Position;

/**
 * Represents a solid game piece that cannot move.
 */
abstract class StationaryGamePiece extends SolidGamePiece {
  /**
   * Constructs a new StationaryGamePiece with the given name and Position.
   *
   * @param name     name of this StationaryGamePiece
   * @param position position of this StationaryGamePiece
   * @throws IllegalArgumentException if given position is null or invalid
   */
  StationaryGamePiece(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public MovingGamePiece extractMovingPiece() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Tried to perform an extraction operation on a " +
            "StationaryGamePiece, which does not support doing so.");
  }

  @Override
  public GamePiece replacementAfterExtraction() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Tried to perform an extraction operation on a " +
            "StationaryGamePiece, which does not support doing so.");
  }
}
