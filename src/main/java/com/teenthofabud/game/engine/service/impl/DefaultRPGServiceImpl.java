package com.teenthofabud.game.engine.service.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
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
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class DefaultRPGServiceImpl implements RPGAPI {

    private static final String MOVEMENT_MENU = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐    Movement Menu    ▌
            ▐=====================▌
            ▐  U - Move up        ▌
            ▐  D - Move down      ▌
            ▐  L - Move left      ▌
            ▐  R - Move right     ▌
            ▐  S - Save position  ▌
            ▐  X - Game menu      ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;

    private BufferedReader stdin;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private FileManager<Checkpoint, Path> checkpointFileManager;
    private RenderingService renderingService;
    private ExplorationService explorationService;

    private DefaultRPGServiceImpl(BufferedReader stdin,
                                  PlayerService playerService,
                                  CharacterTypeService characterTypeService,
                                  CharacterService characterService,
                                  FileManager<Checkpoint, Path> checkpointFileManager,
                                  RenderingService renderingService,
                                  ExplorationService explorationService) {
        this.stdin = stdin;
        this.playerService = playerService;
        this.characterTypeService = characterTypeService;
        this.characterService = characterService;
        this.checkpointFileManager = checkpointFileManager;
        this.renderingService = renderingService;
        this.explorationService = explorationService;
    }

    @Override
    public Character createCharacter() throws RPGException {
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
            throw new RPGException(e.getMessage());
        }
        return character;
    }

    @Override
    public void saveGame(Checkpoint checkpoint) {
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
            renderingService.success("Checkpoint saved for " + checkpoint.getCharacter() + " at (" + checkpoint.x() + ", " + checkpoint.y() + ") on map");
        } else {
            renderingService.info("No checkpoint to save!");
        }
    }

    @Override
    public Optional<Checkpoint> resumeGame() {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        renderingService.info("Resuming checkpoint....");
        try {
            Checkpoint checkpoint = checkpointFileManager.readData();
            renderingService.success("Resumed " + checkpoint.getCharacter() + " from checkpoint at (" + checkpoint.x() + ", " + checkpoint.y() + ") on map");
            optionalCheckpoint = Optional.of(checkpoint);
        } catch (FileManagementException e) {
            renderingService.error(e.getMessage());
        }
        return optionalCheckpoint;
    }

    @Override
    public void deleteGame()  throws RPGException {
        boolean flag = true;
        try {
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
        } catch (IOException e) {
            throw new RPGException(e.getMessage());
        } catch (FileManagementException e) {
            renderingService.error(e.getMessage());
        }
    }

    @Override
    public void exitGame() {
        renderingService.info("Exiting game....");
        System.exit(0);
    }

    @Override
    public void explore(Map map, Checkpoint checkpoint) throws RPGException {
        boolean flag = true;
        try {
            while(flag) {
                renderingService.menu(MOVEMENT_MENU);
                renderingService.grid(map.getMagnitude(), checkpoint.x(), checkpoint.y());
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "U", "D", "L", "R" -> {
                        explorationService.move(map, option.toUpperCase(), checkpoint);
                    }
                    case "X" -> {
                        renderingService.info("Back to game menu....");
                        flag = false;
                    }
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (ExplorationException e) {
            renderingService.error(e.getMessage());
        } catch (IOException e) {
            throw new RPGException(e.getMessage());
        }
    }

    private static volatile RPGAPI INSTANCE;

    public static RPGAPI getInstance(BufferedReader stdin,
                                     PlayerService playerService,
                                     CharacterTypeService characterTypeService,
                                     CharacterService characterService,
                                     FileManager<Checkpoint, Path> checkpointFileManager,
                                     RenderingService renderingService,
                                     ExplorationService explorationService) {
        RPGAPI result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultRPGServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultRPGServiceImpl(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService);
            }
            return INSTANCE;
        }
    }

}
