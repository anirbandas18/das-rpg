package com.teenthofabud.game.engine.controller.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.MenuAPI;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.engine.renderer.RenderingService;
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

public class DefaultMenuControllerImpl implements MenuAPI {

    private BufferedReader stdin;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private FileManager<Checkpoint> checkpointFileManager;
    private RenderingService renderingService;

    private DefaultMenuControllerImpl(BufferedReader stdin,
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
    public Character createCharacter() throws MenuException {
        Character character = null;
        try {
            renderingService.info("Enter player name: ");
            Player player = playerService.createPlayer(stdin.readLine());
            CharacterType type = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));
            character = characterService.createCharacter(player, type);
            renderingService.success("Created character: " + character);
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            renderingService.error(e.getMessage());
        } catch (IOException e) {
            throw new MenuException(e.getMessage());
        }
        return character;
    }

    @Override
    public void saveGame(Checkpoint checkpoint) throws MenuException {
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
            renderingService.success("Checkpoint saved for " + checkpoint.getCharacter());
        } else {
            renderingService.info("No checkpoint to save!");
        }
    }

    @Override
    public Optional<Checkpoint> resumeGame() throws MenuException {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        renderingService.info("Resuming checkpoint....");
        try {
            optionalCheckpoint = checkpointFileManager.readData();
            if(optionalCheckpoint.isPresent()) {
                renderingService.success("Resumed " + optionalCheckpoint.get().getCharacter() + " from checkpoint");
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

    private static volatile MenuAPI INSTANCE;

    public static MenuAPI getInstance(BufferedReader stdin,
                                      PlayerService playerService,
                                      CharacterTypeService characterTypeService,
                                      CharacterService characterService,
                                      FileManager<Checkpoint> checkpointFileManager,
                                      RenderingService renderingService) {
        MenuAPI result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMenuControllerImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultMenuControllerImpl(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService);
            }
            return INSTANCE;
        }
    }

}
