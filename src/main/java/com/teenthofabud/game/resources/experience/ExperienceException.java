package com.teenthofabud.game.resources.experience;

public class ExperienceException extends Exception {

    private String message;

    public ExperienceException(String message) {
        super("Experience " + message);
    }
}
