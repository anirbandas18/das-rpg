package com.teenthofabud.game.engine.controller;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;

import java.util.Optional;

public interface MenuAPI {

    public Character createCharacter() throws MenuException;

    public void saveGame(Checkpoint checkpoint) throws MenuException;

    public Optional<Checkpoint> resumeGame() throws MenuException;

    public void exitGame();

}
