package com.teenthofabud.game.engine.controller;

public class MenuException extends Exception {

    public MenuException(String message) {
        super("Menu " + message);
    }
}
