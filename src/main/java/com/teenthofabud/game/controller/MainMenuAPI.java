package com.teenthofabud.game.controller;

import com.teenthofabud.game.resources.character.Character;

import java.util.Optional;

public interface MainMenuAPI {

    static final String MAIN_MENU_OPTIONS = """
            Main Menu:
            ====================
            C - Create character
            X - Exit game
            Enter your option:  
            """;

    static final String CHARACTER_MENU_OPTIONS = """
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
            """;

    public Optional<Character> createCharacter();

    public void exitGame();

    default String getOptions() {
        return MAIN_MENU_OPTIONS;
    }

}
