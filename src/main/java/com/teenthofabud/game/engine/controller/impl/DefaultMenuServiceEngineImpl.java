package com.teenthofabud.game.engine.controller.impl;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.service.impl.DefaultRPGServiceImpl;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.controller.MenuService;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.checkpoint.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.persistence.configuration.DefaultConfigurationFileManagerImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.MapException;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.map.service.impl.DefaultMapServiceImpl;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;

public class DefaultMenuServiceEngineImpl implements MenuService {

    private static final String PLAY_MENU = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐          Play           ▌
            ▐=========================▌
            ▐  C - Create character   ▌
            ▐  E - Explore            ▌
            ▐  S - Save game          ▌
            ▐  D - Delete game        ▌
            ▐  R - Resume game        ▌
            ▐  X - Exit game          ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;

    private BufferedReader stdin;
    private FileManager<Checkpoint, Path> checkpointFileManager;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private RenderingService renderingService;
    private RPGAPI RPGAPI;
    private MapService mapMapService;
    private MovementService movementService;
    private ExplorationService explorationService;
    private FileManager<Configuration, String> configurationFileManager;

    private DefaultMenuServiceEngineImpl() {
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
        this.playerService = DefaultPlayerServiceImpl.getInstance();
        this.characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
        this.characterService = DefaultCharacterServiceImpl.getInstance();
        this.renderingService = DefaultStdoutRenderingImpl.getInstance();
        this.configurationFileManager = DefaultConfigurationFileManagerImpl.getInstance();
        this.mapMapService = DefaultMapServiceImpl.getInstance(configurationFileManager);
        this.movementService = DefaultMovementServiceImpl.getInstance();
        this.explorationService = DefaultExplorationServiceImpl.getInstance(renderingService, movementService);
        this.RPGAPI = DefaultRPGServiceImpl.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService);
    }

    private void saveGame(Optional<Checkpoint> optionalCheckpoint) {
        if(optionalCheckpoint.isEmpty()) {
            renderingService.warn("No character or progress to save");
            return;
        }
        RPGAPI.saveGame(optionalCheckpoint.get());
    }

    private void explore(Map map, Optional<Checkpoint> optionalCheckpoint) throws RPGException {
        if(optionalCheckpoint.isEmpty() || optionalCheckpoint.get().getCharacter() == null) {
            renderingService.warn("Please create a character or resume a saved game to start exploration");
        } else {
            Checkpoint checkpoint = optionalCheckpoint.get();
            RPGAPI.explore(map, checkpoint);
        }
    }

    private Optional<Checkpoint> createCharacter() throws RPGException {
        Character character = RPGAPI.createCharacter();
        Point point = new Point();
        Checkpoint checkpoint = new Checkpoint.Builder().character(character).x(point.x).y(point.y).build();
        return Optional.of(checkpoint);
    }

    private void resumeGame(Optional<Checkpoint> oldCheckpoint) throws MapException, RPGException {
        Optional<Checkpoint> newCheckpoint = RPGAPI.resumeGame();
        if(newCheckpoint.isPresent()) {
            Map map = mapMapService.getDefaultGrid();
            explore(map, newCheckpoint);
            oldCheckpoint = newCheckpoint;
        } else {
            renderingService.warn("No save game available");
        }
    }

    private void deleteGame(Optional<Checkpoint> optionalCheckpoint) throws RPGException {
        if(!RPGAPI.deleteGame()) {
            renderingService.warn("No save game available");
        } else {
            optionalCheckpoint = Optional.empty();
        }
    }

    @Override
    public void play() throws MenuException {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        try {
            Map map = mapMapService.getDefaultGrid();
            while(true) {
                renderingService.menu(PLAY_MENU);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> optionalCheckpoint = createCharacter();
                    case "E" -> explore(map, optionalCheckpoint);
                    case "S" -> saveGame(optionalCheckpoint);
                    case "D" -> deleteGame(optionalCheckpoint);
                    case "R" -> resumeGame(optionalCheckpoint);
                    case "X" -> RPGAPI.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new MenuException(e.getMessage());
        } catch (RPGException | MapException e) {
            renderingService.error("RPG failure: " + e.getMessage());
        }
    }

    private static volatile MenuService INSTANCE;

    public static MenuService getInstance() {
        MenuService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMenuServiceEngineImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultMenuServiceEngineImpl();
            }
            return INSTANCE;
        }
    }

}
