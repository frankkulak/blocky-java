package blocky.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import blocky.model.BlockyModel;
import blocky.model.gamepieces.BlueBlock;
import blocky.model.gamepieces.CrackedBlock;
import blocky.model.gamepieces.Empty;
import blocky.model.gamepieces.GamePiece;
import blocky.model.gamepieces.PlayerBlock;
import blocky.model.gamepieces.PopBlock;
import blocky.model.gamepieces.RedBlock;
import blocky.model.gamepieces.SolidBlock;
import blocky.model.gamepieces.Wall;
import blocky.model.gamepieces.WinningPiece;
import blocky.model.gamepieces.YellowBlock;
import blocky.util.Direction;

/**
 * Acts as a text-based view of the Blocky game, intended for use with the console.
 */
public class TextBlockyView implements BlockyView {
  private final InputStream in; // INVARIANT: never null
  private final PrintStream out; // INVARIANT: never null
  private BlockyViewListener listener; // INVARIANT: never null once set

  /**
   * Constructs new TextBlockyView with given InputStream and PrintStream for IO.
   *
   * @param in  input to control view
   * @param out output to render view to
   * @throws IllegalArgumentException if given PrintStream is null
   */
  public TextBlockyView(InputStream in, PrintStream out) throws IllegalArgumentException {
    if (in == null || out == null)
      throw new IllegalArgumentException("Tried to create text view with null input or output.");

    this.in = in;
    this.out = out;
  }

  // -----------------------------------------------------------------------------------------------
  // BlockyView INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void setListener(BlockyViewListener listener) throws IllegalArgumentException {
    if (listener == null) throw new IllegalArgumentException("Tried to set view listener to null.");
    this.listener = listener;
  }

  @Override
  public void start(BlockyModel model) throws IllegalArgumentException {
    if (model == null) throw new IllegalArgumentException("Tried to start view with null model.");
    this.handleCommand("/help");
    this.render(model);
    this.playGame();
  }

  @Override
  public void levelLoaded(BlockyModel model) throws IllegalArgumentException {
    this.render(model); // might throw IAE
  }

  @Override
  public void moveMade(BlockyModel model) throws IllegalArgumentException {
    this.render(model); // might throw IAE
    this.listener.finishedRendering();
  }

  @Override
  public void display(String text) {
    this.out.print(text + "\n\n");
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Renders given model with this view.
   *
   * @param model model to render with this view
   * @throws IllegalArgumentException if given model is null
   * @throws IllegalStateException    if given model cannot be rendered with this view
   */
  private void render(BlockyModel model) throws IllegalArgumentException, IllegalStateException {
    if (model == null) throw new IllegalArgumentException("Tried to render null model.");

    List<List<GamePiece>> layout = model.layout();
    StringBuilder outputString = new StringBuilder();

    for (int i = 0; i < layout.size(); i++) {
      List<GamePiece> row = layout.get(i);
      for (GamePiece gp : row) outputString.append(toChar(gp));
      if (i < layout.size() - 1) outputString.append('\n');
    }

    this.display(outputString.toString());
  }

  /**
   * Runs the Blocky game, taking user input until the game is quit.
   */
  private void playGame() {
    Scanner scan = new Scanner(this.in);
    while (scan.hasNext()) {
      try {
        this.handleCommand(scan.next());
      } catch (IllegalStateException | IllegalArgumentException e) {
        this.display(e.getMessage());
      }
    }
  }

  /**
   * Determines and returns character to represent given game piece in this view.
   *
   * @param gp game piece to represent with char
   * @return char representing given game piece
   * @throws IllegalArgumentException if given game piece cannot be rendered
   */
  private static char toChar(GamePiece gp) throws IllegalArgumentException {
    GamePiece toRender = gp.renderAs();

    if (toRender instanceof Wall) {
      return '\u2588'; // █
    } else if (toRender instanceof Empty || toRender instanceof WinningPiece) {
      return ' '; // blank space
    } else if (toRender instanceof PlayerBlock) {
      return '\u25A0'; // ■
    } else if (toRender instanceof SolidBlock) {
      return '\u25A2'; // ▢
    } else if (toRender instanceof PopBlock) {
      return '\u25CE'; // ◎
    } else if (toRender instanceof CrackedBlock) {
      return '\u25EB'; // ◫
    } else if (toRender instanceof YellowBlock) {
      return '\u25CF'; // ●
    } else if (toRender instanceof RedBlock) {
      return '\u25C8'; // ◈
    } else if (toRender instanceof BlueBlock) {
      return '\u25A3'; // ▣
    } else {
      throw new IllegalArgumentException("Could not render given GamePiece.");
    }
  }

  /**
   * Handles the given string as a command and performs the corresponding action.
   *
   * @param str string acting as command
   * @throws IllegalArgumentException if given string is not a valid command
   */
  private void handleCommand(String str) throws IllegalArgumentException {
    if (str.equalsIgnoreCase("w")) {
      this.listener.move(Direction.UP);
    } else if (str.equalsIgnoreCase("a")) {
      this.listener.move(Direction.LEFT);
    } else if (str.equalsIgnoreCase("s")) {
      this.listener.move(Direction.DOWN);
    } else if (str.equalsIgnoreCase("d")) {
      this.listener.move(Direction.RIGHT);
    } else if (str.equalsIgnoreCase("/re")) {
      this.listener.restartLevel();
    } else if (str.equalsIgnoreCase("/next")) {
      this.listener.nextLevel();
    } else if (str.equalsIgnoreCase("/prev")) {
      this.listener.prevLevel();
    } else if (str.equalsIgnoreCase("/solve")) {
      this.listener.solveLevel();
    } else if (str.equalsIgnoreCase("/quit")) {
      this.listener.terminate("Game quit by player.");
    } else if (str.equalsIgnoreCase("/help")) {
      this.listener.help();
    } else if (str.equalsIgnoreCase("/print")) {
      this.listener.printLevel();
    } else if (str.startsWith("/goto:")) {
      this.listener.goToLevel(parseLevelToGoTo(str));
    } else {
      throw new IllegalArgumentException("Command \"" + str + "\" not recognized.");
    }
  }

  /**
   * Parses and returns level specified in given /goto: command.
   *
   * @param gotoCommand command of /goto: format
   * @return level specified in given command
   * @throws IllegalArgumentException if given command is malformed
   */
  private static int parseLevelToGoTo(String gotoCommand) throws IllegalArgumentException {
    String[] gotoArr = gotoCommand.split(":");
    if (gotoArr.length == 2 && gotoArr[0].equalsIgnoreCase("/goto")) {
      try {
        return Integer.parseInt(gotoArr[1]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Expected integer after /goto: command.");
      }
    } else {
      throw new IllegalArgumentException("Incorrect format used for /goto: command.");
    }
  }
}
