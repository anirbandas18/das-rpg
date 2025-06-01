package com.teenthofabud.game.engine.renderer;

public class RenderingException extends Exception {

    private String message;

    public RenderingException(String message) {
        super("RPG " + message);
    }
}
