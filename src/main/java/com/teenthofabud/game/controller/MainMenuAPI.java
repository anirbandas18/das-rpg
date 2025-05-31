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

    /*static final String MAIN_MENU_OPTIONS = """
            Main Menu:
            ====================
            C - Create character
            S - Save game
            R - Resume game
            X - Exit game
            Enter your option:
            """;*/

   /* static final String CHARACTER_MENU_OPTIONS = """
            Character Menu:
            =====================
            S - Striker
            M - Midfielder
            D - Defender
            R - Referee
            G - Goalkeeper
            N - Random selection
            X - Back to main menu
            Enter your option:  
            """;*/

    public Character createCharacter() throws MainMenuException;

    public void saveGame(Checkpoint checkpoint) throws MainMenuException;

    public Optional<Checkpoint> resumeGame() throws MainMenuException;

    public void exitGame();

    default String getOptions() {
        return MAIN_MENU_OPTIONS;
    }

}
