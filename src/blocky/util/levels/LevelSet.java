package blocky.util.levels;

import java.util.List;

/**
 * Represents a set of levels for the Blocky game which are in a particular order.
 */
public abstract class LevelSet {
  private int curLevelIndex;
  protected final List<Level> levels;

  /**
   * Constructs new LevelSet with given list of levels.
   *
   * @param levels list of levels to include in this set
   * @throws IllegalArgumentException if given list is null or empty
   */
  protected LevelSet(List<Level> levels) throws IllegalArgumentException {
    if (levels == null || levels.size() < 1) {
      throw new IllegalArgumentException("Level list must be non-null and have >= 1 level.");
    }

    this.curLevelIndex = 0;
    this.levels = levels;
  }

  /**
   * Returns the size of this LevelSet.
   *
   * @return size of this LevelSet
   */
  public int size() {
    return this.levels.size();
  }

  /**
   * Returns current level of this LevelSet.
   *
   * @return current level
   */
  public Level curLevel() {
    return this.levels.get(this.curLevelIndex);
  }

  /**
   * Advances to and returns next level in this set, if possible.
   *
   * @return next level if available
   * @throws IllegalStateException if on the last level / no next level
   */
  public Level nextLevel() throws IllegalStateException {
    try {
      // + 2 because goToLevel indexing starts at 1, and going to next level (i + 1 + 1 = i + 2)
      return this.goToLevel(this.curLevelIndex + 2);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Cannot advance because currently on last level.");
    }
  }

  /**
   * Goes back to and returns previous level in this set, if possible.
   *
   * @return previous level if available
   * @throws IllegalStateException if on the first level / no prev level
   */
  public Level prevLevel() throws IllegalStateException {
    try {
      // +/- 0 because goToLevel indexing starts at 1, and going to prev level (i + 1 - 1 = i)
      return this.goToLevel(this.curLevelIndex);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Cannot go back because currently on first level.");
    }
  }

  /**
   * Goes to and returns level at specified index. Note that indexing starts at 1 for ease for the
   * user. If no level exists at given index, IAE will be thrown.
   *
   * @param index index of level to go to
   * @return level at given index
   * @throws IllegalArgumentException if given index is not contained in this level set
   */
  public Level goToLevel(int index) throws IllegalArgumentException {
    int indexToUse = index - 1; // - 1 because given index starts at 1, but List starts at 0

    if (indexToUse >= this.levels.size() || indexToUse < 0) {
      throw new IllegalArgumentException("Level " + index + " does not exist in this set.");
    }

    this.curLevelIndex = indexToUse;
    return this.curLevel();
  }

  /**
   * Restarts this level set to be at the first level, then returns the first level.
   */
  public void restart() {
    this.goToLevel(1);
  }
}
