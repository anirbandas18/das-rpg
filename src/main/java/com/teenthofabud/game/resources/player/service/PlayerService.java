package com.teenthofabud.game.resources.player.service;

import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;

public interface PlayerService {

    public Player createPlayer(String name) throws PlayerException;

}
