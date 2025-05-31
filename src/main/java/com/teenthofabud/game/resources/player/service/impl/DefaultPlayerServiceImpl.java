package com.teenthofabud.game.resources.player.service.impl;

import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;

public class DefaultPlayerServiceImpl implements PlayerService {

    @Override
    public Player createPlayer(String name) throws PlayerException {
        if(name == null || name.length() == 0) {
            throw new PlayerException("name is required");
        }
        Player player = new Player.Builder().name(name).build();
        return player;
    }

    private static volatile PlayerService INSTANCE;

    private DefaultPlayerServiceImpl() {

    }

    public static PlayerService getInstance() {
        PlayerService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultPlayerServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultPlayerServiceImpl();
            }
            return INSTANCE;
        }
    }
}
