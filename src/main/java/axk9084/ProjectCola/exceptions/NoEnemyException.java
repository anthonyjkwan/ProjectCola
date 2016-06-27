package axk9084.ProjectCola.exceptions;

/**
 * NoEnemyException is thrown when an attacking character attempts to attack a tile that has no occupying enemy.
 *
 * Created by antkwan on 6/27/2016.
 */
public class NoEnemyException extends Exception {

  private char columnKey;
  private int rowKey;

  public NoEnemyException( char columnKey, int rowKey ) {
    this.columnKey = columnKey;
    this.rowKey = rowKey;
  }

  public char getColumnKey() {
    return columnKey;
  }

  public int getRowKey() {
    return rowKey;
  }
}
