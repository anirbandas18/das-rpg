package com.teenthofabud.game;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.controller.MainMenuException;
import com.teenthofabud.game.controller.MainMenuAPI;
import com.teenthofabud.game.controller.impl.DefaultMainMenuController;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.persistence.repository.impl.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.renderer.RenderingService;
import com.teenthofabud.game.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.player.service.impl.DefaultPlayerServiceImpl;

import java.io.*;
import java.util.Optional;

public class RPG {

    private BufferedReader stdin;
    private FileManager<Checkpoint> checkpointFileManager;
    private PlayerService playerService;
    private CharacterTypeService characterTypeService;
    private CharacterService characterService;
    private RenderingService renderingService;
    private MainMenuAPI mainMenuAPI;

    private RPG() {
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
        this.playerService = DefaultPlayerServiceImpl.getInstance();
        this.characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
        this.characterService = DefaultCharacterServiceImpl.getInstance();
        this.renderingService = DefaultStdoutRenderingImpl.getInstance();
        this.mainMenuAPI = DefaultMainMenuController.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager, renderingService);
    }

    public void play() throws IOException, MainMenuException {
        Character character = null;
        Checkpoint checkpoint = null;
        while(true) {
            System.out.print(mainMenuAPI.getOptions());
            String option = stdin.readLine();
            switch (option.toUpperCase()) {
                case "C" -> {
                    character = mainMenuAPI.createCharacter();
                }
                case "S" -> {
                    checkpoint = new Checkpoint.Builder().character(character).build();
                    mainMenuAPI.saveGame(checkpoint);
                }
                case "R" -> {
                    Optional<Checkpoint> optionalCheckpoint = mainMenuAPI.resumeGame();
                    if(optionalCheckpoint.isPresent()) {
                        checkpoint = optionalCheckpoint.get();
                    }
                }
                case "X" -> mainMenuAPI.exitGame();
                default -> renderingService.error("Option " + option + " not supported. Try again!");
            }
        }
    }

    private static volatile RPG INSTANCE;

    public static RPG getInstance() {
        RPG result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(RPG.class) {
            if (INSTANCE == null) {
                INSTANCE = new RPG();
            }
            return INSTANCE;
        }
    }

    public static void main(String[] args) {
        try {
            RPG rpgGame = RPG.getInstance();
            rpgGame.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MainMenuException e) {
            System.err.println("RPG failure: " + e.getMessage());
        }
    }
}