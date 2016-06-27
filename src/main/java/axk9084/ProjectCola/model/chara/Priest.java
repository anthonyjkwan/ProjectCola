package axk9084.ProjectCola.model.chara;

/**
 * Created by antkwan on 6/27/2016.
 */
public class Priest extends Chara {

  private static final String NAME = "Priest";
  private static final int MAX_HEALTH = 25;
  private static final int ATTACK_POWER = 3;
  private static final int MIN_ATTACK_RANGE = 1;
  private static final int MAX_ATTACK_RANGE = 1;
  private static final int MOVE_SPEED = 1;
  private static final int ACTIONS = 1;

  public Priest( int player ) {
    super( player, NAME, MAX_HEALTH, ATTACK_POWER, MIN_ATTACK_RANGE, MAX_ATTACK_RANGE, MOVE_SPEED, ACTIONS );
  }

}
