package com.teenthofabud.game.controller;

public class MainMenuException extends Exception {

    private String message;

    public MainMenuException(String message) {
        super("Main menu " + message);
    }
}
