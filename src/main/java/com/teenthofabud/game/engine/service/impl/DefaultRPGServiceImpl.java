package com.teenthofabud.game.engine.service.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.engine.fight.FightException;
import com.teenthofabud.game.engine.fight.FightSimulation;
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
import com.teenthofabud.game.resources.enemy.Enemy;
import com.teenthofabud.game.resources.enemy.EnemyException;
import com.teenthofabud.game.resources.enemy.service.EnemyService;
import com.teenthofabud.game.resources.experience.ExperienceException;
import com.teenthofabud.game.resources.experience.service.ExperienceService;
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

    private BufferedReader stdin;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private FileManager<Checkpoint, Path> checkpointFileManager;
    private RenderingService renderingService;
    private ExplorationService explorationService;
    private MapService mapService;
    private EnemyService enemyService;
    private FightSimulation fightSimulation;
    private ExperienceService experienceService;

    private Map map;
    private Character character;
    private Point currentPosition;
    private Enemy enemy;

    @Deprecated
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
        this.enemyService = enemyService;
        this.fightSimulation = fightSimulation;
        this.experienceService = experienceService;
    }

    private DefaultRPGServiceImpl(BufferedReader stdin,
                                  PlayerService playerService,
                                  CharacterTypeService characterTypeService,
                                  CharacterService characterService,
                                  FileManager<Checkpoint, Path> checkpointFileManager,
                                  RenderingService renderingService,
                                  ExplorationService explorationService,
                                  MapService mapService,
                                  EnemyService enemyService,
                                  FightSimulation fightSimulation,
                                  ExperienceService experienceService) {
        this.stdin = stdin;
        this.playerService = playerService;
        this.characterTypeService = characterTypeService;
        this.characterService = characterService;
        this.checkpointFileManager = checkpointFileManager;
        this.renderingService = renderingService;
        this.explorationService = explorationService;
        this.mapService = mapService;
        this.enemyService = enemyService;
        this.fightSimulation = fightSimulation;
        this.experienceService = experienceService;
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
            experienceService.gain(character, 0);
            renderingService.success("Created character: " + character + " with initial experience " + experienceService.show(character));
        } catch (PlayerException | ExperienceException | CharacterException | CharacterTypeException e) {
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
        try {
            Integer experience = experienceService.show(character);
            renderingService.info("Saving checkpoint....");
            Checkpoint checkpoint = new Checkpoint.Builder()
                    .x(currentPosition.x)
                    .y(currentPosition.y)
                    .character(character)
                    .experience(experience)
                    .build();
            checkpointFileManager.writeData(checkpoint);
            renderingService.success("Checkpoint saved for " + character + " at (" + currentPosition.x + ", " + currentPosition.y + ") on map " + map.getName() + " with experience " + experience);
        } catch (FileManagementException e) {
            renderingService.error(e.getMessage());
        } catch (ExperienceException e) {
            renderingService.warn("No experience available to save!");
        }
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
                experienceService.gain(character, checkpoint.getExperience());
                renderingService.success("Resumed " + checkpoint.getCharacter() + " from checkpoint at (" + checkpoint.x() + ", " + checkpoint.y() + ") on map with experience " + checkpoint.getExperience());
                return true;
            } else {
                renderingService.warn("No checkpoint to resume!");
                return false;
            }
        } catch (FileManagementException | ExperienceException e) {
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
                renderingService.menu(explorationService.menu());
                renderingService.map(map.getMagnitude(), currentPosition.x, currentPosition.y);
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
    public void fight() throws RPGException {
        if(enemy == null) {
            throw new RPGException("Enemy not available");
        }
        if(!experienceService.available(character)) {
            renderingService.error("Fight is missing character");
            return;
        }
        boolean flag = true;
        try {
            fightSimulation.start(character, experienceService.show(character), enemy);
            while(flag) {
                renderingService.menu(fightSimulation.menu());
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "W", "A", "S", "D" -> {
                        boolean result = fightSimulation.act(option.toUpperCase());
                        switch (fightSimulation.status()) {
                            case WIN, LOSE -> {
                                experienceService.gain(character, fightSimulation.experience());
                                flag = false;
                                renderingService.info("Back to game menu....");
                            }
                        }
                    }
                    case "X" -> {
                        fightSimulation.stop();
                        renderingService.info("Back to game menu....");
                        flag = false;
                    }
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (FightException | ExperienceException | RenderingException e) {
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
            this.enemy = enemyService.spawnEnemy();
        } catch (MapException | EnemyException e) {
            throw new RPGException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        this.character = null;
        this.currentPosition = new Point(0, 0);
    }

    private static volatile RPGAPI INSTANCE;

    @Deprecated
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

    public static RPGAPI getInstance(BufferedReader stdin,
                                     PlayerService playerService,
                                     CharacterTypeService characterTypeService,
                                     CharacterService characterService,
                                     FileManager<Checkpoint, Path> checkpointFileManager,
                                     RenderingService renderingService,
                                     ExplorationService explorationService,
                                     MapService mapService,
                                     EnemyService enemyService,
                                     FightSimulation fightSimulation,
                                     ExperienceService experienceService) {
        RPGAPI result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultRPGServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultRPGServiceImpl(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService, explorationService, mapService, enemyService, fightSimulation, experienceService);
            }
            return INSTANCE;
        }
    }

}
