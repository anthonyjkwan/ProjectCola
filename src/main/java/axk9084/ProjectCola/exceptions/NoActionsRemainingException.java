package axk9084.ProjectCola.exceptions;

/**
 * NoActionsRemainingException is thrown when a character attempts an action without any action points remaining.
 *
 * Created by antkwan on 6/25/2016.
 */
public class NoActionsRemainingException extends Exception {
  private String characterName;

  public NoActionsRemainingException( String characterName ) {
    this.characterName = characterName;
  }

  public String getCharacterName() {
    return characterName;
  }
}
