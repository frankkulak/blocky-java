package blocky.game.view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import blocky.game.model.BlockyModel;
import blocky.game.util.Direction;
import util.ButtonPanel;
import util.ButtonPanelListener;

/**
 * Acts as a visual view for the Blocky game.
 */
public class VisualBlockyView extends JFrame
        implements BlockyView, KeyListener, ActionListener, ButtonPanelListener {

  private BlockyViewListener listener;

  private final JLabel levelInfo;
  private final JLabel moveInfo;
  private final GameBoardPanel gameBoardPanel;
  private final JLabel userMessages;

  private final Timer timer;
  private int lastRenderedTick;
  private int lastModelTick;

  private final int ticksPerStep;

  /**
   * Constructs a new VisualBlockyView object with the given specifications.
   *
   * @param dark whether or not the theme should be dark
   * @param size size in pixels to allot for the game board
   * @param gq   quality of graphics to use
   */
  public VisualBlockyView(boolean dark, int size, GraphicsQuality gq) {
    if (size < 50) throw new IllegalArgumentException("Size of game board must be at least 50 pixels.");

    final int textAreaHeight = 30;
    final int totalHeight = textAreaHeight * 2 + size;
    this.ticksPerStep = gq.ticksPerStep;

    // this JFrame
    this.setTitle("Blocky");
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.addKeyListener(this);

    // creating all components
    JPanel playGamePanel = new JPanel();
    JPanel topPanel = new JPanel();
    this.levelInfo = new JLabel("[LEVEL]", SwingConstants.CENTER);
    this.moveInfo = new JLabel("[MOVES]", SwingConstants.CENTER);
    this.gameBoardPanel = new GameBoardPanel(dark, size, size, gq.ticksPerStep);
    JPanel bottomPanel = new JPanel();
    this.userMessages = new JLabel("Game started.", SwingConstants.CENTER);

    // setting play game panel
    setFixedSize(playGamePanel, size,totalHeight);
    playGamePanel.setLayout(new BoxLayout(playGamePanel, BoxLayout.Y_AXIS));
    playGamePanel.add(topPanel, BorderLayout.NORTH);
    playGamePanel.add(gameBoardPanel, BorderLayout.CENTER);
    playGamePanel.add(bottomPanel, BorderLayout.SOUTH);

    // setting top panel
    setFixedSize(topPanel, size, textAreaHeight);
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.add(levelInfo);
    topPanel.add(moveInfo);

    // setting bottom panel
    setFixedSize(bottomPanel, size, textAreaHeight);
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    Color buttonColor = new Color(40, 130, 210);
    ButtonPanel restartButton = new ButtonPanel("Restart Level", (int) (size * 0.4), (int) (textAreaHeight * 0.9), buttonColor);
    restartButton.setListener(this);
    bottomPanel.add(restartButton);
    bottomPanel.add(this.userMessages); // fixme uncomment this, comment next

    // setting text areas
    setFixedSize(this.levelInfo, size / 2, textAreaHeight);
    setFixedSize(this.moveInfo, size / 2, textAreaHeight);
    setFixedSize(this.userMessages, (int) (size * 0.6), textAreaHeight); // fixme size?

    // setting game board panel
    setFixedSize(this.gameBoardPanel, size, size);
    this.gameBoardPanel.addKeyListener(this);

    // adding playGamePanel to this
    this.add(playGamePanel);

    // packing this JFrame
    this.setResizable(false);
    this.pack();

    // initializing timer
    this.timer = new Timer(gq.msPerTick, this);
    this.lastRenderedTick = 0;
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
    this.levelLoaded(model);
    this.setVisible(true);
  }

  @Override
  public void levelLoaded(BlockyModel model) throws IllegalArgumentException {
    this.lastRenderedTick = 0;
    this.lastModelTick = model.curStep() * this.ticksPerStep;

    this.levelInfo.setText("Level: " + model.levelIndex());
    this.moveInfo.setText("Moves: 0");

    this.gameBoardPanel.updateSprites(model.sprites());
    this.gameBoardPanel.setNumberOfTiles(model.levelWidth(), model.levelHeight());
    this.gameBoardPanel.setTick(this.lastRenderedTick);
    this.gameBoardPanel.repaint();
  }

  @Override
  public void moveMade(BlockyModel model) throws IllegalArgumentException {
    this.lastModelTick = model.curStep() * this.ticksPerStep;

    this.moveInfo.setText("Moves: " + model.movesMade());
    this.gameBoardPanel.setTick(this.lastRenderedTick);
    this.gameBoardPanel.repaint();

    this.timer.start();
  }

  @Override
  public void display(String text) throws IllegalStateException {
    this.userMessages.setText(text);
  }

  // -----------------------------------------------------------------------------------------------
  // KeyListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void keyTyped(KeyEvent e) {
    // intentionally left blank
  }

  @Override
  public void keyPressed(KeyEvent e) {
    try {
      this.handleCommand(e.getKeyCode());
    } catch (IllegalStateException | IllegalArgumentException ex) {
      this.display(ex.getMessage());
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // intentionally left blank
  }

  // -----------------------------------------------------------------------------------------------
  // ActionListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.lastRenderedTick >= this.lastModelTick) {
      this.timer.stop();
      this.listener.finishedRendering();
      return;
    }

    this.gameBoardPanel.setTick(this.lastRenderedTick + 1);
    this.gameBoardPanel.repaint();
    this.lastRenderedTick++;
  }

  // -----------------------------------------------------------------------------------------------
  // ButtonPanelListener INTERFACE METHODS
  // -----------------------------------------------------------------------------------------------

  @Override
  public void buttonPressed(String buttonText) {
    switch (buttonText) {
      case "Restart Level":
        this.listener.restartLevel();
        break;
      case "Level Set":
        // todo
        break;
      default:
        throw new RuntimeException("Button \"" + buttonText + "\" not found.");
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Decides what to do to the controller depending on which key was entered.
   *
   * @param keyCode code of key that was pressed
   * @throws IllegalArgumentException if given key code is invalid
   */
  private void handleCommand(int keyCode) throws IllegalArgumentException {
    if (keyCode == 65 || keyCode == 37) { // 'a' or left arrow
      this.listener.move(Direction.LEFT);
    } else if (keyCode == 87 || keyCode == 38) { // 'w' or up arrow
      this.listener.move(Direction.UP);
    } else if (keyCode == 68 || keyCode == 39) { // 'd' or right arrow
      this.listener.move(Direction.RIGHT);
    } else if (keyCode == 83 || keyCode == 40) { // 's' or down arrow
      this.listener.move(Direction.DOWN);
    } else if (keyCode == 82) { // 'r'
      this.listener.restartLevel();
    } else if (keyCode == 78) { // 'n'
      this.listener.nextLevel();
    } else if (keyCode == 80) { // 'p'
      this.listener.prevLevel();
    } else if (keyCode == 76) { // 'l'
      this.listener.solveLevel();
    } else if (keyCode == 27) { // esc
      this.listener.terminate("Game quit by player.");
    } else if (keyCode == 10) { // enter/return
      this.listener.printLevel();
    } else {
      throw new IllegalArgumentException("Unrecognized key (code: " + keyCode + ").");
    }
  }

  /**
   * Sets the given component to have the given width and height as a fixed size.
   *
   * @param comp   component to set a fixed size for
   * @param width  width to set for given component
   * @param height height to set for given component
   */
  private static void setFixedSize(Component comp, int width, int height) {
    comp.setMinimumSize(new Dimension(width, height));
    comp.setPreferredSize(new Dimension(width, height));
    comp.setMaximumSize(new Dimension(width, height));
  }
}
