package axk9084.ProjectCola.exceptions;

/**
 * OutOfAttackRangeException is thrown when a character attempts to attack another tile out of it's attack range.
 *
 * Created by antkwan on 6/27/2016.
 */
public class OutOfAttackRangeException extends Exception {

  private String characterName;
  private char columnKey;
  private int rowKey;
  private int minAttackRange;
  private int maxAttackRange;

  public OutOfAttackRangeException( String characterName, char columnKey, int rowKey, int minAttackRange, int maxAttackRange ) {
    this.characterName = characterName;
    this.columnKey = columnKey;
    this.rowKey = rowKey;
    this.minAttackRange = minAttackRange;
    this.maxAttackRange = maxAttackRange;
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

  public int getMinAttackRange() {
    return minAttackRange;
  }

  public int getMaxAttackRange() {
    return maxAttackRange;
  }
}
