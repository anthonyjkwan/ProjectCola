package axk9084.ProjectCola.exceptions;

/**
 * TileDoesNotExistException is thrown when attempting to use a Tile with a column/row key pairing that does not exist.
 *
 * Created by antkwan on 6/26/2016.
 */
public class TileDoesNotExistException extends Exception {
  private char columnKey;
  private int rowkey;

  public TileDoesNotExistException( char columnKey, int rowkey ) {
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
