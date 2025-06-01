package com.teenthofabud.game.engine.rpg;

public class RPGException extends Exception {

    private String message;

    public RPGException(String message) {
        super("RPG " + message);
    }
}
