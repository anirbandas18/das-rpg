package com.teenthofabud.game.constants.charactertype;

public enum CharacterType {

    STRIKER("S"),
    MIDFIELDER("M"),
    DEFENDER("D"),
    REFEREE("R"),
    GOALKEEPER("G");

    private String key;

    private CharacterType(String key) {
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
