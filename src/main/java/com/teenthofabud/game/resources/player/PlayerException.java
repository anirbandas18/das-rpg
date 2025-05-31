package com.teenthofabud.game.resources.player;

public class PlayerException extends Exception {

    private String message;

    public PlayerException(String message) {
        super("Player " + message);
    }
}
