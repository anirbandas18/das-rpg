package com.teenthofabud.game.engine.controller.impl;

import com.teenthofabud.game.engine.renderer.RenderingException;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.controller.MenuService;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.renderer.RenderingService;

import java.io.BufferedReader;
import java.io.IOException;

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
    private RenderingService renderingService;
    private RPGAPI rpgapi;


    private DefaultMenuServiceEngineImpl(BufferedReader bufferedReader,
                                         RenderingService renderingService,
                                         RPGAPI rpgapi) {
        this.stdin = bufferedReader;
        this.renderingService = renderingService;
        this.rpgapi = rpgapi;
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
                        if(rpgapi.resumeGame()) {
                            rpgapi.explore();
                        }
                    }
                    case "X" -> rpgapi.exitGame();
                    default -> renderingService.error("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new MenuException(e.getMessage());
        } catch (RPGException | RenderingException e) {
            renderingService.error("RPG failure: " + e.getMessage());
        }
    }

    private static volatile MenuService INSTANCE;

    public static MenuService getInstance(BufferedReader bufferedReader,
                                          RenderingService renderingService,
                                          RPGAPI rpgapi) {
        MenuService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMenuServiceEngineImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultMenuServiceEngineImpl(bufferedReader, renderingService, rpgapi);
            }
            return INSTANCE;
        }
    }

}
