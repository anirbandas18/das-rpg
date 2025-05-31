package com.teenthofabud.game.resources;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.DefaultPlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest implements TestDataSourceProvider {

    private static PlayerService PLAYER_SERVICE;

    @BeforeAll
    static void beforeAll() {
        PLAYER_SERVICE = DefaultPlayerServiceImpl.getInstance();
    }

    @Test
    public void testPlayerCreationIsSuccessful() {
        assertDoesNotThrow(() -> {
            Player expectedPlayer = player();
            Player actualPlayer = PLAYER_SERVICE.createPlayer(expectedPlayer.getName());
            Assertions.assertEquals(expectedPlayer, actualPlayer);
        });
    }

    @Test
    public void testPlayerCreationFailed() {
        assertThrowsExactly(PlayerException.class, () -> {
            String expectedPlayerName = "";
            PLAYER_SERVICE.createPlayer(expectedPlayerName);
        });

    }

}
