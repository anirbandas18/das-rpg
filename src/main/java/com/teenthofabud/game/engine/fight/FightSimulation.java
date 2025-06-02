package com.teenthofabud.game.engine.fight;

import com.teenthofabud.game.constants.status.FightState;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.enemy.Enemy;

public interface FightSimulation {

    default String menu() {
        return """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐       Fight      ▌
            ▐==================▌
            ▐  W - Kick  (10)  ▌
            ▐  A - Slide (15)  ▌
            ▐  S - Head  (5)   ▌
            ▐  D - Pass  (1)   ▌
            ▐  X - Back        ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;
    }

    public void start(Character character, Integer experience, Enemy enemy) throws FightException;

    public boolean act(String actionKey) throws FightException;

    public int experience();

    public FightState status();

    public void stop();

}
