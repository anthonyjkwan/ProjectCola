package axk9084.ProjectCola.model;

/**
 * Represents a effect on a tile, magical or otherwise, that influences entities on the same tile.
 * In addition, effects may endTurnAction at the end of every turn.
 * An example of a effect action can be a tornado moving tiles.
 *
 * Created by antkwan on 6/24/2016.
 */
public interface Effect {

  /**
   * An action taken by the effect at the end of a turn.  Effects are not required to make actions and can
   * immediately return.
   */
  public void endTurnAction();

}
