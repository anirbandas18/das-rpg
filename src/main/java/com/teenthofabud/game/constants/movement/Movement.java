package com.teenthofabud.game.constants.movement;

public enum Movement {

    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");

    private String key;

    private Movement(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
