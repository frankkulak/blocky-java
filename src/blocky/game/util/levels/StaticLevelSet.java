package blocky.game.util.levels;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a fixed set of levels which cannot be edited once created. All levels in a
 * StaticLevelSet are determined at construction.
 */
public class StaticLevelSet extends LevelSet {
  /**
   * Constructs new StaticLevelSet with given list of levels.
   *
   * @param levels list of levels to include in this set
   * @throws IllegalArgumentException if given set is null or empty
   */
  private StaticLevelSet(List<Level> levels) throws IllegalArgumentException {
    super(levels);
  }

  /**
   * Class for building StaticLevelSet objects.
   */
  public static class Builder {
    private final List<Level> levels;

    public Builder() {
      // const will throw IAE if not added to
      this.levels = new ArrayList<>();
    }

    public Builder addLevel(Level.Builder level) throws IllegalStateException {
      // setting id of level to length of current list + 1 since indexing begins at 1
      Level builtLevel = level.setID(this.levels.size() + 1).build();
      builtLevel.getSolution(); // to ensure there is a solution, may throw ISE
      this.levels.add(builtLevel);
      return this;
    }

    public StaticLevelSet build() throws IllegalStateException {
      try {
        return new StaticLevelSet(this.levels);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("Could not build StaticLevelSet as specified.");
      }
    }
  }
}
