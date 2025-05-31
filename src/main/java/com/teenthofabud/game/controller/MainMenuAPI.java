package com.teenthofabud.game.controller;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;

import java.util.Optional;

public interface MainMenuAPI {

    public Character createCharacter() throws MainMenuException;

    public void saveGame(Checkpoint checkpoint) throws MainMenuException;

    public Optional<Checkpoint> resumeGame() throws MainMenuException;

    public void exitGame();

}
