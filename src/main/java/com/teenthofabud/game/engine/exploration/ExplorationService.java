package com.teenthofabud.game.engine.exploration;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.map.Map;

import java.awt.*;

public interface ExplorationService {

    default String menu() {
        return """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐       Explore       ▌
            ▐=====================▌
            ▐  U - Move up        ▌
            ▐  D - Move down      ▌
            ▐  L - Move left      ▌
            ▐  R - Move right     ▌
            ▐  S - Save position  ▌
            ▐  X - Back           ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;
    }

    public void init(Character character, Point point, Map map) throws ExplorationException;

    public boolean move(String movementKey) throws ExplorationException;

    public Point checkpoint();

    public void clear();

}
