package com.teenthofabud.game.resources.map;

public class MapException extends Exception {

    private String message;

    public MapException(String message) {
        super("Map " + message);
    }
}
