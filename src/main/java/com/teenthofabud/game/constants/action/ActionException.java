package com.teenthofabud.game.constants.action;

public class ActionException extends Exception {

    private String message;

    public ActionException(String message) {
        super("Action " + message);
    }
}
