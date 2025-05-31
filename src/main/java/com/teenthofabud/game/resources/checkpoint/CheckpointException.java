package com.teenthofabud.game.resources.checkpoint;

public class CheckpointException extends Exception {

    private String message;

    public CheckpointException(String message) {
        super("Checkpoint " + message);
    }
}
