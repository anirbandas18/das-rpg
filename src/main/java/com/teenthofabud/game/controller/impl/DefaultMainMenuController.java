package com.teenthofabud.game.controller.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.controller.MainMenuAPI;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class DefaultMainMenuController implements MainMenuAPI {

    private BufferedReader stdin;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;

    private DefaultMainMenuController(BufferedReader stdin, PlayerService playerService, CharacterTypeService characterTypeService, CharacterService characterService) {
        this.characterService = characterService;
        this.characterTypeService = characterTypeService;
        this.playerService = playerService;
        this.stdin = stdin;
    }

    @Override
    public Optional<Character> createCharacter() {
        boolean flag = true;
        Optional<Character> optionalCharacter = Optional.empty();
        try {
            System.out.println("Enter player name: ");
            Player player = playerService.createPlayer(stdin.readLine());
            CharacterType type = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));
            while(flag) {
                System.out.print(CHARACTER_MENU_OPTIONS);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "S", "M", "D", "R", "G" -> {
                        type = characterTypeService.retrieveCharacterType(option.toUpperCase());
                        flag = false;
                    }
                    case "N" -> {
                        flag = false;
                    }
                    case "X" -> {
                        System.out.println("Returning back to main menu....");
                        return optionalCharacter;
                    }
                    default -> System.err.println("Option " + option + " not supported. Try again!");
                }
            }
            Character character = characterService.createCharacter(player, type);
            System.out.println("Created character: " + character);
            optionalCharacter = Optional.of(character);
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Game failure: " + e.getMessage());
        }
        return optionalCharacter;
    }

    @Override
    public void exitGame() {
        System.out.println("Exiting game....");
        System.exit(0);
    }

    private static volatile MainMenuAPI instance;

    public static MainMenuAPI getInstance(BufferedReader stdin, PlayerService playerService, CharacterTypeService characterTypeService, CharacterService characterService) {
        MainMenuAPI result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMainMenuController.class) {
            if (instance == null) {
                instance = new DefaultMainMenuController(stdin, playerService, characterTypeService, characterService);
            }
            return instance;
        }
    }

}
