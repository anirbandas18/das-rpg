package com.teenthofabud.game.engine.rpg.impl;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.controller.MenuAPI;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.impl.DefaultMenuControllerImpl;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.rpg.RPGService;
import com.teenthofabud.game.engine.rpg.RPGException;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.persistence.repository.impl.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.map.service.impl.DefaultMapServiceImpl;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class DefaultRPGServiceEngineImpl implements RPGService {

    private static final String GAME_MENU = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐        Game Menu        ▌
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
    private FileManager<Checkpoint> checkpointFileManager;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private RenderingService renderingService;
    private MenuAPI menuAPI;
    private MapService mapMapService;
    private MovementService movementService;
    private ExplorationService explorationService;

    private DefaultRPGServiceEngineImpl() {
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
        this.playerService = DefaultPlayerServiceImpl.getInstance();
        this.characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
        this.characterService = DefaultCharacterServiceImpl.getInstance();
        this.renderingService = DefaultStdoutRenderingImpl.getInstance();
        this.mapMapService = DefaultMapServiceImpl.getInstance(renderingService);
        this.movementService = DefaultMovementServiceImpl.getInstance();
        this.explorationService = DefaultExplorationServiceImpl.getInstance(renderingService, movementService);
        this.menuAPI = DefaultMenuControllerImpl.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService);
    }

    @Deprecated
    private void save(Character character) {
        Checkpoint checkpoint = new Checkpoint.Builder().character(character).build();
        menuAPI.saveGame(checkpoint);
    }

    private void save(Character character, Point point) {
        Checkpoint checkpoint = new Checkpoint.Builder().character(character).x(point.x).y(point.y).build();
        menuAPI.saveGame(checkpoint);
    }

    private void exploration(Map map, Optional<Checkpoint> optionalCheckpoint) throws MenuException {
        if(optionalCheckpoint.isEmpty() || optionalCheckpoint.get().getCharacter() == null) {
            renderingService.warn("Please create a character or resume a saved game to start exploration");
        } else {
            Checkpoint checkpoint = optionalCheckpoint.get();
            menuAPI.explore(map, checkpoint);
        }
    }

    @Override
    public void play() throws RPGException {
        Character character = null;
        Point point = new Point();
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        Map map = mapMapService.get10xGrid();
        try {
            while(true) {
                renderingService.menu(GAME_MENU);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> character = menuAPI.createCharacter();
                    case "E" -> exploration(map, optionalCheckpoint);
                    case "S" -> save(character, point);
                    case "D" -> menuAPI.deleteGame();
                    case "R" -> optionalCheckpoint = menuAPI.resumeGame();
                    case "X" -> menuAPI.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new RPGException(e.getClass().getSimpleName() + ": " + e);
        } catch (MenuException e) {
            renderingService.error("Driver failure: " + e.getMessage());
        }
    }

    private static volatile RPGService INSTANCE;

    public static RPGService getInstance() {
        RPGService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultRPGServiceEngineImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultRPGServiceEngineImpl();
            }
            return INSTANCE;
        }
    }

}
