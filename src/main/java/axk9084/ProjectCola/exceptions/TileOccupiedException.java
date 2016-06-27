package axk9084.ProjectCola.exceptions;

/**
 * TileOccupiedException is thrown when a character is attempting to move to an occupied tile.
 *
 * Created by antkwan on 6/25/2016.
 */
public class TileOccupiedException extends Exception {

  private String characterName;
  private int row;
  private char column;

  public TileOccupiedException( String characterName, char column, int row ) {
    this.characterName = characterName;
    this.row = row;
    this.column = column;
  }

  public String getCharacterName() {
    return characterName;
  }

  public int getRow() {
    return row;
  }

  public char getColumn() {
    return column;
  }
}
