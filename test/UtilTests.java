import org.junit.Test;

import blocky.util.Direction;
import blocky.util.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

/**
 * A class for testing the Direction enum and Position class.
 */
public class UtilTests {

  // EXAMPLES --------------------------------------------------------------------------------------

  // direction examples
  private Direction left = Direction.LEFT;
  private Direction right = Direction.RIGHT;
  private Direction up = Direction.UP;
  private Direction down = Direction.DOWN;

  // position examples
  private Position zeroPos = new Position(0, 0);
  private Position samePos = new Position(5, 5);
  private Position difPos = new Position(4, 6);
  private Position negPos = new Position(-5, -5);
  private Position altPos = new Position(-5, 5);

  // DIRECTION TESTS -------------------------------------------------------------------------------

  @Test
  public void directionToStringWorks() {
    assertEquals("left", left.toString());
    assertEquals("right", right.toString());
    assertEquals("up", up.toString());
    assertEquals("down", down.toString());
  }

  @Test
  public void oppositeDirectionWorks() {
    assertEquals(right, left.opposite());
    assertEquals(left, right.opposite());
    assertEquals(down, up.opposite());
    assertEquals(up, down.opposite());
  }

  @Test
  public void rowChangeForDirectionWorks() {
    assertEquals(0, left.rowChange());
    assertEquals(0, right.rowChange());
    assertEquals(-1, up.rowChange());
    assertEquals(1, down.rowChange());
  }

  @Test
  public void colChangeForDirectionWorks() {
    assertEquals(-1, left.colChange());
    assertEquals(1, right.colChange());
    assertEquals(0, up.colChange());
    assertEquals(0, down.colChange());
  }

  // POSITION TESTS --------------------------------------------------------------------------------

  @Test
  public void posEqualsWorks() {
    // testing pos equals copy of self
    assertEquals(zeroPos, zeroPos.copy());
    assertEquals(samePos, samePos.copy());
    assertEquals(difPos, difPos.copy());

    // testing pos equals other pos with same position
    assertEquals(zeroPos, new Position(0, 0));
    assertEquals(samePos, new Position(5, 5));
    assertEquals(difPos, new Position(4, 6));

    // testing pos does not equal other non-pos / null
    assertNotEquals(zeroPos, Direction.LEFT);
    assertNotEquals(samePos, null);

    // testing pos does not equal pos with same vals but negative
    assertNotEquals(samePos, negPos);
    assertNotEquals(difPos, new Position(-4, -6));

    // testing pos does not equal pos with swapped vals
    assertNotEquals(difPos, new Position(6, 4));

    // testing pos does not equal pos with dif vals
    assertNotEquals(zeroPos, samePos);
    assertNotEquals(samePos, difPos);
    assertNotEquals(difPos, zeroPos);
  }

  @Test
  public void posHashCodeWorks() {
    // todo

    fail("Test not implemented.");
  }

  @Test
  public void posCopyWorks() {
    // assert all are equal, but not the same object

    // testing pos with neg vals
    assertEquals(negPos.copy(), negPos);
    assertNotSame(negPos.copy(), negPos);

    // testing pos with vals of 0
    assertEquals(zeroPos.copy(), zeroPos);
    assertNotSame(zeroPos.copy(), zeroPos);

    // testing pos with same vals
    assertEquals(samePos.copy(), samePos);
    assertNotSame(samePos.copy(), samePos);

    // testing pos with dif vals
    assertEquals(difPos.copy(), difPos);
    assertNotSame(difPos.copy(), difPos);

    // testing pos with one neg one pos
    assertEquals(altPos.copy(), altPos);
    assertNotSame(altPos.copy(), altPos);
  }

  @Test
  public void posToStringWorks() {
    // testing pos with neg vals
    assertEquals("(-5, -5)", negPos.toString());

    // testing pos with vals of 0
    assertEquals("(0, 0)", zeroPos.toString());

    // testing pos with same vals
    assertEquals("(5, 5)", samePos.toString());

    // testing pos with dif vals
    assertEquals("(4, 6)", difPos.toString());

    // testing pos with one neg one pos
    assertEquals("(-5, 5)", altPos.toString());
  }

  @Test
  public void posGetItemWorks() {
    // todo

    fail("Test not implemented.");
  }

  @Test
  public void posGetItemThrowsIAEForNullInput() {
    try { // position 0, 0
      zeroPos.getItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to get item from null list.")) {
        fail();
      }
    }

    try { // negative position
      negPos.getItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to get item from null list.")) {
        fail();
      }
    }

    try { // regular position
      difPos.getItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to get item from null list.")) {
        fail();
      }
    }
  }

  @Test
  public void posGetItemThrowsIOBEWhenNotInList() {
    // todo

    fail("Test not implemented.");
  }

  @Test
  public void posSetItemWorks() {
    // setting to some actual value
    // todo

    // setting to null
    // todo

    fail("Test not implemented.");
  }

  @Test
  public void posSetItemThrowsIAEForNullInput() {

    // while inserting null item

    try { // position 0, 0
      zeroPos.setItem(null, null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to set item in null list.")) {
        fail();
      }
    }

    try { // negative position
      negPos.setItem(null, null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to set item in null list.")) {
        fail();
      }
    }

    try { // regular position
      difPos.setItem(null, null);
      fail();
    } catch (IllegalArgumentException e) {
      if (!e.getMessage().equals("Tried to set item in null list.")) {
        fail();
      }
    }

    // while inserting actual item

    // todo

    fail("Test not finished.");
  }

  @Test
  public void posSetItemThrowsIOBEWhenNotInList() {
    // todo

    fail("Test not implemented.");
  }

}
