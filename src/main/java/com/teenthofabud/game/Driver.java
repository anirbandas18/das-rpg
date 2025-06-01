package com.teenthofabud.game;

import com.teenthofabud.game.engine.controller.MenuService;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.impl.DefaultMenuServiceEngineImpl;

public class Driver {

    public static void main(String[] args) {
        try {
            MenuService menu = DefaultMenuServiceEngineImpl.getInstance();
            menu.play();
        } catch (MenuException e) {
            System.err.println("Driver failure: " + e.getMessage());
        }
    }
}