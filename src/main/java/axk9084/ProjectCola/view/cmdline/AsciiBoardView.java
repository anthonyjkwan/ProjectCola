package axk9084.ProjectCola.view.cmdline;

import axk9084.ProjectCola.controller.ProjectColaEngine;
import axk9084.ProjectCola.exceptions.TileDoesNotExistException;
import axk9084.ProjectCola.model.Entity;
import axk9084.ProjectCola.model.board.Board;
import axk9084.ProjectCola.model.chara.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates visuals of the game board and character status through a command line prompt.
 *
 * For example, a 6x6 board:
 *
 * # Player 1                    #    +---+---+---+---+---+---+   # Player 2                    #
 * ###############################  6 |   |   | P | M |   |   |   ###############################
 * # Knight (K)         HP 50/50 #    +---+---+---+---+---+---+   # Knight (K)         HP 50/50 #
 * # ABILITIES x x x x   ACT 1/1 #  5 |   |   | K | R |   |   |   # ABILITIES x x x x   ACT 1/1 #
 * ###############################    +---+---+---+---+---+---+   ###############################
 * # Rogue (R)          HP 35/35 #  4 |   |   |   |   |   |   |   # Rogue (R)          HP 35/35 #
 * # ABILITIES x x x x   ACT 2/2 #    +---+---+---+---+---+---+   # ABILITIES x x x x   ACT 2/2 #
 * ###############################  3 |   |   |   |   |   |   |   ###############################
 * # Priest (P)         HP 25/25 #    +---+---+---+---+---+---+   # Priest (P)         HP 25/25 #
 * # ABILITIES x x x x   ACT 1/1 #  2 |   |   | K | R |   |   |   # ABILITIES x x x x   ACT 1/1 #
 * ###############################    +---+---+---+---+---+---+   ###############################
 * # Mage (M)           HP 20/20 #  1 |   |   | P | M |   |   |   # Mage (M)           HP 20/20 #
 * # ABILITIES x x x x   ACT 1/1 #    +---+---+---+---+---+---+   # ABILITIES x x x x   ACT 1/1 #
 * ###############################      a   b   c   d   e   f     ###############################
 *
 * The major drawback with this approach is that the font used MUST be fixed width to draw correctly.
 *
 * Created by antkwan on 6/24/2016.
 */
public class AsciiBoardView {

  // Decimal ASCII value for the character 'a'
  private static final int ASCII_a = 97;

  private final ProjectColaEngine engine;
  private Board board;

  public AsciiBoardView( ProjectColaEngine engine ) {
    this.engine = engine;
    this.board = engine.getBoard();
  }

  /**
   * Print the ASCII representation of the board as well as the character statuses for each player.
   * Because of the nature of the command line printing top to bottom, the character status charts are generated before
   * hand and stored as a List of strings.  Iterating through the list provides a string for each line needed for the
   * complete character status chart.  The order of printing thus follows the pattern of printing the 1st player's
   * character status line, printing the first line of the board and then printing the line of the 2nd player's
   * character status.  This is then repeated till the end of the board.
   */
  public void printBoardView() {
    if ( board == null ) {
      System.err.println( "Board is null" );
    }

    int rows = board.getRows();
    int columns = board.getColumns();

    // Generate the character statuses for each player beforehand, this is needed to print the
    // character statuses alongside the board
    List<String> player1Status = generateCharacterStatus( 1, engine.getPlayerCharacters().get( 1 ) );
    List<String> player2Status = generateCharacterStatus( 2, engine.getPlayerCharacters().get( 2 ) );
    Iterator<String> player1StatusIter = player1Status.iterator();
    Iterator<String> player2StatusIter = player2Status.iterator();

    StringBuilder builder = new StringBuilder();

    // For each row in the board
    for ( int row = rows; row > 0; row-- ) {


      // Append the player 1 status line at the front
      appendCharacterStatusLine( builder, player1StatusIter );

      // Generate a divider line
      builder.append( "    " );
      for ( int column = 0; column < columns; column++ ) {
        builder.append( '+' );
        builder.append( '-' ).append( '-' ).append( '-' );;
      }
      builder.append( "+   " );

      // Append the player 2 status line at the end
      appendCharacterStatusLine( builder, player2StatusIter );

      builder.append( System.getProperty( "line.separator" ) );

      // Append the player 1 status line at the front
      appendCharacterStatusLine( builder, player1StatusIter );

      // Generate a entity row, with numerical label at the front
      builder.append( "  " ).append( row ).append( ' ' );
      for ( int column = 0; column < columns; column++ ) {
        builder.append( '|' );
        builder.append( ' ' );
        try {
          builder.append( getEntityRepresentation( board.getEntityAt( (char) ( ASCII_a + column ), row ) ) );
        } catch ( TileDoesNotExistException e ) {
          System.err.println( "Could not find tile " + (char) ( ASCII_a + column ) + row );
          builder.append( '?' );
        }
        builder.append( ' ' );
      }
      builder.append( "|   " );

      // Append the player 2 status line at the end
      appendCharacterStatusLine( builder, player2StatusIter );

      builder.append( System.getProperty( "line.separator" ) );
    }

    // Append the player 1 status line at the front
    appendCharacterStatusLine( builder, player1StatusIter );

    // Generate closing divider
    builder.append( "    " );
    for ( int column = 0; column < columns; column++ ) {
      builder.append( '+' );
      builder.append( '-' ).append( '-' ).append( '-' );
    }
    builder.append( "+   " );

    // Append the player 2 status line at the end
    appendCharacterStatusLine( builder, player2StatusIter );

    builder.append( System.getProperty( "line.separator" ) );

    // Append the player 1 status line at the front
    appendCharacterStatusLine( builder, player1StatusIter );

    // Generate bottom alphabetical label row
    builder.append( "    " );
    for ( int column = 0; column < columns; column++ ) {
      builder.append( ' ' ).append( ' ' );
      builder.append( (char) ( ASCII_a + column ) );
      builder.append( ' ' );
    }
    builder.append( "    " );

    // Append the player 2 status line at the end
    appendCharacterStatusLine( builder, player2StatusIter );

    builder.append( System.getProperty( "line.separator" ) );

    System.out.println( builder.toString() );
  }

  /**
   * Append a character status string to the given builder
   *
   * @param builder  builder to append string to
   * @param iterator  iterator to retrieve status string from
   */
  private void appendCharacterStatusLine( StringBuilder builder, Iterator<String> iterator ) {
    if ( iterator.hasNext() ) {
      builder.append( iterator.next() );
    }
    else {
      // If character status chart has no more strings, append whitespace
      builder.append( "                                 " );
    }
  }

  /**
   * Get the representation of a entity for the board view.
   * This should be replaced when the characters get refactored to not be hard-coded classes
   *
   * @param entity  the entity to get the ASCII representation of
   * @return char representing the entity
   */
  private char getEntityRepresentation( Entity entity ) {
    if ( entity == null ) {
      return ' ';
    }
    else if ( entity instanceof Knight ) {
      return 'K';
    }
    else if ( entity instanceof Rogue ) {
      return 'R';
    }
    else if ( entity instanceof Priest ) {
      return 'P';
    }
    else if ( entity instanceof Mage ) {
      return 'M';
    }
    else {
      return '?';
    }
  }

  /**
   * Takes a list of Characters and builds a character status chart.  It is packaged in a list of strings
   * such that it can be printed out line by line without needing to print the entire chart at once.
   *
   * @param player  the player that owns the characters
   * @param characters  the list of characters to generate the status chart for
   * @return  List of Strings of the character status chart
   */
  private List<String> generateCharacterStatus( int player, List< Chara > characters ) {
    List<String> characterStatus = new ArrayList<String>();

    // The top Player label
    final String playerString = "# Player " + player + "                    #";

    // Generic divider to divide each character
    final String divider = "###############################";

    // Add Player String
    characterStatus.add( playerString );

    // Loop through each character, create their status lines, and add to the characterStatus list
    for ( Chara chara : characters ) {
      StringBuilder builder;
      int spacesToAdd;

      // Generate value strings
      String name = chara.getName() + " (" + getEntityRepresentation( chara ) + ")";
      String hp = "HP " + chara.getHealth() + "/" + chara.getMaxHealth();
      String abilities = "ABILITIES x x x x"; //// TODO: 6/27/2016 Add ability ready/on cooldown indicators
      String actions = "ACT " + chara.getActionsRemaining() + "/" + chara.getActions();
      
      // Add divider
      characterStatus.add( divider );

      // Add Name, icon and HP line
      builder = new StringBuilder();
      builder.append( "# " );
      builder.append( name );
      // Fill with spaces to reach the same string length as the divider
      spacesToAdd = divider.length() - builder.toString().length() - hp.length() - 2;
      for ( int i = 0; i < spacesToAdd; i++) {
        builder.append( ' ' );
      }
      builder.append( hp );
      builder.append( " #" );
      characterStatus.add( builder.toString() );

      // Add abilities and actions line
      builder = new StringBuilder();
      builder.append( "# " );
      builder.append( abilities );
      // Fill with spaces to reach the same string length as the divider
      spacesToAdd = divider.length() - builder.toString().length() - actions.length() - 2;
      for ( int i = 0; i < spacesToAdd; i++) {
        builder.append( ' ' );
      }
      builder.append( actions );
      builder.append( " #" );
      characterStatus.add( builder.toString() );
    }

    // Add final divider
    characterStatus.add( divider );

    return characterStatus;
  }

}
