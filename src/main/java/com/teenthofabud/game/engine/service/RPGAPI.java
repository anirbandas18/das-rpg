package com.teenthofabud.game.engine.service;

public interface RPGAPI {

    public void init() throws RPGException;

    public void clear();

    public void createCharacter() throws RPGException;

    public void explore() throws RPGException;

    public void resumeGame() throws RPGException;

    public void saveGame() throws RPGException;

    public void deleteGame() throws RPGException;

    public void exitGame();

}
