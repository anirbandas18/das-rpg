package com.teenthofabud.game.constants.movement;

public class MovementException extends Exception {

    private String message;

    public MovementException(String message) {
        super("Movement " + message);
    }
}
