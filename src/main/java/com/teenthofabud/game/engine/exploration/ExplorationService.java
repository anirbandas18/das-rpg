package com.teenthofabud.game.engine.exploration;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.map.Map;

import java.awt.*;

public interface ExplorationService {

    public void init(Character character, Point point, Map map) throws ExplorationException;

    public void move(String movementKey) throws ExplorationException;

    public Point checkpoint();

    public void clear();

}
