package blocky.util;

/**
 * Represents the quality (smoothness of transitions) to use when rendering the game.
 */
public enum GraphicsQuality {
  LOW(4, 16), MED(8, 8), HIGH(16, 4), BEST(32, 2);

  public final int ticksPerStep, msPerTick;

  /**
   * Constructs a new GraphicsQuality enum object with the given ratios of ticks per step and
   * milliseconds per tick.
   *
   * @param ticksPerStep the ratio of ticks per second to use
   * @param msPerTick    the ratio of milliseconds per tick to use
   */
  GraphicsQuality(int ticksPerStep, int msPerTick) {
    this.ticksPerStep = ticksPerStep;
    this.msPerTick = msPerTick;
  }

  /**
   * Parses given String as GraphicsQuality, if possible.
   *
   * @param str String from which to parse GraphicsQuality
   * @return GraphicsQuality parsed from String
   * @throws IllegalArgumentException if given String could not be parsed as GraphicsQuality
   */
  public static GraphicsQuality parseString(String str) throws IllegalArgumentException {
    switch (str) {
      case "low":
        return LOW;
      case "med":
        return MED;
      case "high":
        return HIGH;
      case "best":
        return BEST;
      default:
        throw new IllegalArgumentException("Could not parse \"" + str + "\" as GraphicsQuality");
    }
  }
}
