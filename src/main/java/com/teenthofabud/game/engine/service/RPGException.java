package com.teenthofabud.game.engine.service;

public class RPGException extends Exception {

    private String message;

    public RPGException(String message) {
        super("RPG " + message);
    }
}
