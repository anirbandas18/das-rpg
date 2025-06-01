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
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.map.service.impl.DefaultMapServiceImpl;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

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
    private RPGAPI rpgapi;
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
        this.rpgapi = DefaultRPGServiceImpl.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService, mapMapService);
    }

    @Override
    public void play() throws MenuException {
        try {
            rpgapi.init();
            while(true) {
                renderingService.menu(PLAY_MENU);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> rpgapi.createCharacter();
                    case "E" -> rpgapi.explore();
                    case "S" -> rpgapi.saveGame();
                    case "D" -> rpgapi.deleteGame();
                    case "R" -> {
                        rpgapi.resumeGame();
                        rpgapi.explore();
                    }
                    case "X" -> rpgapi.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new MenuException(e.getMessage());
        } catch (RPGException e) {
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
