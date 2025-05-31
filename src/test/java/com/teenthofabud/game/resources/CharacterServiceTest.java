package com.teenthofabud.game.resources;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.player.service.DefaultPlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CharacterServiceTest {

    private static CharacterService CHARACTER_SERVICE;
    private static CharacterTypeService CHARACTER_TYPE_SERVICE;
    private static PlayerService PLAYER_SERVICE;

    @BeforeAll
    static void beforeAll() {
        CHARACTER_SERVICE = new DefaultCharacterServiceImpl();
        CHARACTER_TYPE_SERVICE = new DefaultCharacterTypeServiceImpl();
        PLAYER_SERVICE = new DefaultPlayerServiceImpl();
    }

    @Test
    public void testCharacterCreationIsSuccessful() {
        assertDoesNotThrow(() -> {
            Player expectedPlayer = PLAYER_SERVICE.createPlayer(UUID.randomUUID().toString());
            CharacterType expectedCharacterType = CHARACTER_TYPE_SERVICE.retrieveCharacterType(Math.abs(expectedPlayer.hashCode()));
            Character actualCharacter = CHARACTER_SERVICE.createCharacter(expectedPlayer, expectedCharacterType);
            Assertions.assertEquals(expectedPlayer, actualCharacter.getPlayer());
            Assertions.assertEquals(expectedCharacterType, actualCharacter.getType());
        });
    }

    @Test
    public void testCharacterCreationWithNullPlayerFailed() {
        assertThrowsExactly(CharacterException.class, () -> {
            Player expectedPlayer = null;
            CharacterType expectedCharacterType = CHARACTER_TYPE_SERVICE.retrieveCharacterType(Math.abs(new Random().nextInt()));
            CHARACTER_SERVICE.createCharacter(expectedPlayer, expectedCharacterType);
        });

    }

    @Test
    public void testCharacterCreationWithNullTypeFailed() {
        assertThrowsExactly(CharacterException.class, () -> {
            Player expectedPlayer = PLAYER_SERVICE.createPlayer(UUID.randomUUID().toString());
            CharacterType expectedCharacterType = null;
            CHARACTER_SERVICE.createCharacter(expectedPlayer, expectedCharacterType);
        });

    }

}
