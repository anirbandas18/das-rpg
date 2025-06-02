package com.teenthofabud.game.resources.enemy;

public class EnemyException extends Exception {

    private String message;

    public EnemyException(String message) {
        super("Enemy " + message);
    }
}
