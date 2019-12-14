package blocky.game.model.gamepieces;

import blocky.game.model.commands.ChangedStatesCommand;
import blocky.game.model.commands.ModelCommand;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Represents a block that can be gone over once and then becomes a SolidBlock.
 */
public final class PopBlock extends OpenGamePiece {
  private boolean hasPopped; // whether or not this piece has popped yet

  /**
   * Constructs a new PopBlock with the given name and Position.
   *
   * @param name     name of this PopBlock
   * @param position position of this PopBlock
   * @throws IllegalArgumentException if given position is null or invalid
   */
  public PopBlock(String name, Position position) throws IllegalArgumentException {
    super(name, position);
    this.hasPopped = false;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof PopBlock)) return false;
    PopBlock that = (PopBlock) other;
    return super.equals(other) && (this.hasPopped == that.hasPopped);
  }

  @Override
  public PopBlock copy() {
    PopBlock newPiece = new PopBlock(this.getName(), this.getPosition());
    newPiece.hasPopped = this.hasPopped;
    if (this.pieceInside != null) newPiece.pieceInside = (MovingGamePiece) this.pieceInside.copy();
    return newPiece;
  }

  @Override
  public boolean canBeEntered() {
    // this.pieceInside == null is redundant because there will never be a piece inside before
    // this PopBlock has popped, but it's just there for safety/sanity
    return !this.hasPopped && this.pieceInside == null;
  }

  @Override
  public ModelCommand beEnteredBy(MovingGamePiece gp) throws IllegalStateException {
    super.beEnteredBy(gp);
    this.hasPopped = true;
    return new ChangedStatesCommand(this, this.asSolidBlock());
  }

  @Override
  public ModelCommand hitBy(MovingGamePiece gp, Direction dir) throws IllegalStateException {
    try {
      return super.hitBy(gp, dir);
    } catch (IllegalStateException e) {
      return this.asSolidBlock().hitBy(gp, dir);
    }
  }

  @Override
  public GamePiece renderAs() {
    return (this.hasPopped && this.pieceInside == null ? this.asSolidBlock() : super.renderAs());
  }

  @Override
  public String uniqueIdentifier() {
    return "O" + (this.hasPopped ? "!" : "") + super.uniqueIdentifier();
  }

  @Override
  int hashCodeID() {
    return 5;
  }

  /**
   * Returns this PopBlock as a SolidBlock; should only be called once popped.
   *
   * @return this PopBlock as a SolidBlock
   */
  private GamePiece asSolidBlock() {
    return new SolidBlock(this.getName(), this.getPosition());
  }
}
