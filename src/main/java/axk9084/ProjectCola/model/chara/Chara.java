package axk9084.ProjectCola.model.chara;

import axk9084.ProjectCola.model.Entity;

/**
 * A Chara represents a game piece on the board.
 * Using Chara instead of Character to avoid confusion with java.lang.Character
 *
 * Created by antkwan on 6/24/2016.
 */
public abstract class Chara implements Entity {

  // Describes which player this character belongs to
  private final int player;

  // The name of the character
  private final String name;

  // The max health of the character
  private final int maxHealth;

  // The current health of the character
  private int health;

  // The attack power of the character
  private final int attackPower;

  // The minimum attack range of the character
  private final int minAttackRange;

  // The maximum attack range of the character
  private final int maxAttackRange;

  // Describes how many spaces the character can move
  private final int moveSpeed;

  // The number of actions a character can make in a single turn
  private final int actions;

  // The number of actions a character has left on this turn
  private int actionsRemaining;

  public Chara( int player, String name, int maxHealth, int attackPower, int minAttackRange, int maxAttackRange, int moveSpeed, int actions ) {
    this.player = player;
    this.name = name;
    this.maxHealth = maxHealth;
    this.attackPower = attackPower;
    this.minAttackRange = minAttackRange;
    this.maxAttackRange = maxAttackRange;
    this.moveSpeed = moveSpeed;
    this.actions = actions;

    this.health = this.maxHealth;
    this.actionsRemaining = this.actions;
  }

  public String getName() {
    return name;
  }

  public int getPlayer() {
    return player;
  }

  public int getMoveSpeed() {
    return moveSpeed;
  }

  public int getActions() {
    return actions;
  }

  public int getActionsRemaining() {
    return actionsRemaining;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getHealth() {
    return health;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public int getMinAttackRange() {
    return minAttackRange;
  }

  public int getMaxAttackRange() {
    return maxAttackRange;
  }

  public void decrementActionsRemaining() {
    actionsRemaining--;
  }

  public void resetActionsRemaining() {
    actionsRemaining = actions;
  }

  public void takeDamage( int incomingDamage ) {
    health = health - incomingDamage;
  }

  public boolean isDead() {
    return health < 1;
  }
}
