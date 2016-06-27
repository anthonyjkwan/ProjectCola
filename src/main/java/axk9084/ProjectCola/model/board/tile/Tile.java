package axk9084.ProjectCola.model.board.tile;

import axk9084.ProjectCola.model.Effect;
import axk9084.ProjectCola.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A tile is a representation of a space on the game board.  The tile may contain an entity and a set of effects.
 * A tile has terrain as well which introduces its own effects.
 *
 * Created by antkwan on 6/24/2016.
 */
public abstract class Tile {

  private Entity occupyingEntity = null;

  private List<Effect> effects = new ArrayList<Effect>();

  public Entity getOccupyingEntity() {
    return occupyingEntity;
  }

  public void setOccupyingEntity( Entity occupyingEntity ) {
    this.occupyingEntity = occupyingEntity;
  }

  public List< Effect > getEffects() {
    return effects;
  }

  public void setEffects( List< Effect > effects ) {
    this.effects = effects;
  }

  public abstract List< Effect > getTerrainEffects();
}
