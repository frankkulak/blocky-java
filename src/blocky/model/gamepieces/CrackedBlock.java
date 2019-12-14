package blocky.model.gamepieces;

import blocky.model.commands.ChangedStatesCommand;
import blocky.model.commands.ModelCommand;
import blocky.util.Direction;
import blocky.util.Position;

/**
 * Represents a block that will stop the block that hits it once, and then become empty.
 */
public final class CrackedBlock extends OpenGamePiece {
  private boolean hasCracked; // whether or not this block has cracked yet

  /**
   * Constructs a new CrackedBlock with the given name and Position.
   *
   * @param name     name of this CrackedBlock
   * @param position position of this CrackedBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public CrackedBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
    this.hasCracked = false;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof CrackedBlock)) return false;
    CrackedBlock that = (CrackedBlock) other;
    return super.equals(other) && (this.hasCracked == that.hasCracked);
  }

  @Override
  public CrackedBlock copy() {
    CrackedBlock copy = new CrackedBlock(this.getName(), this.getPosition());
    copy.hasCracked = this.hasCracked;
    if (this.pieceInside != null) copy.pieceInside = (MovingGamePiece) this.pieceInside.copy();
    return copy;
  }

  @Override
  public boolean canBeEntered() {
    return this.hasCracked && this.pieceInside == null;
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) {
    if (this.hasCracked) return super.hitBy(gp, dir);
    this.hasCracked = true;
    // fixme just use new DeleteCommand(this); ?
    return new ChangedStatesCommand(this, new Empty(this.getName(), this.getPosition()));
  }

  @Override
  public GamePiece renderAs() {
    if (this.hasCracked && this.pieceInside == null) {
      return new Empty(this.getName(), this.getPosition());
    }

    return super.renderAs();
  }

  @Override
  public String uniqueIdentifier() {
    return "C" + (this.hasCracked ? "!" : "") + super.uniqueIdentifier();
  }

  @Override
  int hashCodeID() {
    return 6;
  }
}
