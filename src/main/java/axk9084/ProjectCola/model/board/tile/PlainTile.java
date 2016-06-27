package axk9084.ProjectCola.model.board.tile;

import axk9084.ProjectCola.model.Effect;

import java.util.Collections;
import java.util.List;

/**
 * A plain tile is a tile with no terrain effects.
 *
 * Created by antkwan on 6/25/2016.
 */
public class PlainTile extends Tile {
  public List< Effect > getTerrainEffects() {
    return Collections.emptyList();
  }
}
