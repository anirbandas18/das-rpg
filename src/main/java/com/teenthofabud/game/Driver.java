package com.teenthofabud.game;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.controller.MenuService;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.impl.DefaultMenuServiceEngineImpl;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.impl.DefaultRPGServiceImpl;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.persistence.checkpoint.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.persistence.configuration.DefaultConfigurationFileManagerImpl;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.map.service.impl.DefaultMapServiceImpl;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;

public class Driver {

    private RenderingService renderingService;
    private BufferedReader stdin;
    private FileManager<Checkpoint, Path> checkpointFileManager;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private FileManager<Configuration, String> configurationFileManager;
    private MovementService movementService;

    public Driver() {
        this.renderingService = DefaultStdoutRenderingImpl.getInstance();
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
        this.playerService = DefaultPlayerServiceImpl.getInstance();
        this.characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
        this.characterService = DefaultCharacterServiceImpl.getInstance();
        this.configurationFileManager = DefaultConfigurationFileManagerImpl.getInstance();
        this.movementService = DefaultMovementServiceImpl.getInstance();
    }

    public CharacterService getCharacterService() {
        return characterService;
    }

    public CharacterTypeService getCharacterTypeService() {
        return characterTypeService;
    }

    public FileManager<Checkpoint, Path> getCheckpointFileManager() {
        return checkpointFileManager;
    }

    public FileManager<Configuration, String> getConfigurationFileManager() {
        return configurationFileManager;
    }

    public MovementService getMovementService() {
        return movementService;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public RenderingService getRenderingService() {
        return renderingService;
    }

    public BufferedReader getStdin() {
        return stdin;
    }

    public static void main(String[] args) {
        try {
            Driver driver = new Driver();
            MapService mapService = DefaultMapServiceImpl.getInstance(driver.getConfigurationFileManager());
            ExplorationService explorationService = DefaultExplorationServiceImpl.getInstance(driver.getRenderingService(), driver.getMovementService());
            RPGAPI rpgapi = DefaultRPGServiceImpl.getInstance(driver.getStdin(), driver.getPlayerService(), driver.getCharacterTypeService(),
                    driver.getCharacterService(), driver.getCheckpointFileManager(), driver.getRenderingService(), explorationService, mapService);
            MenuService menu = DefaultMenuServiceEngineImpl.getInstance(driver.getStdin(), driver.getRenderingService(), rpgapi);
            menu.play();
        } catch (MenuException e) {
            System.err.println("Driver failure: " + e.getMessage());
        }
    }
}