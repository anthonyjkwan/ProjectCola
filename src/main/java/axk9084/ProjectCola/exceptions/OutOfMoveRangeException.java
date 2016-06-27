package axk9084.ProjectCola.exceptions;

/**
 * OutOfMoveRangeException is thrown when a character attempts to move outside of it's move range.
 *
 * Created by antkwan on 6/26/2016.
 */
public class OutOfMoveRangeException extends Exception {

  private String characterName;
  private char columnKey;
  private int rowKey;
  private int moveRange;

  public OutOfMoveRangeException( String characterName, char columnKey, int rowKey, int moveRange ) {
    this.characterName = characterName;
    this.columnKey = columnKey;
    this.rowKey = rowKey;
    this.moveRange = moveRange;
  }

  public String getCharacterName() {
    return characterName;
  }

  public char getColumnKey() {
    return columnKey;
  }

  public int getRowKey() {
    return rowKey;
  }

  public int getMoveRange() {
    return moveRange;
  }
}
