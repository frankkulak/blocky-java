package blocky.util;

import java.util.List;

/**
 * Represents a position in a 2D array. Exists with get() and set() methods so as to reduce
 * confusion about row/col placement and therefore (hopefully) reduce bugs.
 */
public class Position {
  public int row, col;

  /**
   * Constructs a new Position object using given row and col values.
   *
   * @param row row coordinate of position
   * @param col col coordinate of position
   */
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Position)) return false;

    Position that = (Position) other;
    return (this.row == that.row) && (this.col == that.col);
  }

  @Override
  public int hashCode() {
    // supports unique identification of up to row and col #999
    return (this.row * 1000) + this.col;
  }

  /**
   * Returns new Position object with same row and col coordinates as this one.
   *
   * @return new Position object with current row and col values
   */
  public Position copy() {
    return new Position(this.row, this.col);
  }

  @Override
  public String toString() {
    return "(row: " + this.row + ", col: " + this.col + ")";
  }

  /**
   * Indexes given list using this position, returns result.
   *
   * @param list list to index
   * @param <T>  content of list to be returned
   * @return item at given position in list
   * @throws IllegalArgumentException  if given list is null
   * @throws IndexOutOfBoundsException if trying to access position that doesn't exist
   */
  public <T> T getItem(List<List<T>> list)
          throws IllegalArgumentException, IndexOutOfBoundsException {
    if (list == null) throw new IllegalArgumentException("Tried to get item from null list.");

    return list.get(row).get(col);
  }

  /**
   * Sets item in given list using this position.
   *
   * @param list list to index
   * @param item item to insert
   * @param <T>  type of list
   * @throws IllegalArgumentException  if given list is null
   * @throws IndexOutOfBoundsException if trying to access position that doesn't exist
   */
  public <T> void setItem(List<List<T>> list, T item)
          throws IllegalArgumentException, IndexOutOfBoundsException {
    if (list == null) throw new IllegalArgumentException("Tried to set item in null list.");

    list.get(row).set(col, item);
  }
}
