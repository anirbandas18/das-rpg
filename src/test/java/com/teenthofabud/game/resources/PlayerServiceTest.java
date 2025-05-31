package com.teenthofabud.game.resources;

import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.player.service.DefaultPlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    private static PlayerService PLAYER_SERVICE;

    @BeforeAll
    static void beforeAll() {
        PLAYER_SERVICE = new DefaultPlayerServiceImpl();
    }

    @Test
    public void testPlayerCreationIsSuccessful() {
        assertDoesNotThrow(() -> {
            String expectedPlayerName = UUID.randomUUID().toString();
            Player actualPlayer = PLAYER_SERVICE.createPlayer(expectedPlayerName);
            Assertions.assertEquals(expectedPlayerName, actualPlayer.getName());
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
