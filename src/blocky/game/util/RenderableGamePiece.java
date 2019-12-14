package blocky.game.util;

import java.awt.Color;

/**
 * TODO
 */
public enum RenderableGamePiece {
  PLAYER, WALL, EMPTY, WINNING_PIECE, SOLID, POP, CRACKED, RED, YELLOW, BLUE;

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * TODO
   *
   * @return todo
   */
  public char asChar() {
    switch (this) {
      case PLAYER:
        return '\u25A0'; // ■
      case WALL:
        return '\u2588'; // █
      case EMPTY:
      case WINNING_PIECE:
        return ' '; // blank space
      case POP:
        return '\u25CE'; // ◎
      case CRACKED:
        return '\u25EB'; // ◫
      case SOLID:
        return '\u25A2'; // ▢
      case RED:
        return '\u25C8'; // ◈
      case YELLOW:
        return '\u25CF'; // ●
      case BLUE:
        return '\u25A3'; // ▣
      default:
        // this will never be thrown (unless I add to this enum and forget to update this method)
        throw new IllegalStateException("Could not find char for this GamePiece.");
    }
  }

  /**
   * TODO
   *
   * @param dark todo
   * @return todo
   */
  public Color color(boolean dark) { // todo make private and create public drawToGraphics method
    switch (this) {
      case PLAYER:
      case WALL:
        return this.accent(dark);
      case EMPTY:
      case WINNING_PIECE:
        return this.background(dark);
      case POP:
      case CRACKED:
        return new Color(200, 200, 200);
      case SOLID:
        return new Color(110, 110, 110);
      case RED:
        return new Color(220, 55, 45);
      case YELLOW:
        return new Color(254, 196, 8);
      case BLUE:
        return new Color(42, 131, 210);
      default:
        // this will never be thrown (unless I add to this enum and forget to update this method)
        throw new IllegalStateException("Could not find color for this GamePiece.");
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Determines and returns the background color for a dark theme if given {@code true} or for a
   * light theme if given {@code false}.
   *
   * @param dark whether the background should be dark or not
   * @return background color of given type
   */
  private Color background(boolean dark) {
    return (dark ? new Color(50, 50, 50) : new Color(255, 255, 255));
  }

  /**
   * Determines and returns the accent color for a dark theme if given {@code true} or for a light
   * theme if given {@code false}.
   *
   * @param dark whether the accent is for a dark background or not
   * @return accent color for given type of background
   */
  private Color accent(boolean dark) {
    return this.background(!dark);
  }
}
