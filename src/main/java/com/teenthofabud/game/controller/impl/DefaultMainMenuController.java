package com.teenthofabud.game.controller.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.controller.MainMenuException;
import com.teenthofabud.game.controller.MainMenuAPI;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.renderer.RenderingService;
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
    private RenderingService renderingService;

    private DefaultMainMenuController(BufferedReader stdin,
                                      PlayerService playerService,
                                      CharacterTypeService characterTypeService,
                                      CharacterService characterService,
                                      FileManager<Checkpoint> checkpointFileManager,
                                      RenderingService renderingService) {
        this.stdin = stdin;
        this.playerService = playerService;
        this.characterTypeService = characterTypeService;
        this.characterService = characterService;
        this.checkpointFileManager = checkpointFileManager;
        this.renderingService = renderingService;
    }

    @Override
    public Character createCharacter() throws MainMenuException {
        Character character = null;
        try {
            renderingService.info("Enter player name: ");
            Player player = playerService.createPlayer(stdin.readLine());
            CharacterType type = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));
            character = characterService.createCharacter(player, type);
            renderingService.success("Created character: " + character);
            return character;
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            renderingService.error(e.getMessage());
        } catch (IOException e) {
            throw new MainMenuException(e.getMessage());
        }
        return character;
    }

    @Override
    public void saveGame(Checkpoint checkpoint) throws MainMenuException {
        if(checkpoint != null) {
            if(checkpoint.getCharacter() == null) {
                renderingService.warn("No progress available to save!");
                return;
            }
            renderingService.info("Saving checkpoint....");
            try {
                checkpointFileManager.writeData(checkpoint);
            } catch (FileManagementException e) {
                renderingService.error(e.getMessage());
            }
            renderingService.success("Checkpoint saved");
        } else {
            renderingService.info("No checkpoint to save!");
        }
    }

    @Override
    public Optional<Checkpoint> resumeGame() throws MainMenuException {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        renderingService.info("Resuming checkpoint....");
        try {
            optionalCheckpoint = checkpointFileManager.readData();
            if(optionalCheckpoint.isPresent()) {
                renderingService.success("Resumed from checkpoint");
            } else {
                renderingService.warn("No saved checkpoint available!");
            }
        } catch (FileManagementException e) {
            renderingService.error(e.getMessage());
        }
        return optionalCheckpoint;
    }

    @Override
    public void exitGame() {
        renderingService.info("Exiting game....");
        System.exit(0);
    }

    private static volatile MainMenuAPI instance;

    public static MainMenuAPI getInstance(BufferedReader stdin,
                                          PlayerService playerService,
                                          CharacterTypeService characterTypeService,
                                          CharacterService characterService,
                                          FileManager<Checkpoint> checkpointFileManager,
                                          RenderingService renderingService) {
        MainMenuAPI result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMainMenuController.class) {
            if (instance == null) {
                instance = new DefaultMainMenuController(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService);
            }
            return instance;
        }
    }

}
