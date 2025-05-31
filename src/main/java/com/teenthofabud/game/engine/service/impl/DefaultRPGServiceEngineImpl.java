package com.teenthofabud.game.engine.service.impl;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.engine.controller.MenuAPI;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.impl.DefaultMenuControllerImpl;
import com.teenthofabud.game.engine.service.RPGService;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.persistence.repository.impl.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class DefaultRPGServiceEngineImpl implements RPGService {

    private static final String GAME_MENU = """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐       Game Menu        ▌
            ▐========================▌
            ▐  C - Create character  ▌
            ▐  S - Save game         ▌
            ▐  R - Resume game       ▌
            ▐  X - Exit game         ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;

    private BufferedReader stdin;
    private FileManager<Checkpoint> checkpointFileManager;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private RenderingService renderingService;
    private MenuAPI menuAPI;

    private DefaultRPGServiceEngineImpl() {
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
        this.playerService = DefaultPlayerServiceImpl.getInstance();
        this.characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
        this.characterService = DefaultCharacterServiceImpl.getInstance();
        this.renderingService = DefaultStdoutRenderingImpl.getInstance();
        this.menuAPI = DefaultMenuControllerImpl.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService);
    }

    @Override
    public void play() throws RPGException {
        try {
            Character character = null;
            Checkpoint checkpoint = null;
            while(true) {
                renderingService.menu(GAME_MENU);
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> {
                        character = menuAPI.createCharacter();
                    }
                    case "S" -> {
                        checkpoint = new Checkpoint.Builder().character(character).build();
                        menuAPI.saveGame(checkpoint);
                    }
                    case "R" -> {
                        Optional<Checkpoint> optionalCheckpoint = menuAPI.resumeGame();
                        if(optionalCheckpoint.isPresent()) {
                            checkpoint = optionalCheckpoint.get();
                        }
                    }
                    case "X" -> menuAPI.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new RPGException(e.getClass().getSimpleName() + ": " + e);
        } catch (MenuException e) {
            System.err.println("Driver failure: " + e.getMessage());
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
