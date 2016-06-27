package axk9084.ProjectCola.view.cmdline;

import axk9084.ProjectCola.controller.ProjectColaEngine;
import axk9084.ProjectCola.exceptions.*;

import java.io.*;

/**
 * The command line entry point of ProjectCola.  This class is responsible for the parsing of input as
 * well as the instantiation and interaction of the game engine.
 *
 * todo* A future possibility for less coupling could be to have a single Main class that is responsible for the
 * todo* engine and chose a UI (cmd line versus GUI) based on command line args
 *
 * Created by antkwan on 6/24/2016.
 */
public class CmdLineMain {

  private static BufferedReader reader;
  private static ProjectColaEngine engine;
  private static AsciiBoardView boardView;

  public static void main( String[] args ) {
    reader = new BufferedReader( new InputStreamReader( System.in ) );
    try {
      startMainMenu( reader );
    }
    catch ( IOException e ) {
      System.err.println( "Input reader encountered an error" );
    }
    finally {
      try {
        reader.close();
      }
      catch ( IOException e ) {
        System.err.println( "Error closing input reader" );
      }
    }

  }

  /**
   * Start game on the command line using the given reader for player input
   *
   * @param reader - reader for player input
   * @throws IOException - on reader failure
   */
  public static void startMainMenu( BufferedReader reader )
      throws IOException {

    System.out.println( "Project Cola" );

    boolean running = true;

    while ( running ) {

      Character input = null;

      // Keep reading until we get a single character input
      while ( input == null ) {
        System.out.println( "== Main Menu ==" );
        System.out.println( "1) Start Game" );
        System.out.println( "9) Exit" );
        System.out.print( ">" );
        String inputLine = reader.readLine();
        input = ( inputLine != null && inputLine.length() == 1 ) ? inputLine.charAt( 0 ) : null;
      }

      // Act based on the character input
      switch ( input ) {
        case '1':
          System.out.println( "Initializing engine..." );
          startGame();
          break;
        case '9':
          System.out.println( "Goodbye!" );
          running = false; // Set running to false to break out of outer loop
          break;
      }

    }

  }

  /**
   * startGame generates a new engine and board view as well as start the state machine for the game.
   * @throws IOException
   */
  private static void startGame() throws IOException {
    engine = new ProjectColaEngine();
    boardView = new AsciiBoardView( engine );

    // Flag to keep running the game until the game is over
    boolean gameRunning = true;

    while ( gameRunning ) {
      Character input = null;

      // Keep reading until we get a single character input
      while ( input == null ) {
        boardView.printBoardView();

        System.out.println( "== Player " + engine.getTurnPlayer() + "'s Turn ==" );
        System.out.println( "1) Move             2) Attack" );
        System.out.println( "3) Use Ability      4) End Turn" );
        System.out.println( "5) Forfeit          9) Exit to Main Menu" );
        System.out.print( ">" );
        String inputLine = reader.readLine();
        input = ( inputLine != null && inputLine.length() == 1 ) ? inputLine.charAt( 0 ) : null;
      }

      // Act based on the character input
      switch ( input ) {
        case '1':
          move();
          break;
        case '2':
          attack();
          break;
        case '3':
          ability();
          break;
        case '4':
          engine.endPlayerTurn();
          break;
        case '5':
          forfeit();
          break;
        case '9':
          System.out.println( "Returning to Main Menu" );
          gameRunning = false;
          break;
      }

      // Check for win
      if ( engine.getGameWinner() != null ) {
        System.out.println( "Player " + engine.getGameWinner() + " has won the game!" );
        System.out.println();
        gameRunning = false;
      }
    }

  }

  /**
   * Begins the prompts for moving a character from one tile to another
   *
   * @throws IOException On reader failure
   */
  private static void move() throws IOException {

    // Continue retrying until a valid move is made
    boolean validMoveMade = false;
    while ( !validMoveMade ) {

      // From column and row represents the current tile position of the character that is going to move
      Character fromColumn = null;
      Integer fromRow = null;

      // To column and row represents the tile position the character will move to
      Character toColumn = null;
      Integer toRow = null;

      // Loop until we get a tile column-row key pair for the current tile that has valid syntax
      while ( fromColumn == null ) {
        System.out.println( "Move what? (ex. b2, type c to cancel)" );
        System.out.print( ">" );
        String inputLine = reader.readLine();

        // Cancel and return out of move call if 'c' is entered
        if ( inputLine.equals( "c" ) ) {
          System.out.println( "Cancelling move." );
          return;
        }

        if ( isValidTileInput( inputLine ) ) {
          fromColumn = inputLine.charAt( 0 );
          fromRow = Character.getNumericValue( inputLine.charAt( 1 ) );
        }

      }

      // Loop until we get a tile column-row key pair for the target tile that has valid syntax
      while ( toColumn == null ) {
        System.out.println( "Move " + fromColumn + fromRow + " to where? (type c to cancel)" );
        System.out.print( ">" );
        String inputLine = reader.readLine();

        // Cancel and return out of move call if 'c' is entered
        if ( inputLine.equals( "c" ) ) {
          System.out.println( "Cancelling move." );
          return;
        }

        if ( isValidTileInput( inputLine ) ) {
          toColumn = inputLine.charAt( 0 );
          toRow = Character.getNumericValue( inputLine.charAt( 1 ) );
        }

      }

      // Attempt to move the character.  If any known errors occur, print respective message
      try {
        engine.moveCharacter( fromColumn, fromRow, toColumn, toRow );
        System.out.println( "Move: " + fromColumn + fromRow + " to " + toColumn + toRow );
        validMoveMade = true;  // Set valid move flag if no errors occurred
      } catch ( NoActionsRemainingException e ) {
        System.out.println( e.getCharacterName() + " has no actions remaining." );
      } catch ( TileDoesNotExistException e ) {
        System.out.println( "Tile " + e.getColumnKey() + e.getRowkey() + " does not exist." );
      } catch ( MissingCharacterException e ) {
        System.out.println( "There is no character on tile " + e.getColumn() + e.getRow() + "." );
      } catch ( SameTileException e ) {
        System.out.println( "Can not move to the same tile " + e.getColumnKey() + e.getRowkey() + "." );
      } catch ( TileOccupiedException e ) {
        System.out.println( "Tile " + e.getColumn() + e.getRow() + " is already occupied by " + e.getCharacterName() + "." );
      } catch ( CharacterNotOwnedException e ) {
        System.out.println( "Character " + e.getCharacterName() + " is owned by Player " + e.getOwnedByPlayer() + "." );
      } catch ( OutOfMoveRangeException e ) {
        System.out.println( e.getCharacterName() + " can not move to "  + e.getColumnKey() + e.getRowKey()
            + " with a move range of " + e.getMoveRange() + ".");
      }

      // Re-print board if a valid move was not made
      if ( !validMoveMade ) {
        boardView.printBoardView();
      }
    }
  }

  /**
   * Begins the prompts to attack another character
   *
   * @throws IOException On reader failure
   */
  private static void attack() throws IOException {

    // Continue retrying until a valid attack is made
    boolean validAttackMade = false;

    while ( !validAttackMade ) {

      // From column and row represents the current tile position of the character that is going to attack
      Character fromColumn = null;
      Integer fromRow = null;

      // To column and row represents the tile position the character will attack
      Character toColumn = null;
      Integer toRow = null;

      // Loop until we get a tile column-row key pair for the current tile that has valid syntax
      while ( fromColumn == null ) {
        System.out.println( "Attack with who? (ex. b2, type c to cancel)" );
        System.out.print( ">" );
        String inputLine = reader.readLine();

        if ( inputLine.equals( "c" ) ) {
          System.out.println( "Cancelling attack." );
          return;
        }

        // Cancel and return out of move call if 'c' is entered
        if ( isValidTileInput( inputLine ) ) {
          fromColumn = inputLine.charAt( 0 );
          fromRow = Character.getNumericValue( inputLine.charAt( 1 ) );
        }

      }

      // Loop until we get a tile column-row key pair for the current tile that has valid syntax
      while ( toColumn == null ) {
        System.out.println( "Attack from " + fromColumn + fromRow + " to where? (type c to cancel)" );
        System.out.print( ">" );
        String inputLine = reader.readLine();

        // Cancel and return out of move call if 'c' is entered
        if ( inputLine.equals( "c" ) ) {
          System.out.println( "Cancelling attack." );
          return;
        }

        if ( isValidTileInput( inputLine ) ) {
          toColumn = inputLine.charAt( 0 );
          toRow = Character.getNumericValue( inputLine.charAt( 1 ) );
        }

      }

      // Attempt to attack with the character.  If any known errors occur, print respective message
      try {
        engine.attackCharacter( fromColumn, fromRow, toColumn, toRow );
        System.out.println( "Attack: " + fromColumn + fromRow + " to " + toColumn + toRow );
        validAttackMade = true;
      } catch ( NoActionsRemainingException e ) {
        System.out.println( e.getCharacterName() + " has no actions remaining." );
      } catch ( TileDoesNotExistException e ) {
        System.out.println( "Tile " + e.getColumnKey() + e.getRowkey() + " does not exist." );
      } catch ( MissingCharacterException e ) {
        System.out.println( "There is no character on tile " + e.getColumn() + e.getRow() + "." );
      } catch ( SameTileException e ) {
        System.out.println( "Can not attack the same tile " + e.getColumnKey() + e.getRowkey() + "." );
      } catch ( CharacterNotOwnedException e ) {
        System.out.println( "Character " + e.getCharacterName() + " is owned by Player " + e.getOwnedByPlayer() + "." );
      } catch ( OutOfAttackRangeException e ) {
        System.out.println( "Character " + e.getCharacterName() + " with attack range " + e.getMinAttackRange() + "-"
            + e.getMaxAttackRange() + " is out of range of " + e.getColumnKey() + e.getRowKey() + "." );
      } catch ( NoEnemyException e ) {
        System.out.println( "There is no enemy on tile " + e.getColumnKey() + e.getRowKey() );
      }

      // Re-print board if a valid attack was not made
      if ( !validAttackMade ) {
        boardView.printBoardView();
      }
    }
  }

  /**
   * Tests that a string has valid syntax that can represent a tile
   *
   * @param input  the string to be tested
   * @return  true if valid, false if not
   */
  private static boolean isValidTileInput( String input ) {
    return input.matches( "^[a-z]\\d$" );
  }

  /**
   * Begins the prompts to use a character ability
   */
  private static void ability() {
    //// TODO: 6/27/2016 Implement ability usage
  }

  /**
   * Forfeit the game
   */
  private static void forfeit() {
    System.out.println( "Player " + engine.getTurnPlayer() + " forfeits!" );
    engine.playerForfeit();
  }

}
