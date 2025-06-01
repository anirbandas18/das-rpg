package com.teenthofabud.game.engine.exploration;

public class ExplorationException extends Exception {

    private String message;

    public ExplorationException(String message) {
        super("Exploration " + message);
    }
}
