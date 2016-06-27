package axk9084.ProjectCola.model.board;

import axk9084.ProjectCola.exceptions.TileDoesNotExistException;
import axk9084.ProjectCola.model.Entity;
import axk9084.ProjectCola.model.board.tile.PlainTile;
import axk9084.ProjectCola.model.board.tile.Tile;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by antkwan on 6/25/2016.
 */
public class Board {

  private final static int ASCII_a = (int) 'a';

  // The map represents the game board.
  // The outer map contains mappings of column key to row map.
  // The inner row maps contain mapping of row key to a Tile
  // The column keys are alphabetical characters starting from 'a'
  // The row keys are incrementing integers beginning at 1.
  private SortedMap< Character, SortedMap< Integer, Tile > > tiles;

  final private int rows;
  final private int columns;

  public Board( int rows, int columns ) {
    this.rows = rows;
    this.columns = columns;

    tiles = new TreeMap< Character, SortedMap< Integer, Tile > >();
    
    // Board generation
    for ( int column = 0; column < columns; column++ ) {
      char columnKey = (char) (ASCII_a + column);

      tiles.put( columnKey, new TreeMap< Integer, Tile >() );

      for ( int row = 1; row < rows + 1; row++ ) {
        tiles.get( columnKey ).put( row, new PlainTile() );
      }
    }

  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  private Tile getTile( char column, int row )
      throws TileDoesNotExistException {

    SortedMap< Integer, Tile > columnMap = tiles.get( column );

    if ( columnMap != null ) {
      Tile tile = columnMap.get( row );

      if ( tile != null ) {
        return tile;
      }
    }

    throw new TileDoesNotExistException( column, row );
  }

  /**
   * Get the entity at a given row and column.
   *
   * @param column  the column key of the Board
   * @param row  the row key of the Board
   *
   * @return the Entity, else null if empty
   * @throws TileDoesNotExistException when the given column and row pairing do not have a Tile
   */
  public Entity getEntityAt( char column, int row )
      throws TileDoesNotExistException {

    return getTile( column, row ).getOccupyingEntity();
  }

  public void setEntityAt( char column, int row, Entity entity )
      throws TileDoesNotExistException {

    getTile( column, row ).setOccupyingEntity( entity );
  }

  public void moveEntity( char fromColumn, int fromRow, char toColumn, int toRow )
      throws TileDoesNotExistException {

    setEntityAt( toColumn, toRow, getEntityAt( fromColumn, fromRow ) );
    setEntityAt( fromColumn, fromRow, null );
  }

}
