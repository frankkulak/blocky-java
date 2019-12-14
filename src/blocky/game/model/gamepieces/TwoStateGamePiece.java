package blocky.game.model.gamepieces;

import blocky.game.util.Position;

/**
 * TODO
 */
abstract class TwoStateGamePiece extends GamePiece {
  final GamePiece before, after;

  TwoStateGamePiece(String name, Position pos, GamePiece before, GamePiece after) {
    super(name, pos);
    this.before = before;
    this.after = after;
  }

  // todo
}
