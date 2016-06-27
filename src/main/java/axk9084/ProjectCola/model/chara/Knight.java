package axk9084.ProjectCola.model.chara;

/**
 * Created by antkwan on 6/25/2016.
 */
public class Knight extends Chara {

  private static final String NAME = "Knight";
  private static final int MAX_HEALTH = 50;
  private static final int ATTACK_POWER = 5;
  private static final int MIN_ATTACK_RANGE = 1;
  private static final int MAX_ATTACK_RANGE = 1;
  private static final int MOVE_SPEED = 1;
  private static final int ACTIONS = 1;

  public Knight( int player ) {
    super( player, NAME, MAX_HEALTH, ATTACK_POWER, MIN_ATTACK_RANGE, MAX_ATTACK_RANGE, MOVE_SPEED, ACTIONS );
  }
}
