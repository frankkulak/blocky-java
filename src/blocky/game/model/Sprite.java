package blocky.game.model;

import java.util.Comparator;
import java.util.TreeSet;

import blocky.game.model.gamepieces.GamePiece;
import blocky.game.util.Position;

/**
 * Represents an animated game piece to be rendered in a view. Sprites are able to be constructed
 * and edited within the model package, but are read only in all others.
 */
public class Sprite {
  private final TreeSet<KeyFrame> keyFrames;

  /**
   * Constructs a new Sprite object using the given GamePiece and Position as the state of the piece
   * being represented by this Sprite at time 0.
   *
   * @param originalGP  original state of the piece of this sprite
   * @param originalPos original position of the piece of this sprite
   * @throws IllegalArgumentException if either argument is null
   */
  Sprite(GamePiece originalGP, Position originalPos) throws IllegalArgumentException {
    this.keyFrames = new TreeSet<>(new KeyFrameComparator());
    this.addKeyFrame(0, originalGP, originalPos); // will throw IAE if null
  }

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and returns the type of GamePiece to render at the given tick using the given ratio of
   * ticks per step.
   *
   * @param tick         tick to find GamePiece at
   * @param ticksPerStep ratio of ticks per step
   * @return GamePiece at given tick
   * @throws IllegalArgumentException if given tick is negative or if ticksPerStep is < 1
   */
  public GamePiece pieceToRenderAt(int tick, int ticksPerStep) throws IllegalArgumentException {
    if (tick < 0) throw new IllegalArgumentException("Tried to find piece at negative tick.");
    if (ticksPerStep < 1) throw new IllegalArgumentException("There must be >= 1 tick per step.");

    int stepToFind = (int) Math.floor(tick / (double) ticksPerStep);
    KeyFrame before = this.keyFrames.floor(new KeyFrame(stepToFind));
    // below line just here to silence a warning (will never actually pass/throw because there is
    // always a key frame at tick 0, so floor will always return at least that)
    if (before == null) throw new IllegalStateException("This should not have been thrown.");
    return before.renderAs.copy();
  }

  /**
   * Finds and returns the x coordinate at the given tick using the given ratio of ticks per step
   * and ratio of pixels per step.
   *
   * @param tick          tick to find x coordinate at
   * @param ticksPerStep  ratio of ticks per step
   * @param pixelsPerStep ratio of pixels per step
   * @return x coordinate at given tick
   * @throws IllegalArgumentException if given tick is negative or ticksPerStep/pixelsPerStep is <1
   */
  public double xCoordAt(int tick, int ticksPerStep, int pixelsPerStep)
          throws IllegalArgumentException {
    return this.coordAt(tick, ticksPerStep, pixelsPerStep, true);
  }

  /**
   * Finds and returns the y coordinate at the given tick using the given ratio of ticks per step
   * and ratio of pixels per step.
   *
   * @param tick          tick to find y coordinate at
   * @param ticksPerStep  ratio of ticks per step
   * @param pixelsPerStep ratio of pixels per step
   * @return y coordinate at given tick
   * @throws IllegalArgumentException if given tick is negative or ticksPerStep/pixelsPerStep is <1
   */
  public double yCoordAt(int tick, int ticksPerStep, int pixelsPerStep)
          throws IllegalArgumentException {
    return this.coordAt(tick, ticksPerStep, pixelsPerStep, false);
  }

  // -----------------------------------------------------------------------------------------------
  // PACKAGE PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Adds a key frame to this sprite at the given step with the given properties.
   *
   * @param step step to add key frame at
   * @param gp   piece to render at and after this key frame
   * @param pos  position at this key frame
   * @throws IllegalArgumentException if key frame already exists at given step, if step is
   *                                  negative, or if either argument is null
   */
  void addKeyFrame(int step, GamePiece gp, Position pos) throws IllegalArgumentException {
    if (this.keyFrames.contains(new KeyFrame(step))) {
      throw new IllegalArgumentException("KeyFrame already exists at step " + step + ".");
    } else {
      this.keyFrames.add(new KeyFrame(step, gp, pos)); // will throw IAE if null
    }
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Finds and returns the given coordinate at the given tick using the given ratio of ticks per
   * step and ratio of pixels per step.
   *
   * @param tick          tick to find coordinate at
   * @param ticksPerStep  ratio of ticks per step
   * @param pixelsPerStep ratio of pixels per step
   * @param findingX      whether or not looking for x coordinate
   * @return coordinate at given tick
   * @throws IllegalArgumentException if given tick is negative or ticksPerStep/pixelsPerStep is <1
   */
  private double coordAt(int tick, int ticksPerStep, int pixelsPerStep, boolean findingX)
          throws IllegalArgumentException {
    if (tick < 0) throw new IllegalArgumentException("Tried to find coord at negative tick.");
    if (ticksPerStep < 1) throw new IllegalArgumentException("There must be >= 1 tick per step.");

    double approxStep = tick / (double) ticksPerStep;
    KeyFrame before = this.keyFrames.floor(new KeyFrame((int) Math.floor(approxStep)));
    KeyFrame after = this.keyFrames.ceiling(new KeyFrame((int) Math.ceil(approxStep)));

    // below line just here to silence an error (will never actually pass/throw because there is
    // always a key frame at tick 0, so floor will always return at least that)
    if (before == null) throw new IllegalStateException("This should not have been thrown.");

    if (before.equals(after) || after == null) {
      int valAtStep = (findingX ? before.position.col : before.position.row);
      return valAtStep * pixelsPerStep;
    } else {
      int v1 = (findingX ? before.position.col : before.position.row);
      int v2 = (findingX ? after.position.col : after.position.row);
      return pixelsAtTick(tick, ticksPerStep, pixelsPerStep, before.step, v1, after.step, v2);
    }
  }

  /**
   * Calculates and returns the number of pixels to use at the desired tick.
   *
   * @param tick          tick to find pixels at
   * @param ticksPerStep  ratio of ticks per step
   * @param pixelsPerStep ratio of pixels per step
   * @param step1         step of first key frame
   * @param val1          value at first key frame
   * @param step2         step of second key frame
   * @param val2          value at second key frame
   * @return pixels to use at the given tick
   */
  private static int pixelsAtTick(int tick, int ticksPerStep, int pixelsPerStep,
                                  int step1, int val1, int step2, int val2) {
    int firstTick = step1 * ticksPerStep;
    int secondTick = step2 * ticksPerStep;
    double percent = (tick - firstTick) / (double) (secondTick - firstTick);

    int pix1 = val1 * pixelsPerStep;
    int pix2 = val2 * pixelsPerStep;

    int difference = (int) (percent * (pix2 - pix1));

    return difference + pix1;
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE CLASSES
  // -----------------------------------------------------------------------------------------------

  /**
   * Acts as a Comparator of KeyFrame objects for use with the TreeSet of KeyFrames.
   */
  private class KeyFrameComparator implements Comparator<KeyFrame> {
    @Override
    public int compare(KeyFrame kf1, KeyFrame kf2) {
      return kf1.compareTo(kf2);
    }
  }

  /**
   * Represents the state of a Sprite at a certain step. Every time a MovingGamePiece advances by
   * one block, a step is added (EX: at the start of the game, the step is 0, but if a PlayerBlock
   * moves 4 blocks to the right, the step is now 4, if the player then moves down by 3 blocks, the
   * step is now 7, and so on).
   */
  private class KeyFrame implements Comparable {
    private final int step;
    private final GamePiece renderAs;
    private final Position position;

    /**
     * Constructs a new KeyFrame object at the given step, assigning the internal GamePiece and
     * Position to null. Intended for use with searching TreeSet only; never use this constructor
     * when adding to the TreeSet.
     *
     * @param step step of this key frame
     * @throws IllegalArgumentException if step is negative
     */
    KeyFrame(int step) throws IllegalArgumentException {
      if (step < 0) throw new IllegalArgumentException("Tried to make KeyFrame at negative step.");

      this.step = step;
      this.renderAs = null;
      this.position = null;
    }

    /**
     * Constructs a new KeyFrame object at the given step with the given GamePiece and Position.
     *
     * @param step     step of this key frame
     * @param renderAs game piece to render at and after this key frame
     * @param pos      position of piece at this key frame
     * @throws IllegalArgumentException if any arguments are null, or if step is negative
     */
    KeyFrame(int step, GamePiece renderAs, Position pos) throws IllegalArgumentException {
      if (step < 0) {
        throw new IllegalArgumentException("Tried to make KeyFrame at negative step.");
      } else if (renderAs == null || pos == null) {
        throw new IllegalArgumentException("Tried to make KeyFrame with null parameter(s).");
      }

      this.step = step;
      this.renderAs = renderAs;
      this.position = pos;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof KeyFrame)) return false;
      KeyFrame that = (KeyFrame) other;
      return this.step == that.step;
    }

    @Override
    public int hashCode() {
      return this.step;
    }

    @Override
    public int compareTo(Object other) {
      if (!(other instanceof KeyFrame)) {
        throw new IllegalArgumentException("Tried to compare a KeyFrame to a non-KeyFrame.");
      }

      KeyFrame that = (KeyFrame) other;
      return this.step - that.step;
    }
  }
}
