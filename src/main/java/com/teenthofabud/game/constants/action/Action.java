package com.teenthofabud.game.constants.action;

public enum Action {

    KICK('W', 10),
    SLIDE('A', 15),
    HEAD('S', 5),
    PASS('D', 1);

    private char key;
    private int power;

    private Action(char key, int power) {
        this.key = key;
        this.power = power;
    }

    public char getKey() {
        return key;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
