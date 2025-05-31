package com.teenthofabud.game.constants.charactertype;

public class CharacterTypeException extends Exception {

    private String message;

    public CharacterTypeException(String message) {
        super("Character type " + message);
    }
}
