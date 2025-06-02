package com.teenthofabud.game.engine.controller;

public interface PlayService {

    default String menu() {
        return """
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐          Play           ▌
            ▐=========================▌
            ▐  C - Create character   ▌
            ▐  E - Explore            ▌
            ▐  F - Fight              ▌
            ▐  S - Save game          ▌
            ▐  D - Delete game        ▌
            ▐  R - Resume game        ▌
            ▐  X - Exit game          ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
            """;
    }

    public void play() throws PlayException;

}
