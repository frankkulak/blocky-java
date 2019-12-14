package blocky.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JPanel;

import blocky.model.Sprite;
import blocky.model.gamepieces.*;

/**
 * Acts as a panel to render the game board in the visual view.
 */
class GameBoardPanel extends JPanel {
  private final boolean dark;
  private final int ticksPerStep;
  private final int width;
  private final int height;
  private int horzTiles;
  private int vertTiles;
  private List<Sprite> sprites;
  private int tick;

  /**
   * Constructs a new GameBoardPanel object using given parameters to specify how it looks.
   *
   * @param dark         whether or not to use dark background
   * @param width        width of panel in pixels
   * @param height       height of panel in pixels
   * @param ticksPerStep ratio of ticks per step
   */
  GameBoardPanel(boolean dark, int width, int height, int ticksPerStep) {
    super();

    this.dark = dark;
    this.setBackground(this.background());

    this.width = width;
    this.height = height;

    this.ticksPerStep = ticksPerStep;
  }

  /**
   * Sets the number of tiles to render.
   *
   * @param horzTiles number of horizontal tiles to render
   * @param vertTiles number of vertical tiles to render
   * @throws IllegalArgumentException if either argument is < 1
   */
  void setNumberOfTiles(int horzTiles, int vertTiles) throws IllegalArgumentException {
    if (horzTiles < 1 || vertTiles < 1) {
      throw new IllegalArgumentException("Must have at least one tile in each dimension.");
    }

    this.horzTiles = horzTiles;
    this.vertTiles = vertTiles;
  }

  /**
   * Updates the sprites to render.
   *
   * @param sprites sprites to render
   */
  void updateSprites(List<Sprite> sprites) {
    this.sprites = sprites;
  }

  /**
   * Sets the tick to render sprites at.
   *
   * @param tick tick to render sprites at
   */
  void setTick(int tick) {
    this.tick = tick;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform ogTransform = g2d.getTransform();

    int tileWidth = this.tileWidth();
    int tileHeight = this.tileHeight();
    int strokeSize = (int) (tileWidth * 0.05);
    if (strokeSize < 1) strokeSize = 1;
    g2d.setStroke(new BasicStroke(strokeSize));

    this.drawWalls(g2d);

    for (Sprite sprite : this.sprites) {
      int x = (int) sprite.xCoordAt(this.tick, this.ticksPerStep, tileWidth);
      int y = (int) sprite.yCoordAt(this.tick, this.ticksPerStep, tileHeight);
      GamePiece piece = sprite.pieceToRenderAt(this.tick, this.ticksPerStep);
      this.editGraphic2D(g2d, piece, x, y, tileWidth, tileHeight);
    }

    g2d.setTransform(ogTransform);
  }

  /**
   * Calculates and returns the width of each tile in pixels.
   *
   * @return width of each tile in pixels
   */
  private int tileWidth() {
    return this.width / this.horzTiles;
  }

  /**
   * Calculates and returns the height of each tile in pixels.
   *
   * @return height of each tile in pixels
   */
  private int tileHeight() {
    return this.height / this.vertTiles;
  }

  /**
   * Renders a square of walls around the game board.
   *
   * @param g2d graphics object to draw walls to
   */
  private void drawWalls(Graphics2D g2d) {
    double wallThickness = 0.2; // percentage of tile that is wall

    int tileWidth = this.tileWidth();
    int tileHeight = this.tileHeight();

    int vertWallWidth = (int) (tileWidth * wallThickness);
    int horzWallHeight = (int) (tileHeight * wallThickness);
    int vertWallHeight = (tileHeight * this.vertTiles) - (2 * tileHeight) + (2 * horzWallHeight);
    int horzWallWidth = (tileWidth * this.horzTiles) - (2 * tileWidth) + (2 * vertWallWidth);

    int westX = tileWidth - vertWallWidth;
    int eastX = tileWidth * (this.horzTiles - 1);
    int northY = tileHeight - horzWallHeight;
    int southY = tileHeight * (this.vertTiles - 1);

    g2d.setColor(this.accent());

    g2d.fillRect(westX, northY, vertWallWidth, vertWallHeight); // west
    g2d.fillRect(westX, northY, horzWallWidth, horzWallHeight); // north
    g2d.fillRect(westX, southY, horzWallWidth, horzWallHeight); // south
    g2d.fillRect(eastX, northY, vertWallWidth, vertWallHeight); // east
  }

  /**
   * Adds a shape using the given parameter to the given Graphics2D object.
   *
   * @param g2d graphics object to edit
   * @param gp  game piece to render
   * @param x   x coordinate to render at
   * @param y   y coordinate to render at
   * @param w   width of rendered piece
   * @param h   height of rendered piece
   */
  private void editGraphic2D(Graphics2D g2d, GamePiece gp, int x, int y, int w, int h) {
    if ((gp instanceof Empty) || (gp instanceof Wall)) return;

    // these pieces are the only ones that do not use a rounded rect
    if (gp instanceof WinningPiece) {
      g2d.setColor(this.background());
      g2d.fillRect(x, y, w, h);
      return;
    } else if (gp instanceof PopBlock) {
      renderPopBlock(g2d, x, y, w, h);
      return;
    }

    // all of these pieces use rounded rects
    int arcSize = (int) (w * 0.2);
    if (gp instanceof CrackedBlock) {
      renderCrackedBlock(g2d, x, y, w, h, arcSize);
      return;
    } else if (gp instanceof PlayerBlock) {
      g2d.setColor(this.accent());
    } else if (gp instanceof SolidBlock) {
      g2d.setColor(new Color(110, 110, 110));
    } else if (gp instanceof RedBlock) {
      g2d.setColor(new Color(220, 55, 45));
    } else if (gp instanceof YellowBlock) {
      g2d.setColor(new Color(255, 195, 10));
    } else if (gp instanceof BlueBlock) {
      g2d.setColor(new Color(40, 130, 210));
    }

    g2d.fillRoundRect(x, y, w, h, arcSize, arcSize);
  }

  /**
   * Renders a PopBlock to the given graphics object using the given parameters.
   *
   * @param g2d graphics object to render PopBlock to
   * @param x   x coordinate of tile to render on
   * @param y   y coordinate of tile to render on
   * @param w   width of tile to render on
   * @param h   height of tile to render on
   */
  private void renderPopBlock(Graphics2D g2d, int x, int y, int w, int h) {
    g2d.setColor(new Color(200, 200, 200));

    double ratio = 0.75;

    // outer oval
    int ovalWidth = (int) (w * ratio);
    int ovalHeight = (int) (h * ratio);
    int xOffSet = (w - ovalWidth) / 2;
    int yOffSet = (h - ovalWidth) / 2;
    g2d.drawOval(x + xOffSet, y + yOffSet, ovalWidth, ovalHeight);

    // inner oval
    ovalWidth = (int) (ovalWidth * ratio);
    ovalHeight = (int) (ovalHeight * ratio);
    xOffSet = (w - ovalWidth) / 2;
    yOffSet = (h - ovalWidth) / 2;
    g2d.drawOval(x + xOffSet, y + yOffSet, ovalWidth, ovalHeight);
  }

  /**
   * Renders a CrackedBlock to the given graphics object using the given parameters.
   *
   * @param g2d graphics object to render CrackedBlock to
   * @param x   x coordinate of tile to render on
   * @param y   y coordinate of tile to render on
   * @param w   width of tile to render on
   * @param h   height of tile to render on
   * @param arc size to use for arc
   */
  private void renderCrackedBlock(Graphics2D g2d, int x, int y, int w, int h, int arc) {
    // block
    g2d.setColor(new Color(200, 200, 200));
    g2d.fillRoundRect(x, y, w, h, arc, arc);

    // crack
    g2d.setColor(this.background());
    int crackSize = (int) (w * 0.3);
    int sx = x + (w / 2) + (crackSize / 2);
    int ex = x + (w / 2) - (crackSize / 2);
    int sy = y;
    int ey = y + (h / 3);

    int cracks = 3;
    for (int i = 0; i < cracks - 1; i++) {
      g2d.drawLine(sx, sy, ex, ey);
      g2d.drawLine(ex, ey, sx, ey);
      sy = ey;
      ey += h / 3;
    }

    g2d.drawLine(sx, sy, ex, y + h);
  }

  /**
   * Returns the color of the background, depending on whether or not the theme is dark.
   *
   * @return color of the background
   */
  private Color background() {
    return (this.dark ? new Color(50, 50, 50) : new Color(255, 255, 255));
  }

  /**
   * Returns the accent color, depending on whether or not the theme is dark.
   *
   * @return accent color
   */
  private Color accent() {
    return (!this.dark ? new Color(50, 50, 50) : new Color(255, 255, 255));
  }
}
