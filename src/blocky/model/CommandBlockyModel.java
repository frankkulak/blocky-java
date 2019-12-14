package blocky.model;

import blocky.model.gamepieces.GamePiece;
import blocky.util.Direction;
import blocky.util.Position;

/**
 * Represents a model that is able to be manipulated by model commands in more specific ways. This
 * interface intended for use with commands only, not the controller.
 */
public interface CommandBlockyModel extends BlockyModel {
  /**
   * Moves the MovingGamePiece at the given position in the given direction. Will throw IAE if not
   * given the position of a MovingGamePiece.
   *
   * @param pos position of MovingGamePiece to move
   * @param dir direction to move piece in
   * @return whether or not move was successful
   * @throws IllegalArgumentException if piece at given position is not a MovingGamePiece
   * @throws IllegalStateException    if no level has been loaded into model yet
   */
  boolean movePieceAt(Position pos, Direction dir)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Deletes the GamePiece at the given Position (makes it into an Empty piece); will only delete
   * the piece inside of open game pieces that are occupied, but will delete any game pieces that
   * are solid or open but not filled. Returns {@code true} if the original piece was not already an
   * Empty, meaning the board has changed.
   *
   * @param pos position of piece to delete
   * @return whether or not board has actually changed
   * @throws IllegalArgumentException if given position is null or invalid
   * @throws IllegalStateException    if no level has been loaded into model yet
   */
  boolean deletePieceAt(Position pos) throws IllegalArgumentException, IllegalStateException;

  /**
   * Updates the sprite of the corresponding GamePiece to render as the other given GamePiece at the
   * current step with no change in position.
   *
   * @param sprite   GamePiece of desired sprite
   * @param renderAs GamePiece to render this sprite as
   */
  void updateSpriteRender(GamePiece sprite, GamePiece renderAs);

  /**
   * Notifies this model that the game has been won.
   *
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  void winGame() throws IllegalStateException;

  /**
   * Notifies this model that a fatal move has been made.
   *
   * @throws IllegalStateException if no level has been loaded into model yet
   */
  void fatalMoveMade() throws IllegalStateException;
}
