package blocky.game.model.gamepieces;

import blocky.game.model.commands.ModelCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a GamePiece that can be entered by MovingGamePieces.
 */
abstract class OpenGamePiece extends GamePiece {
  MovingGamePiece pieceInside; // might be null

  /**
   * Constructs a new OpenGamePiece with the given name and Position.
   *
   * @param name     name of this OpenGamePiece
   * @param position position of this OpenGamePiece
   * @throws IllegalArgumentException if given position is null or invalid
   */
  OpenGamePiece(String name, Position position) throws IllegalArgumentException {
    super(name, position);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof OpenGamePiece)) return false;

    OpenGamePiece that = (OpenGamePiece) other;

    try {
      return super.equals(other) && this.pieceInside.equals(that.pieceInside);
    } catch (NullPointerException e) {
      return super.equals(other) && that.pieceInside == null;
    }
  }

  @Override
  public int hashCode() {
    int pieceInsideID = (this.pieceInside == null ? 0 : this.pieceInside.hashCodeID());
    // second digit is reserved for piece inside (times 1,000,000)
    return super.hashCode() + (pieceInsideID * 1000000);
  }

  @Override
  public boolean canBeEntered() {
    return this.pieceInside == null;
  }

  @Override
  public ModelCommand beEnteredBy(MovingGamePiece gp) throws IllegalStateException {
    if (!this.canBeEntered()) throw new IllegalStateException("Tried to enter occupied piece.");
    gp.setPosition(this.getPosition());
    this.pieceInside = gp;
    return super.beEnteredBy(gp);
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) throws IllegalStateException {
    if (this.pieceInside != null) return this.pieceInside.hitBy(gp, dir);
    throw new IllegalStateException("Tried to hit a block that should have been entered.");
  }

  @Override
  public MovingGamePiece extractMovingPiece() throws IllegalStateException {
    if (this.pieceInside == null) throw new IllegalStateException("No moving piece to extract.");
    MovingGamePiece mgp = this.pieceInside;
    this.pieceInside = null;
    return mgp;
  }

  @Override
  public GamePiece replacementAfterExtraction() {
    return this;
  }

  @Override
  public GamePiece renderAs() {
    return (this.pieceInside == null ? this : this.pieceInside);
  }

  @Override
  public String uniqueIdentifier() {
    return (this.pieceInside == null ? "" : "+" + this.pieceInside.uniqueIdentifier());
  }

  @Override
  public boolean deletePieceInside() {
    if (this.pieceInside == null) return false;
    this.pieceInside = null;
    return true;
  }
}
