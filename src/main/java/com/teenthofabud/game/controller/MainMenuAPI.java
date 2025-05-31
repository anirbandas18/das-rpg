package com.teenthofabud.game.controller;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;

import java.util.Optional;

public interface MainMenuAPI {

    static final String MAIN_MENU_OPTIONS = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐      Main Menu         ▌
            ▐========================▌
            ▐  C - Create character  ▌
            ▐  S - Save game         ▌
            ▐  R - Resume game       ▌
            ▐  X - Exit game         ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;

    public Character createCharacter() throws MainMenuException;

    public void saveGame(Checkpoint checkpoint) throws MainMenuException;

    public Optional<Checkpoint> resumeGame() throws MainMenuException;

    public void exitGame();

    default String getOptions() {
        return MAIN_MENU_OPTIONS;
    }

}
