package axk9084.ProjectCola.exceptions;

/**
 * SameTileException is thrown when a player attempts to move a character from and to the same tile.
 *
 * Created by antkwan on 6/26/2016.
 */
public class SameTileException extends Exception {
  private char columnKey;
  private int rowkey;

  public SameTileException( char columnKey, int rowkey ) {
    this.columnKey = columnKey;
    this.rowkey = rowkey;
  }

  public char getColumnKey() {
    return columnKey;
  }

  public int getRowkey() {
    return rowkey;
  }
}
