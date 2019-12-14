package blocky.model.gamepieces;

import blocky.model.commands.DoNothingCommand;
import blocky.model.commands.ModelCommand;
import blocky.util.Direction;
import blocky.util.Position;

/**
 * Represents a game piece in the Blocky game.
 */
public abstract class GamePiece {
  private final String name; // INVARIANT: never null or empty
  private Position position; // INVARIANT: never null or negative

  /**
   * Constructs a new GamePiece with the given name and position.
   *
   * @param name     name of this GamePiece
   * @param position position of this GamePiece
   * @throws IllegalArgumentException if position is null or invalid
   */
  GamePiece(String name, Position position) throws IllegalArgumentException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("GamePiece name must be >= 1 character.");
    }

    this.name = name;
    this.setPosition(position); // throws IAE if null or neg
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof GamePiece)) return false;
    GamePiece that = (GamePiece) other;
    return this.name.equals(that.name) && this.position.equals(that.position);
  }

  @Override
  public int hashCode() {
    // all subclasses will add a unique one char int times 10,000,000 for the piece itself
    // all open pieces add an additional one char int times 1,000,000 for their piece inside
    // the hashCode of the position is only going to occupy the final 6 digits
    // the name of the game piece is not incorporated into the hashCode
    return this.position.hashCode() + (this.hashCodeID() * 10000000);
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns name of this GamePiece.
   *
   * @return name of this GamePiece.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns a copy of the current position of this GamePiece.
   *
   * @return copy of current position
   */
  public Position getPosition() {
    return this.position.copy();
  }

  /**
   * Sets position of this GamePiece to given Position, if valid.
   *
   * @param position new position of this GamePiece
   * @throws IllegalArgumentException if given Position is null or invalid
   */
  public void setPosition(Position position) throws IllegalArgumentException {
    if (position == null || position.row < 0 || position.col < 0) {
      throw new IllegalArgumentException("Tried to set position of GamePiece to either null.");
    }

    this.position = position.copy();
  }

  /**
   * Inserts the given MovingGamePiece into this one, if possible.
   *
   * @param gp MovingGamePiece entering this one
   * @return command to be executed upon this piece being entered
   * @throws IllegalStateException         if this piece has already been entered
   * @throws UnsupportedOperationException if this piece cannot be entered
   */
  public ModelCommand beEnteredBy(MovingGamePiece gp)
          throws IllegalStateException, UnsupportedOperationException {
    return new DoNothingCommand();
  }

  /**
   * Notifies this piece that it is going to be deleted, returns what model should do.
   *
   * @return ModelCommand to perform when this piece is deleted
   */
  public ModelCommand willDelete() {
    return new DoNothingCommand();
  }

  /**
   * Deletes the piece inside of this one, if possible, and returns {@code true} if something was
   * deleted and {@code false} otherwise.
   *
   * @return whether or not anything was deleted
   */
  public boolean deletePieceInside() {
    return false;
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC ABSTRACT METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Constructs and returns a copy of this GamePiece. This method exists so as to reduce mutation of
   * GamePieces, especially to reduce the effects of altering the game board when restarting.
   *
   * @return copy of this GamePiece
   */
  public abstract GamePiece copy();

  /**
   * Determines and returns whether or not this GamePiece can be entered.
   *
   * @return whether or not this GamePiece can be entered
   */
  public abstract boolean canBeEntered();

  /**
   * Notifies this GamePiece that it has been hit by the given MovingGamePiece from the given
   * direction so that it can determine what the game's response should be via a ModelCommand.
   *
   * @param gp  MovingGamePiece that has hit this one
   * @param dir direction from which MovingGamePiece hit this one
   * @return ModelCommand detailing what should be done for this collision
   * @throws IllegalStateException if called on a piece that should be entered
   */
  public abstract ModelCommand hitBy(MovingGamePiece gp, Direction dir)
          throws IllegalStateException;

  /**
   * Extracts a MovingGamePiece from this one if possible. If this GamePiece is a MovingGamePiece,
   * it returns itself. If it's an OpenGamePiece, it returns the MovingGamePiece inside of it if
   * there is one. If any other type of GamePiece, then throws UOE.
   *
   * @return MovingGamePiece associated with this one
   * @throws IllegalStateException         if there is currently no MovingGamePiece to extract
   * @throws UnsupportedOperationException if called on a GamePiece that does not support
   *                                       extraction
   */
  public abstract MovingGamePiece extractMovingPiece()
          throws IllegalStateException, UnsupportedOperationException;

  /**
   * Determines and returns a GamePiece to replace this one with after its MovingGamePiece has been
   * extracted. If called on a GamePiece that does not support extraction, throws UOE.
   *
   * @return GamePiece to replace this one with after extraction
   * @throws UnsupportedOperationException if called on a GamePiece that does not support
   *                                       extraction
   */
  public abstract GamePiece replacementAfterExtraction() throws UnsupportedOperationException;

  /**
   * Determines and returns what this GamePiece should be rendered as.
   *
   * @return type of GamePiece to render for this one.
   */
  public abstract GamePiece renderAs();

  /**
   * Returns a string that identifies this GamePiece in detail for use in HashMap of layout.
   *
   * @return string that uniquely identifies this GamePiece
   */
  public abstract String uniqueIdentifier();

  // -----------------------------------------------------------------------------------------------
  // PRIVATE ABSTRACT METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns a unique int that can be used as this GamePiece's identifier in the hash code.
   *
   * @return integer for use in hash code
   */
  abstract int hashCodeID();
}
