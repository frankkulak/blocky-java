package blocky.view;

/**
 * Represents a quality to render the game at it in a visual view (how smooth the transitions are).
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
}
