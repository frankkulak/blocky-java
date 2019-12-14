package blocky.game.model;

import java.util.List;

import blocky.game.model.gamepieces.GamePiece;
import blocky.game.util.Direction;
import blocky.game.util.Position;
import blocky.game.util.levels.Level;

/**
 * TODO
 */
public class SpriteBlockyModel implements CommandBlockyModel {
  private List<List<Sprite>> sprites;

  // -----------------------------------------------------------------------------------------------
  // CommandBlockyModel INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public boolean movePieceAt(Position pos, Direction dir) throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public boolean deletePieceAt(Position pos) throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public void updateSpriteRender(GamePiece sprite, GamePiece renderAs) {

  }

  @Override
  public void winGame() throws IllegalStateException {

  }

  @Override
  public void fatalMoveMade() throws IllegalStateException {

  }

  // -----------------------------------------------------------------------------------------------
  // BlockyModel INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void restartLevel() throws IllegalStateException {

  }

  @Override
  public void loadLevel(Level level) throws IllegalArgumentException {

  }

  @Override
  public int levelIndex() throws IllegalStateException {
    return 0;
  }

  @Override
  public int levelWidth() throws IllegalStateException {
    return 0;
  }

  @Override
  public int levelHeight() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean move(Direction dir) throws IllegalStateException {
    return false;
  }

  @Override
  public int movesMade() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean foundOptimalSolution() throws IllegalStateException {
    return false;
  }

  @Override
  public List<List<GamePiece>> layout() throws IllegalStateException {
    return null;
  }

  @Override
  public List<Sprite> sprites() throws IllegalStateException {
    return null;
  }

  @Override
  public int curStep() throws IllegalStateException {
    return 0;
  }

  @Override
  public void setListener(BlockyModelListener listener) throws IllegalArgumentException {

  }

  @Override
  public BlockyModel copy() {
    return null;
  }
}
