package blocky.controller;

import java.util.List;

import blocky.model.BlockyModel;
import blocky.util.Direction;
import blocky.util.levels.LevelSet;
import blocky.util.levels.LevelSolver;
import blocky.view.BlockyView;

/**
 * Acts as a controller for the Blocky game.
 */
public class SimpleBlockyController implements BlockyController {
  private final BlockyModel model; // INVARIANT: never null
  private final BlockyView view; // INVARIANT: never null
  private final LevelSet levels; // INVARIANT: never null, has at least one level
  private boolean levelBeat, fatalMoveMade, allowMove;

  /**
   * Constructs a new SimpleBlockyController with given model, view (listener), and level set.
   *
   * @param model  model to use in this controller
   * @param view   view to act as listener for this controller
   * @param levels level set to use in this controller
   * @throws IllegalArgumentException if model, view, or level set is null
   */
  public SimpleBlockyController(BlockyModel model, BlockyView view, LevelSet levels)
          throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Tried to initialize controller with null model or view.");
    } else if (levels == null) {
      throw new IllegalArgumentException("Tried to initialize controller with no levels.");
    }

    this.model = model;
    model.setListener(this);

    this.view = view;
    view.setListener(this);

    this.levels = levels;
    model.loadLevel(levels.curLevel());

    this.levelBeat = false;
    this.fatalMoveMade = false;
    this.allowMove = true;
  }

  // -----------------------------------------------------------------------------------------------
  // BlockyController INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void launch() {
    this.view.start(this.model);
  }

  // -----------------------------------------------------------------------------------------------
  // BlockyModelListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void levelBeat() {
    this.levelBeat = true;
  }

  @Override
  public void fatalMoveMade() {
    this.fatalMoveMade = true;
  }

  // -----------------------------------------------------------------------------------------------
  // BlockyViewListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void finishedRendering() {
    if (this.levelBeat) this.levelBeatProcedure();
    if (this.fatalMoveMade) this.fatalMoveMadeProcedure();

    this.levelBeat = false;
    this.fatalMoveMade = false;

    this.allowMove = true;
  }

  @Override
  public void move(Direction dir) throws IllegalArgumentException, IllegalStateException {
    if (!this.allowMove) throw new IllegalStateException("Cannot move while rendering.");

    this.allowMove = false;
    if (!this.model.move(dir)) {
      this.allowMove = true;
      throw new IllegalStateException("Could not move " + dir.toString() + ".");
    }

    this.updateViewMove(); // view will let this know when to allow a move again
  }

  @Override
  public void nextLevel() throws IllegalStateException {
    this.model.loadLevel(this.levels.nextLevel());
    this.updateViewLevel();
  }

  @Override
  public void prevLevel() throws IllegalStateException {
    this.model.loadLevel(this.levels.prevLevel());
    this.updateViewLevel();
  }

  @Override
  public void goToLevel(int level) throws IllegalArgumentException {
    try {
      this.levels.goToLevel(level);
      this.model.loadLevel(this.levels.curLevel());
      this.updateViewLevel();
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Level " + level + " does not exist in current level set.");
    }
  }

  @Override
  public void restartLevel() {
    this.restartCurrentLevel();
    this.updateViewLevel();
    this.view.display("Level restarted.");
  }

  @Override
  public void solveLevel() {
    List<Direction> solution = this.levels.curLevel().getSolution();
    String solutionString = LevelSolver.solutionToString(solution);
    this.view.display(solutionString);
  }

  @Override
  public void printLevel() {
    System.out.println(this.levels.curLevel().toString());
    this.view.display("Level printed.");
  }

  @Override
  public void help() {
    String helpInfo = "How to play:\n" +
            " - w = move up\n" +
            " - a = move left\n" +
            " - s = move down\n" +
            " - d = move right\n" +
            " - /re = restart current level\n" +
            " - /next = go to next level\n" +
            " - /prev = go back to previous level\n" +
            " - /goto: = go to specified level (after :)\n" +
            " - /help = displays control information\n" +
            " - /solve = displays solution for this level\n" +
            " - /print = prints current level in format for txt file\n" +
            " - /quit = quit and terminate the game";

    this.view.display(helpInfo);
  }

  @Override
  public void terminate(String message) {
    this.view.display(message);
    System.exit(0);
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Restarts the current level and allows a move to be made.
   */
  private void restartCurrentLevel() {
    this.model.restartLevel();
    this.allowMove = true;
  }

  /**
   * Carries out what needs to be done when the level has been beat.
   */
  private void levelBeatProcedure() {
    // displaying if beat with max moves
    if (this.model.foundOptimalSolution()) {
      this.view.display("Good job!");
      try {
        // loading next level
        this.model.loadLevel(this.levels.nextLevel());
      } catch (IllegalStateException e) {
        // fixme change how this moves to next level
        this.view.display("Congrats! You've completed this level set!");
        this.levels.restart();
        this.model.loadLevel(this.levels.curLevel());
      }
    } else {
      this.view.display("This level has a quicker solution. Try again!");
      this.model.restartLevel();
    }

    this.view.levelLoaded(this.model);
  }

  /**
   * Carries out what needs to be done when a fatal move has been made.
   */
  private void fatalMoveMadeProcedure() {
    this.restartCurrentLevel();
    this.view.display("Try again!");

    this.view.levelLoaded(this.model);
  }

  /**
   * Lets the view know that a new level has been loaded and needs to be rendered.
   */
  private void updateViewLevel() {
    this.view.levelLoaded(this.model);
  }

  /**
   * Lets the view know that a move has been made and needs to be rendered.
   */
  private void updateViewMove() {
    this.view.moveMade(this.model);
  }
}
