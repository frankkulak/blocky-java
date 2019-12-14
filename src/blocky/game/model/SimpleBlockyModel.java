package blocky.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blocky.game.model.gamepieces.Empty;
import blocky.game.model.gamepieces.GamePiece;
import blocky.game.model.gamepieces.MovingGamePiece;
import blocky.game.model.gamepieces.PlayerBlock;
import blocky.game.util.levels.Level;
import blocky.game.util.Direction;
import blocky.game.util.Position;

/**
 * Acts as a model for the Blocky game.
 */
public class SimpleBlockyModel implements CommandBlockyModel {
  private List<List<GamePiece>> layout; // INVARIANT: never null after initially set
  private Map<String, Sprite> sprites; // INVARIANT: never null after initially set
  private Position playerPosition; // INVARIANT: never off board, not null if has level
  private BlockyModelListener listener; // INVARIANT: never null after initially set
  private Level level; // INVARIANT: never null after initially set
  private int moves; // INVARIANT: never negative
  private int curStep; // INVARIANT: never negative

  // fixme something to prevent crashing when bouncing between two blues

  // -----------------------------------------------------------------------------------------------
  // BlockyModel INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void restartLevel() throws IllegalStateException {
    this.requireLevel();

    this.layout = this.level.getLayout();
    this.initializeSprites();
    this.playerPosition = this.level.getPlayerPosition();
    this.moves = 0;
    this.curStep = 0;
  }

  @Override
  public void loadLevel(Level level) throws IllegalArgumentException {
    if (level == null) throw new IllegalArgumentException("Tried to load null level into model.");
    this.level = level;
    this.restartLevel();
  }

  @Override
  public int levelIndex() throws IllegalStateException {
    this.requireLevel();
    return this.level.getID();
  }

  @Override
  public int levelWidth() throws IllegalStateException {
    int width = 0;
    for (List<GamePiece> row : this.layout) if (row.size() > width) width = row.size();
    return width;
  }

  @Override
  public int levelHeight() throws IllegalStateException {
    return this.layout.size();
  }

  @Override
  public boolean move(Direction dir) throws IllegalStateException {
    this.moves++; // moves++ must precede movePieceAt or else winning move won't count as a move
    boolean moveMade = this.movePieceAt(this.playerPosition, dir); // might throw ISE
    if (!moveMade) this.moves--; // do NOT replace with [if (moveMade) this.moves++;], see above
    return moveMade;
  }

  @Override
  public int movesMade() throws IllegalStateException {
    this.requireLevel();
    return this.moves;
  }

  @Override
  public boolean foundOptimalSolution() throws IllegalStateException {
    this.requireLevel();

    if (this.moves < this.level.getMoves()) { // this should never actually pass if algo is perfect
      throw new IllegalStateException("Found quicker solution than thought possible (" +
              this.moves + " instead of " + this.level.getMoves() + ").");
    }

    return this.moves == this.level.getMoves();
  }

  @Override
  public List<List<GamePiece>> layout() throws IllegalStateException {
    this.requireLevel();
    return this.copyLayout();
  }

  @Override
  public List<Sprite> sprites() throws IllegalStateException {
    List<Sprite> spriteList = new ArrayList<>();
    for (String name : this.sprites.keySet()) spriteList.add(this.sprites.get(name));
    return spriteList;
  }

  @Override
  public int curStep() throws IllegalStateException {
    return this.curStep;
  }

  @Override
  public void setListener(BlockyModelListener listener) throws IllegalArgumentException {
    if (listener == null) throw new IllegalArgumentException("Tried to set model listener null.");
    this.listener = listener;
  }

  @Override
  public BlockyModel copy() {
    SimpleBlockyModel model = new SimpleBlockyModel();
    model.layout = this.copyLayout();
    model.playerPosition = this.playerPosition.copy();
    model.level = this.level;
    model.listener = this.listener;
    model.curStep = this.curStep;
    model.initializeSprites();
    return model;
  }

  // -----------------------------------------------------------------------------------------------
  // CommandBlockyModel INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public boolean movePieceAt(Position pos, Direction dir)
          throws IllegalArgumentException, IllegalStateException {
    this.requireLevel();

    try {
      boolean movingPlayer = pos.equals(this.playerPosition);

      Position posOfMovingPiece = pos.copy();
      MovingGamePiece movingPiece = this.movingPieceAt(posOfMovingPiece);

      Position posOfNextPiece = nextPosition(posOfMovingPiece, dir);
      GamePiece nextPiece = posOfNextPiece.getItem(this.layout);

      boolean pieceMoved = false;
      while (nextPiece.canBeEntered()) {
        this.curStep++;

        movingPiece = this.extractMovingPieceAt(posOfMovingPiece);
        nextPiece.beEnteredBy(movingPiece).execute(this);

        posOfMovingPiece = posOfNextPiece.copy();
        posOfNextPiece = nextPosition(posOfMovingPiece, dir);

        nextPiece = posOfNextPiece.getItem(this.layout);

        pieceMoved = true;
      }

      if (movingPlayer) {
        this.playerPosition = posOfMovingPiece.copy();
      } else {
        this.markPlayerPosition();
      }

      if (pieceMoved) {
        Sprite sprite = this.sprites.get(movingPiece.getName());
        sprite.addKeyFrame(this.curStep, movingPiece, posOfMovingPiece.copy());
      }

      boolean otherPieceMoved = nextPiece.hitBy(movingPiece, dir).execute(this);

      return pieceMoved || otherPieceMoved;
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Tried to move a stationary block.");
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("Tried to move player off board.");
    }
  }

  @Override
  public boolean deletePieceAt(Position pos) throws IllegalArgumentException, IllegalStateException {
    this.requireLevel();

    try {
      GamePiece itemBefore = pos.getItem(this.layout);
      Empty replacement = new Empty(itemBefore.getName(), pos.copy());
      if (!itemBefore.deletePieceInside()) pos.setItem(this.layout, replacement);
      return !itemBefore.equals(replacement);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Tried to set value of GamePiece that doesn't exist.");
    }
  }

  @Override
  public void updateSpriteRender(GamePiece gp, GamePiece renderAs) {
    Sprite sprite = this.sprites.get(gp.getName());

    try {
      sprite.addKeyFrame(this.curStep, renderAs, gp.getPosition());
    } catch (IllegalArgumentException e) {
      this.curStep++;
      sprite.addKeyFrame(this.curStep, renderAs, gp.getPosition());
    }
  }

  @Override
  public void winGame() throws IllegalStateException {
    this.requireLevel();
    this.listener.levelBeat();
  }

  @Override
  public void fatalMoveMade() throws IllegalStateException {
    this.requireLevel();
    this.listener.fatalMoveMade();
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Marks the player's position in its sprite at the current step.
   */
  private void markPlayerPosition() {
    // if ClassCastException thrown, playerPosition has fallen out of sync with player block
    PlayerBlock player = (PlayerBlock) this.movingPieceAt(this.playerPosition);
    this.updateSpriteRender(player, player);
  }

  /**
   * Initializes this.sprites with all new Sprite objects.
   */
  private void initializeSprites() {
    this.sprites = new HashMap<>();

    for (List<GamePiece> row : this.layout) {
      for (GamePiece gp : row) {
        GamePiece mgp = gp.renderAs();
        if (!gp.equals(mgp)) this.sprites.put(mgp.getName(), new Sprite(mgp, mgp.getPosition()));
        this.sprites.put(gp.getName(), new Sprite(gp, gp.getPosition()));
      }
    }
  }

  /**
   * Returns copy of board layout.
   *
   * @return copy of board layout
   */
  private List<List<GamePiece>> copyLayout() {
    List<List<GamePiece>> layout = new ArrayList<>();

    for (List<GamePiece> row : this.layout) {
      List<GamePiece> newRow = new ArrayList<>();
      for (GamePiece gp : row) newRow.add(gp.copy());
      layout.add(newRow);
    }

    return layout;
  }

  /**
   * Throws ISE if no level is currently loaded into this model.
   *
   * @throws IllegalStateException if no level is loaded into model
   */
  private void requireLevel() throws IllegalStateException {
    if (this.level == null) throw new IllegalStateException("Tried to play game with no level.");
  }

  /**
   * Finds the next position that the current moving piece will go to.
   *
   * @param pos current position that moving piece is in
   * @param dir direction that the moving piece is going
   * @return next position that the moving piece will go to
   */
  private static Position nextPosition(Position pos, Direction dir) {
    return new Position(pos.row + dir.rowChange(), pos.col + dir.colChange());
  }

  /**
   * Finds and returns MovingGamePiece at given position, but does not extract it.
   *
   * @param pos position at which to find MovingGamePiece
   * @return MovingGamePiece at given position
   * @throws ClassCastException if there is no MovingGamePiece at given position
   */
  private MovingGamePiece movingPieceAt(Position pos) throws ClassCastException {
    return (MovingGamePiece) pos.getItem(this.layout).renderAs();
  }

  /**
   * Finds, extracts, and returns MovingGamePiece at given position.
   *
   * @param pos position at which to find MovingGamePiece
   * @return MovingGamePiece at given position
   * @throws ClassCastException if there is no MovingGamePiece at given position
   */
  private MovingGamePiece extractMovingPieceAt(Position pos) throws ClassCastException {
    try {
      GamePiece pieceAtPosition = pos.getItem(this.layout);
      MovingGamePiece movingGamePiece = pieceAtPosition.extractMovingPiece();
      pos.setItem(this.layout, pieceAtPosition.replacementAfterExtraction());
      return movingGamePiece;
    } catch (UnsupportedOperationException | IllegalStateException e) {
      throw new ClassCastException("Tried to access moving piece where there was not one.");
    }
  }
}
