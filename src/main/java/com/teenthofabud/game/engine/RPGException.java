package com.teenthofabud.game.engine;

public class RPGException extends Exception {

    private String message;

    public RPGException(String message) {
        super("Driver " + message);
    }
}
