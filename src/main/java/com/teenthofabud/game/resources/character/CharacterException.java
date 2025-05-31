package com.teenthofabud.game.resources.character;

public class CharacterException extends Exception {

    private String message;

    public CharacterException(String message) {
        super("Character " + message);
    }
}
