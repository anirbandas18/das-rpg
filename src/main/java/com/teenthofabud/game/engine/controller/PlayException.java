package com.teenthofabud.game.engine.controller;

public class PlayException extends Exception {

    public PlayException(String message) {
        super("Play " + message);
    }
}
