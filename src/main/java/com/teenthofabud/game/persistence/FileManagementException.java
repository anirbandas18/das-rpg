package com.teenthofabud.game.persistence;

public class FileManagementException extends Exception {

    private String message;

    public FileManagementException(String message) {
        super("File management " + message);
    }
}
