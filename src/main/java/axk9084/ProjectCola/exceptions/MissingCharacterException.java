package axk9084.ProjectCola.exceptions;

/**
 * MissingCharacterException is thrown when an attempted action expects a Chara on a tile but
 * the tile does not contain one.
 *
 * Created by antkwan on 6/25/2016.
 */
public class MissingCharacterException extends Exception {

  // Row and column of the tile of the missing character
  private int row;
  private char column;

  public MissingCharacterException( char column, int row ) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public char getColumn() {
    return column;
  }
}
