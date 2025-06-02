package com.teenthofabud.game.engine.fight;

public class FightException extends Exception {

    private String message;

    public FightException(String message) {
        super("Fight " + message);
    }
}
