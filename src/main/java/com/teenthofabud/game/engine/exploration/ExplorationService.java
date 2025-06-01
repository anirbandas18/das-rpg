package com.teenthofabud.game.engine.exploration;

import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;

public interface ExplorationService {

    public void move(Map map, String movementKey, Checkpoint checkpoint) throws ExplorationException;

}
