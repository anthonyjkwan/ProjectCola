package axk9084.ProjectCola.controller;


import axk9084.ProjectCola.exceptions.*;
import axk9084.ProjectCola.model.Entity;
import axk9084.ProjectCola.model.board.Board;
import axk9084.ProjectCola.model.chara.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main engine that contains commands to play the game and update the model accordingly.
 * It also contains the rules and validity checks of the game.
 *
 * Created by antkwan on 6/24/2016.
 */
public class ProjectColaEngine {

  private Board board;

  // Mapping of a player to their characters
  private Map< Integer, List< Chara > > playerCharacters;

  // Turn counter
  private int turn = 1;

  // Game winner flag which remains null until a player wins
  private Integer gameWinner = null;

  public ProjectColaEngine() {
    setupDefaultBoard();
    turn = 1;
  }


  /**
   * This call sets up a default board with default size and piece placings.
   * This should be replaced in the future with the capability to customize boards.
   * Customized boards should enforce size restrictions
   */
  private void setupDefaultBoard() {
    board = new Board( 6, 6 );

    try {

      playerCharacters = new HashMap<Integer, List<Chara>>();

      // Player 1 characters
      playerCharacters.put( 1, new ArrayList< Chara >() );
      addCharacterToBoard( 'c', 2, new Knight( 1 ) );
      addCharacterToBoard( 'd', 2, new Rogue( 1 ) );
      addCharacterToBoard( 'c', 1, new Priest( 1 ) );
      addCharacterToBoard( 'd', 1, new Mage( 1 ) );

      // Player 2 characters
      playerCharacters.put( 2, new ArrayList< Chara >() );
      addCharacterToBoard( 'c', 5, new Knight( 2 ) );
      addCharacterToBoard( 'd', 5, new Rogue( 2 ) );
      addCharacterToBoard( 'c', 6, new Priest( 2 ) );
      addCharacterToBoard( 'd', 6, new Mage( 2 ) );

    } catch ( TileDoesNotExistException e ) {
      // This error should not occur with hardcoded value.
      // In the future with custom board, error handling should be added
      e.printStackTrace();
    }

  }

  /**
   * Helper method to add a character to the playerCharacters collection as well as the board
   *
   * @param column  column location of the chara
   * @param row     row location of the chara
   * @param chara   the chara to be added
   * @throws TileDoesNotExistException  if the given column and row do not correspond to a valid tile
   */
  private void addCharacterToBoard( char column, int row, Chara chara )
      throws TileDoesNotExistException {

    playerCharacters.get( chara.getPlayer() ).add( chara );
    board.setEntityAt( column, row, chara );
  }

  /**
   * Get the board
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Gets the player number of the current turn
   */
  public int getTurnPlayer() {
    // Because there is only 2 players at the moment, get which player based on current turn count
    return turn % 2 != 0 ? 1 : 2;
  }

  /**
   * Helper method to get a character at a given column and row.
   * This contains null checks and type checks for the Chara super class.
   *
   * @param column  column of the chara to get
   * @param row     row of the chara to get
   * @return  the chara of the given column and row
   * @throws MissingCharacterException  if there is no character at the given tile
   * @throws TileDoesNotExistException  if the given column and row do not correspond to a valid tile
   */
  private Chara getCharacterAt( char column, int row )
      throws MissingCharacterException, TileDoesNotExistException {

    Entity entity = board.getEntityAt( column, row  );

    if ( entity != null && entity instanceof Chara ) {
      return (Chara) entity;
    }
    else {
      throw new MissingCharacterException( column, row );
    }
  }

  /**
   * Checks if a given chara's player matches the player of the current turn
   *
   * @param chara  the chara to check the turn validity of
   * @throws CharacterNotOwnedException when the chara's player does not match the current turn's player
   * @throws NullPointerException when null chara is given as a parameter
   */
  private void checkTurnValidity( Chara chara )
      throws CharacterNotOwnedException {

    if ( chara == null ) {
      throw new NullPointerException();
    }

    // Check that the chara exists and that the owning player matches the current player's turn
    if( chara.getPlayer() != getTurnPlayer() ) {
      throw new CharacterNotOwnedException( chara.getName(), chara.getPlayer(), getTurnPlayer() );
    }
  }

  /**
   * Helper method to check if a tile is empty.
   *
   * @param column  column of the tile to check
   * @param row     row of the tile to check
   * @return true if tile is empty, false if not
   * @throws TileDoesNotExistException  if the given column and row do not correspond to a valid tile
   */
  private boolean isTileEmpty( char column, int row )
      throws TileDoesNotExistException {

    return board.getEntityAt( column, row ) == null;
  }

  /**
   * Moves a character from one tile to another.
   *
   * @param fromColumn  the column of the tile of the character that is moving
   * @param fromRow  the row of the tile of the character that is moving
   * @param toColumn  the column of the tile that the character is moving to
   * @param toRow  the row of the tile that the character is moving to
   * @throws MissingCharacterException  if character does not exist on the tile with the given fromColumn and fromRow
   * @throws CharacterNotOwnedException  if the character attempting to move does not belong to the current player
   * @throws NoActionsRemainingException  if the character attempting to move does not have any actions remaining
   * @throws TileOccupiedException  if the tile the character is attempting to move to is occupied by another entity
   * @throws TileDoesNotExistException  if the tile the character is attempting to move to does not exist
   * @throws SameTileException  if the tile the character is attempting to move to is the same one it is on
   * @throws OutOfMoveRangeException  if the tile the character is attempting to move to is out of its movement range
   */
  public void moveCharacter(  char fromColumn, int fromRow, char toColumn, int toRow )
      throws MissingCharacterException, CharacterNotOwnedException, NoActionsRemainingException,
      TileOccupiedException, TileDoesNotExistException, SameTileException, OutOfMoveRangeException {

    // Check if the tile we're moving from is different from the one we're moving to
    if ( fromColumn == toColumn && fromRow == toRow ) {
      throw new SameTileException( fromColumn, fromRow );
    }

    // Get the chara and check that the chara is owned by this player
    Chara chara = getCharacterAt( fromColumn, fromRow  );
    checkTurnValidity( chara );

    // Check if the chara has an action left
    if ( chara.getActionsRemaining() < 1 ) {
      throw new NoActionsRemainingException( chara.getName() );
    }

    // Check if the chara can move that far
    if ( !isWithinMoveRange( fromColumn, fromRow, toColumn, toRow, chara.getMoveSpeed() ) ) {
      throw new OutOfMoveRangeException( chara.getName(), toColumn, toRow, chara.getMoveSpeed() );
    }

    // Check if the target row and column is empty or occupied
    if ( !isTileEmpty( toColumn, toRow ) ) {
      throw new TileOccupiedException( chara.getName(), toColumn, toRow );
    }

    // Move the chara and decrement action if successful
    board.moveEntity( fromColumn, fromRow, toColumn, toRow );
    chara.decrementActionsRemaining();
  }

  /**
   * Helper method to get the distance from one tile to another tile with the given column and row keys of each tile.
   *
   * @param fromColumn  1st column
   * @param fromRow  1st row
   * @param toColumn  2nd column
   * @param toRow  2nd row
   * @return  the int distance between the two tiles
   */
  private int getDistance( char fromColumn, int fromRow, char toColumn, int toRow ) {
    return Math.abs( fromColumn - toColumn ) + Math.abs( fromRow - toRow );
  }


  /**
   * Helper method to check if two tiles are in range of each other given a movement range .
   *
   * @param fromColumn  1st column
   * @param fromRow  1st row
   * @param toColumn  2nd column
   * @param toRow  2nd row
   * @param moveRange  the movement range
   * @return true if within range, false if not
   */
  private boolean isWithinMoveRange( char fromColumn, int fromRow, char toColumn, int toRow, int moveRange ) {
    return moveRange >= getDistance( fromColumn, fromRow, toColumn, toRow );
  }

  /**
   * Attack with a character.
   *
   * @param fromColumn  column of the attacking character
   * @param fromRow  row of the attacking character
   * @param toColumn  column of the target character
   * @param toRow  row of the target character
   * @throws MissingCharacterException  if character does not exist on the tile with the given fromColumn and fromRow
   * @throws CharacterNotOwnedException  if the character attempting to attack does not belong to the current player
   * @throws NoActionsRemainingException  if the character attempting to attack does not have any actions remaining
   * @throws TileDoesNotExistException  if the tile the character is attempting to attack to does not exist
   * @throws SameTileException  if the tile the character is attempting to attack to is the same one it is on
   * @throws OutOfAttackRangeException  if the tile the character is attempting to attack is out of attack range
   * @throws NoEnemyException  if the tile the character is attempting to attack contains no enemy character
   */
  public void attackCharacter( char fromColumn, int fromRow, char toColumn, int toRow )
      throws MissingCharacterException, CharacterNotOwnedException, NoActionsRemainingException,
       TileDoesNotExistException, SameTileException, OutOfAttackRangeException, NoEnemyException {

    // Check if the tile we're attacking from is different from the one we're attacking
    if ( fromColumn == toColumn && fromRow == toRow ) {
      throw new SameTileException( fromColumn, fromRow );
    }

    // Get the chara and check that the chara is owned by this player
    Chara chara = getCharacterAt( fromColumn, fromRow  );
    checkTurnValidity( chara );

    // Check if the chara has an action left
    if ( chara.getActionsRemaining() < 1 ) {
      throw new NoActionsRemainingException( chara.getName() );
    }

    // Check if the chara can move that far
    if ( !isWithinAttackRange( fromColumn, fromRow, toColumn, toRow, chara.getMinAttackRange(), chara.getMaxAttackRange() ) ) {
      throw new OutOfAttackRangeException( chara.getName(), toColumn, toRow, chara.getMinAttackRange(), chara.getMaxAttackRange() );
    }

    // Check if the target row and column has an occupying enemy character
    Chara enemyChara = getCharacterAt( toColumn, toRow );
    if ( enemyChara == null || enemyChara.getPlayer() == getTurnPlayer() ) {
      throw new NoEnemyException( toColumn, toRow );
    }

    // Attack the chara and decrement action if successful
    //// TODO: 6/27/2016 Could use some trigger (observer pattern) to tell view an attack occurred
    enemyChara.takeDamage( chara.getAttackPower() );
    chara.decrementActionsRemaining();

    // Check for chara death
    if ( enemyChara.isDead() ) {
      characterDeath( toColumn, toRow, enemyChara );
    }
  }

  /**
   * Helper method to determine when given column and row keys of two tiles if the tiles are within attack range
   * with given min/max attack ranges
   *
   * @param fromColumn  1st column
   * @param fromRow  1st row
   * @param toColumn  2nd column
   * @param toRow  2nd row
   * @param minAttackRange  minimum attack range
   * @param maxAttackRange  maximum attack range
   * @return true if within attack range, false if not
   */
  private boolean isWithinAttackRange( char fromColumn, int fromRow, char toColumn, int toRow,
                                       int minAttackRange, int maxAttackRange ) {

    int distance = getDistance( fromColumn, fromRow, toColumn, toRow );
    return distance >= minAttackRange && distance <= maxAttackRange;
  }

  /**
   * Helper method to execute on a character's death.
   * Remove the character from the playerCharacters collection and the board.
   * Check for a winner.
   *
   * @param column  column of where the character died
   * @param row  row of where the character died
   * @param chara  the character that died
   * @throws TileDoesNotExistException if the given column and row do not correspond to a valid tile
   */
  private void characterDeath( char column, int row, Chara chara )
      throws TileDoesNotExistException {
    board.setEntityAt( column, row, null );
    playerCharacters.get( chara.getPlayer() ).remove( chara );
    checkForWin();
  }

  /**
   * Helper method that checks for a winner by checking if a player's characters have all been killed.
   */
  private void checkForWin() {
    for ( Map.Entry<Integer, List<Chara>> entry : playerCharacters.entrySet() ) {
      if ( entry.getValue().isEmpty() ) {
        gameWinner = entry.getKey() == 1 ? 2 : 1;
      }
    }
  }

  public void useCharacterAbility() {
    //// TODO: 6/27/2016 Implement ability usage 
  }

  /**
   * End a player's turn.  Currently only resets the actions of the characters and increments the turn.
   */
  public void endPlayerTurn() {

    // Check casts
    //// TODO: 6/25/2016 Check casts at end of turn

    // Check effects
    //// TODO: 6/25/2016 Check effects at end of turn

    // Reset character action points
    resetCharacterActions();

    turn++;
  }

  /**
   * Method for the current player to forfeit the game.
   * Flags the opposite player as the winner.
   */
  public void playerForfeit() {
    gameWinner = getTurnPlayer() == 1 ? 2 : 1;
  }

  /**
   * Get the winner.
   *
   * @return the winner of the game, else null if game still in progress
   */
  public Integer getGameWinner() {
    return gameWinner;
  }

  /**
   * Get the mapping of players to their characters
   *
   * @return  Map of player to character mappings
   */
  public Map<Integer, List<Chara>> getPlayerCharacters() {
    //// TODO: 6/27/2016 Perform deep copy to avoid abuse from views
    return playerCharacters;
  }

  /**
   * Helper method to reset the character action points of the current player
   */
  private void resetCharacterActions() {
    for ( Chara chara : playerCharacters.get( getTurnPlayer() ) ) {
      chara.resetActionsRemaining();
    }
  }

}
