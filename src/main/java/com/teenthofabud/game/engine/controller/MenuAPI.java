package com.teenthofabud.game.engine.controller;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;

import java.util.Optional;

public interface MenuAPI {

    public Character createCharacter() throws MenuException;

    public void saveGame(Checkpoint checkpoint);

    public Optional<Checkpoint> resumeGame();

    public void deleteGame() throws MenuException;

    public void exitGame();

    public void explore(Map map, Checkpoint checkpoint) throws MenuException;

}
