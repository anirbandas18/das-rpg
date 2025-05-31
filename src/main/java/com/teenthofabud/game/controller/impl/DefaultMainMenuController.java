package com.teenthofabud.game.controller.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.controller.MainMenuException;
import com.teenthofabud.game.controller.MainMenuAPI;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
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
    private FileManager<Checkpoint> checkpointFileManager;

    private DefaultMainMenuController(BufferedReader stdin,
                                      PlayerService playerService,
                                      CharacterTypeService characterTypeService,
                                      CharacterService characterService,
                                      FileManager<Checkpoint> checkpointFileManager) {
        this.stdin = stdin;
        this.playerService = playerService;
        this.characterTypeService = characterTypeService;
        this.characterService = characterService;
        this.checkpointFileManager = checkpointFileManager;
    }

    @Override
    public Character createCharacter() throws MainMenuException {
        Character character = null;
        try {
            System.out.println("Enter player name: ");
            Player player = playerService.createPlayer(stdin.readLine());
            CharacterType type = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));
            character = characterService.createCharacter(player, type);
            System.out.println("Created character: " + character);
            return character;
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new MainMenuException(e.getMessage());
        }
        return character;
    }

    @Override
    public void saveGame(Checkpoint checkpoint) throws MainMenuException {
        if(checkpoint != null) {
            if(checkpoint.getCharacter() == null) {
                System.out.println("Character is required to be saved!");
                return;
            }
            System.out.println("Saving checkpoint....");
            try {
                checkpointFileManager.writeData(checkpoint);
            } catch (FileManagementException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("Checkpoint saved");
        } else {
            System.out.println("No checkpoint to save!");
        }
    }

    @Override
    public Optional<Checkpoint> resumeGame() throws MainMenuException {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        System.out.println("Resuming checkpoint....");
        try {
            optionalCheckpoint = checkpointFileManager.readData();
            if(optionalCheckpoint.isPresent()) {
                System.out.println("Resumed from checkpoint");
            } else {
                System.out.println("No saved checkpoint available!");
            }
        } catch (FileManagementException e) {
            System.err.println(e.getMessage());
        }
        return optionalCheckpoint;
    }

    @Override
    public void exitGame() {
        System.out.println("Exiting game....");
        System.exit(0);
    }

    private static volatile MainMenuAPI instance;

    public static MainMenuAPI getInstance(BufferedReader stdin,
                                          PlayerService playerService,
                                          CharacterTypeService characterTypeService,
                                          CharacterService characterService,
                                          FileManager<Checkpoint> checkpointFileManager) {
        MainMenuAPI result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMainMenuController.class) {
            if (instance == null) {
                instance = new DefaultMainMenuController(stdin, playerService, characterTypeService, characterService, checkpointFileManager);
            }
            return instance;
        }
    }

}
