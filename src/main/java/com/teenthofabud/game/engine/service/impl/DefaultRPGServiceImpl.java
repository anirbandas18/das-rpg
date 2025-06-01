package com.teenthofabud.game.engine.service.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.engine.renderer.RenderingException;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.exploration.ExplorationException;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.MapException;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

public class DefaultRPGServiceImpl implements RPGAPI {

    private static final String EXPLORE_MENU = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐       Explore       ▌
            ▐=====================▌
            ▐  U - Move up        ▌
            ▐  D - Move down      ▌
            ▐  L - Move left      ▌
            ▐  R - Move right     ▌
            ▐  S - Save position  ▌
            ▐  X - Back           ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;

    private BufferedReader stdin;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private FileManager<Checkpoint, Path> checkpointFileManager;
    private RenderingService renderingService;
    private ExplorationService explorationService;
    private MapService mapService;

    private Map map;
    private Character character;
    private Point currentPosition;

    private DefaultRPGServiceImpl(BufferedReader stdin,
                                  PlayerService playerService,
                                  CharacterTypeService characterTypeService,
                                  CharacterService characterService,
                                  FileManager<Checkpoint, Path> checkpointFileManager,
                                  RenderingService renderingService,
                                  ExplorationService explorationService,
                                  MapService mapService) {
        this.stdin = stdin;
        this.playerService = playerService;
        this.characterTypeService = characterTypeService;
        this.characterService = characterService;
        this.checkpointFileManager = checkpointFileManager;
        this.renderingService = renderingService;
        this.explorationService = explorationService;
        this.mapService = mapService;
    }

    @Override
    public void createCharacter() throws RPGException {
        try {
            renderingService.info("Enter player name: ");
            Player player = playerService.createPlayer(stdin.readLine());
            CharacterType type = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));
            character = characterService.createCharacter(player, type);
            currentPosition.x = 0;
            currentPosition.y = 0;
            renderingService.success("Created character: " + character);
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            renderingService.error(e.getMessage());
        } catch (IOException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void saveGame() throws RPGException {
        if(character == null) {
            renderingService.warn("No character available to save!");
            return;
        }
        if(currentPosition == null) {
            renderingService.warn("No position available to save!");
            return;
        }
        if(currentPosition.x < 0 || currentPosition.y < 0) {
            throw new RPGException("invalid position");
        }
        renderingService.info("Saving checkpoint....");
        try {
            Checkpoint checkpoint = new Checkpoint.Builder().x(currentPosition.x).y(currentPosition.y).character(character).build();
            checkpointFileManager.writeData(checkpoint);
        } catch (FileManagementException e) {
            renderingService.error(e.getMessage());
        }
        renderingService.success("Checkpoint saved for " + character + " at (" + currentPosition.x + ", " + currentPosition.y + ") on map " + map.getName());
    }

    @Override
    public boolean resumeGame() throws RPGException {
        try {
            if(checkpointFileManager.isDataAvailable()) {
                renderingService.info("Resuming checkpoint....");
                Checkpoint checkpoint = checkpointFileManager.readData();
                character = checkpoint.getCharacter();
                currentPosition.x = checkpoint.x();
                currentPosition.y = checkpoint.y();
                renderingService.success("Resumed " + checkpoint.getCharacter() + " from checkpoint at (" + checkpoint.x() + ", " + checkpoint.y() + ") on map");
                return true;
            } else {
                renderingService.warn("No checkpoint to resume!");
                return false;
            }
        } catch (FileManagementException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void deleteGame() throws RPGException {
        boolean flag = true;
        try {
            if(checkpointFileManager.isDataAvailable()) {
                while (flag) {
                    renderingService.warn("Are you sure you want to delete saved game? Y/N");
                    String option = stdin.readLine();
                    switch (option.toUpperCase()) {
                        case "Y" -> {
                            checkpointFileManager.clearData();
                            renderingService.warn("Checkpoint deleted!");
                            flag = false;
                        }
                        case "N" -> flag = false;
                        default -> renderingService.error("Option " + option + " not supported. Try again!");
                    }
                }
            } else {
                renderingService.warn("No checkpoint to delete!");
            }
        } catch (IOException | FileManagementException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void exitGame() {
        renderingService.info("Exiting game....");
        System.exit(0);
    }

    @Override
    public void explore() throws RPGException {
        if(map == null) {
            throw new RPGException("Map not available");
        }
        boolean flag = true;
        try {
            explorationService.init(character, currentPosition, map);
            while(flag) {
                renderingService.menu(EXPLORE_MENU);
                renderingService.grid(map.getMagnitude(), currentPosition.x, currentPosition.y);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "U", "D", "L", "R" -> {
                        explorationService.move(option.toUpperCase());
                        currentPosition = explorationService.checkpoint();
                    }
                    case "S" -> {
                        saveGame();
                    }
                    case "X" -> {
                        explorationService.clear();
                        renderingService.info("Back to game menu....");
                        flag = false;
                    }
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (ExplorationException | RenderingException e) {
            renderingService.error(e.getMessage());
        } catch (IOException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void init() throws RPGException {
        try {
            this.map = mapService.getDefaultGrid();
            this.currentPosition = new Point(0, 0);
        } catch (MapException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        this.character = null;
        this.currentPosition = null;
    }

    private static volatile RPGAPI INSTANCE;

    public static RPGAPI getInstance(BufferedReader stdin,
                                     PlayerService playerService,
                                     CharacterTypeService characterTypeService,
                                     CharacterService characterService,
                                     FileManager<Checkpoint, Path> checkpointFileManager,
                                     RenderingService renderingService,
                                     ExplorationService explorationService,
                                     MapService mapService) {
        RPGAPI result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultRPGServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultRPGServiceImpl(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService, mapService);
            }
            return INSTANCE;
        }
    }

}
