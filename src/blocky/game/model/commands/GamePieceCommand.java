package blocky.game.model.commands;

import blocky.game.model.gamepieces.GamePiece;

/**
 * Represents a command that affects a specific GamePiece.
 */
abstract class GamePieceCommand implements ModelCommand {
  final GamePiece gamePiece;

  /**
   * Creates new GamePieceCommand targeting given GamePiece.
   *
   * @param gp GamePiece to target
   */
  GamePieceCommand(GamePiece gp) {
    this.gamePiece = gp;
  }
}
