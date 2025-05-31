package com.teenthofabud.game.engine.controller;

public class MenuException extends Exception {

    private String message;

    public MenuException(String message) {
        super("Main menu " + message);
    }
}
