package com.teenthofabud.game.engine.service;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;

import java.util.Optional;

public interface RPGAPI {

    public Character createCharacter() throws RPGException;

    public void saveGame(Checkpoint checkpoint);

    public Optional<Checkpoint> resumeGame() throws RPGException;

    public boolean deleteGame() throws RPGException;

    public void exitGame();

    public void explore(Map map, Checkpoint checkpoint) throws RPGException;

}
