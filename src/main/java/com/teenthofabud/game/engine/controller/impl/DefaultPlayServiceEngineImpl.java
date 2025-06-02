package com.teenthofabud.game.engine.controller.impl;

import com.teenthofabud.game.engine.controller.PlayService;
import com.teenthofabud.game.engine.renderer.RenderingException;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.controller.PlayException;
import com.teenthofabud.game.engine.renderer.RenderingService;

import java.io.BufferedReader;
import java.io.IOException;

public class DefaultPlayServiceEngineImpl implements PlayService {

    private BufferedReader stdin;
    private RenderingService renderingService;
    private RPGAPI rpgapi;


    private DefaultPlayServiceEngineImpl(BufferedReader bufferedReader,
                                         RenderingService renderingService,
                                         RPGAPI rpgapi) {
        this.stdin = bufferedReader;
        this.renderingService = renderingService;
        this.rpgapi = rpgapi;
    }

    @Override
    public void play() throws PlayException {
        try {
            rpgapi.init();
            while(true) {
                renderingService.menu(menu());
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> rpgapi.createCharacter();
                    case "E" -> rpgapi.explore();
                    case "F" -> rpgapi.fight();
                    case "S" -> rpgapi.saveGame();
                    case "D" -> rpgapi.deleteGame();
                    case "R" -> rpgapi.resumeGame();
                    case "X" -> rpgapi.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new PlayException(e.getMessage());
        } catch (RPGException | RenderingException e) {
            renderingService.error("Menu failure: " + e.getMessage());
        }
    }

    private static volatile PlayService INSTANCE;

    public static PlayService getInstance(BufferedReader bufferedReader,
                                          RenderingService renderingService,
                                          RPGAPI rpgapi) {
        PlayService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultPlayServiceEngineImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultPlayServiceEngineImpl(bufferedReader, renderingService, rpgapi);
            }
            return INSTANCE;
        }
    }

}
