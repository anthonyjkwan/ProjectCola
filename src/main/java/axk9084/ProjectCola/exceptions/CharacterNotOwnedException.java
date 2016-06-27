package axk9084.ProjectCola.exceptions;

/**
 * CharacterNotOwnedException is thrown when an attempted action tries to use
 * a character not belonging to the current turn's player
 *
 * Created by antkwan on 6/25/2016.
 */
public class CharacterNotOwnedException extends Exception {

  private String characterName;
  private int ownedByPlayer;
  private int playerAttemptingToUse;

  public CharacterNotOwnedException( String characterName, int ownedByPlayer, int playerAttemptingToUse ) {
    this.characterName = characterName;
    this.ownedByPlayer = ownedByPlayer;
    this.playerAttemptingToUse = playerAttemptingToUse;
  }

  public String getCharacterName() {
    return characterName;
  }

  public int getOwnedByPlayer() {
    return ownedByPlayer;
  }

  public int getPlayerAttemptingToUse() {
    return playerAttemptingToUse;
  }
}
