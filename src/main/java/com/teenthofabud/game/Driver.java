package com.teenthofabud.game;

import com.teenthofabud.game.engine.RPG;
import com.teenthofabud.game.engine.RPGException;
import com.teenthofabud.game.engine.impl.DefaultRPGEngineImpl;

public class Driver {

    public static void main(String[] args) {
        try {
            RPG game = DefaultRPGEngineImpl.getInstance();
            game.play();
        } catch (RPGException e) {
            System.err.println("Driver failure: " + e.getMessage());
        }
    }
}