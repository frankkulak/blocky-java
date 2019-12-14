package blocky.game.util;

/**
 * Represents the direction that a GamePiece moves in.
 */
public enum Direction {
  LEFT(0, -1), RIGHT(0, 1), UP(-1, 0), DOWN(1, 0);

  private final int rowChange, colChange;

  /**
   * TODO
   *
   * @param rowChange todo
   * @param colChange todo
   */
  Direction(int rowChange, int colChange) {
    this.rowChange = rowChange;
    this.colChange = colChange;
  }

  /**
   * Returns an array of all possible direction values.
   *
   * @return array of all directions
   */
  public static Direction[] allDirections() {
    return new Direction[]{LEFT, UP, RIGHT, DOWN};
  }

  /**
   * Returns this Direction as a String.
   *
   * @return String representing this Direction
   */
  public String toString() {
    switch (this) {
      case LEFT:
        return "left";
      case RIGHT:
        return "right";
      case UP:
        return "up";
      case DOWN:
        return "down";
      default:
        // this will never actually be thrown
        throw new IllegalArgumentException("Tried to convert null Direction to String.");
    }
  }

  /**
   * Determines and returns the opposite of this Direction.
   *
   * @return opposite of this Direction
   */
  public Direction opposite() {
    switch (this) {
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      case UP:
        return DOWN;
      case DOWN:
        return UP;
      default:
        // this will never actually be thrown
        throw new IllegalArgumentException("Tried to find opposite of null Direction.");
    }
  }

  /**
   * Determines and returns the change in row required for moving in this Direction.
   *
   * @return change in row
   */
  public int rowChange() {
    return this.rowChange;
  }

  /**
   * Determines and returns the change in column required for moving in this Direction.
   *
   * @return change in col
   */
  public int colChange() {
    return this.colChange;
  }
}
