package axk9084.ProjectCola.model.board.terrain;

import axk9084.ProjectCola.model.Effect;

/**
 *
 * Created by antkwan on 6/24/2016.
 */
public class TerrainEffect implements Effect {

  /**
   * Terrain effects are permanent and constant and do not have an action at the end of a turn. Immediately return.
   */
  public void endTurnAction() {}
}
