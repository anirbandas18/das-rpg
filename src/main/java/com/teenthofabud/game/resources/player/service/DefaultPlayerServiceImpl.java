package com.teenthofabud.game.resources.player.service;

import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;

public class DefaultPlayerServiceImpl implements PlayerService {

    @Override
    public Player createPlayer(String name) throws PlayerException {
        if(name == null || name.length() == 0) {
            throw new PlayerException("name is required");
        }
        Player player = new Player.Builder().name(name).build();
        return player;
    }

    private static volatile PlayerService instance;

    private DefaultPlayerServiceImpl() {

    }

    public static PlayerService getInstance() {
        PlayerService result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultPlayerServiceImpl.class) {
            if (instance == null) {
                instance = new DefaultPlayerServiceImpl();
            }
            return instance;
        }
    }
}
