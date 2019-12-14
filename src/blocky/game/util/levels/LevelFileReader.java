package blocky.game.util.levels;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import blocky.game.Blocky;
import blocky.game.model.gamepieces.BlueBlock;
import blocky.game.model.gamepieces.CrackedBlock;
import blocky.game.model.gamepieces.Empty;
import blocky.game.model.gamepieces.GamePiece;
import blocky.game.model.gamepieces.PlayerBlock;
import blocky.game.model.gamepieces.PopBlock;
import blocky.game.model.gamepieces.RedBlock;
import blocky.game.model.gamepieces.SolidBlock;
import blocky.game.model.gamepieces.Wall;
import blocky.game.model.gamepieces.WinningPiece;
import blocky.game.model.gamepieces.YellowBlock;
import blocky.game.util.Position;

/**
 * Class for parsing text files that contain data representing levels of Blocky.
 */
public class LevelFileReader {

  // -----------------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Parses file with given name to generate a LevelSet for use in game.
   *
   * @param fileName name of file to parse
   * @return LevelSet created from input file
   * @throws IllegalArgumentException if file could not be found
   * @throws IllegalStateException    if there is an error in the format of the file
   */
  public static LevelSet parseLevelFile(String fileName)
          throws IllegalArgumentException, IllegalStateException {
    InputStream file;
    file = Blocky.class.getResourceAsStream(fileName);
    if (file == null) throw new IllegalArgumentException("File " + fileName + " not found.");
    return parseLevelFile(file); // might throw ISE
  }

  /**
   * Parses the given string as a level.
   *
   * @param levelFormat string formatted as level
   * @return level parsed from given string
   * @throws IllegalArgumentException if given string is malformatted
   */
  public static Level parseLevel(String levelFormat) throws IllegalArgumentException {
    Scanner scan = new Scanner(levelFormat);
    if (!scan.next().equals("-level"))
      throw new IllegalArgumentException("Level input must begin with '-level'.");
    return parseLevel(scan).setID(1).build();
  }

  // -----------------------------------------------------------------------------------------------
  // PRIVATE METHODS
  // -----------------------------------------------------------------------------------------------

  /**
   * Parses file to generate LevelSet for use in game.
   *
   * @param file file to read from
   * @return LevelSet generated from file
   */
  private static LevelSet parseLevelFile(InputStream file) {
    Objects.requireNonNull(file, "Must have non-null file source.");
    Scanner scan = new Scanner(file);
    scan.useDelimiter(Pattern.compile("(\\p{Space}+|#.*)+"));

    StaticLevelSet.Builder levelSetBuilder = new StaticLevelSet.Builder();

    while (scan.hasNext()) {
      String next = scan.next();
      if (next.equals("-level")) {
        levelSetBuilder.addLevel(parseLevel(scan));
      } else {
        throw new IllegalStateException("Unexpected token \"" + next + "\" found in file.");
      }
    }

    return levelSetBuilder.build();
  }

  /**
   * Parses level from given scanner. Might throw various exceptions if input is invalid.
   *
   * @param scan scanner from which to parse level
   * @return level parsed from scanner
   */
  private static Level.Builder parseLevel(Scanner scan) {
    Level.Builder levelBuilder = new Level.Builder();

    // map to keep track of what number to suffix on name of game pieces
    Map<Character, Integer> nameMap = new HashMap<>();

    // creating layout of level
    Position curPos = new Position(0, 0);
    loop:
    while (scan.hasNext()) {
      String next = scan.next();
      switch (next) {
        case "-player": // might throw NFE if two ints do not follow
          requireHasNext(scan);
          int row = Integer.parseInt(scan.next());
          requireHasNext(scan);
          int col = Integer.parseInt(scan.next());
          levelBuilder.setPlayerPosition(new Position(row, col));
          break;
        case "-/level":
          break loop;
        default: // might throw if GamePiece cannot be parsed
          levelBuilder.addRow();
          curPos.col = 0;
          char[] chars = next.toCharArray();
          for (char c : chars) {
            levelBuilder.addPieceToRow(parseGamePiece(c, curPos, nameMap));
            curPos.col++;
          }
          curPos.row++;
          break;
      }
    }

    return levelBuilder;
  }

  /**
   * Parses given char and Position as GamePiece, returns that GamePiece.
   *
   * @param c       char of game piece to return
   * @param pos     position of game piece
   * @param nameMap map to keep track of what int to suffix on names
   * @return game piece corresponding to given char
   * @throws IllegalStateException if given char is not a valid game piece
   */
  private static GamePiece parseGamePiece(char c, Position pos, Map<Character, Integer> nameMap)
          throws IllegalStateException {
    // X = Wall, - = Empty, W = Winning block
    // P = PlayerBlock, S = SolidBlock, C = CrackedBlock, O = PopBlock
    // R = RedBlock, Y = YellowBlock, B = BlueBlock
    // todo - Include: N = OrangeBlock, U = PurpleBlock, G = GreenBlock, g = StickyBlock
    // todo - The above blocks are intended to be added to the game. See README.txt for details.

    // fixme Walls, Empties, and WinningPieces might not need names
    if (!nameMap.containsKey(c)) nameMap.put(c, 0);
    nameMap.replace(c, nameMap.get(c) + 1);
    String name = "" + c + nameMap.get(c);

    String emptyName = "";
    if (c == 'P' || c == 'Y') {
      if (!nameMap.containsKey('-')) nameMap.put('-', 0);
      nameMap.replace('-', nameMap.get('-') + 1);
      emptyName = "-" + nameMap.get('-');
    }

    switch (c) {
      case 'X':
        return new Wall(name, pos.copy());
      case '-':
        return new Empty(name, pos.copy());
      case 'W':
        return new WinningPiece(name, pos.copy());
      case 'S':
        return new SolidBlock(name, pos.copy());
      case 'P':
        Empty player = new Empty(emptyName, pos.copy());
        player.beEnteredBy(new PlayerBlock(name, pos.copy()));
        return player;
      case 'C':
        return new CrackedBlock(name, pos.copy());
      case 'O':
        return new PopBlock(name, pos.copy());
      case 'R':
        return new RedBlock(name, pos.copy());
      case 'Y':
        Empty yellow = new Empty(emptyName, pos.copy());
        yellow.beEnteredBy(new YellowBlock(name, pos.copy()));
        return yellow;
      case 'B':
        return new BlueBlock(name, pos.copy());
      default:
        throw new IllegalStateException("Char '" + c + "' cannot be parsed as GamePiece.");
    }
  }

  /**
   * Throws IAE if given scanner does not have a next token. Created for brevity in above code.
   *
   * @param scan scanner to check for next token
   * @throws IllegalStateException if no next token found
   */
  private static void requireHasNext(Scanner scan) throws IllegalStateException {
    if (!scan.hasNext()) throw new IllegalStateException("Expected token, but did not find one.");
  }
}
