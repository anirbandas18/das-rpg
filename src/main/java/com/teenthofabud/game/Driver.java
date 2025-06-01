package com.teenthofabud.game;

import com.teenthofabud.game.engine.rpg.RPGService;
import com.teenthofabud.game.engine.rpg.RPGException;
import com.teenthofabud.game.engine.rpg.impl.DefaultRPGServiceEngineImpl;

public class Driver {

    public static void main(String[] args) {
        try {
            RPGService game = DefaultRPGServiceEngineImpl.getInstance();
            game.play();
        } catch (RPGException e) {
            System.err.println("Driver failure: " + e.getMessage());
        }
    }
}