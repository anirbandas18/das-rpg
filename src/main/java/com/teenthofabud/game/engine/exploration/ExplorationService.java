package com.teenthofabud.game.engine.exploration;

import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;

import java.awt.*;

public interface ExplorationService {

    public Point move(Map map, String movementKey, Checkpoint checkpoint) throws ExplorationException;

}
