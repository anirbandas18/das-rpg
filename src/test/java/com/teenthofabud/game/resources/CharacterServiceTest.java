package com.teenthofabud.game.resources;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CharacterServiceTest implements TestDataSourceProvider {

    private static CharacterService CHARACTER_SERVICE;
    private static CharacterTypeService CHARACTER_TYPE_SERVICE;

    @BeforeAll
    static void beforeAll() {
        CHARACTER_SERVICE = DefaultCharacterServiceImpl.getInstance();
        CHARACTER_TYPE_SERVICE = DefaultCharacterTypeServiceImpl.getInstance();
    }

    @Test
    public void testCharacterCreationIsSuccessful() {
        assertDoesNotThrow(() -> {
            Player expectedPlayer = player();
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
            Player expectedPlayer = player();
            CharacterType expectedCharacterType = null;
            CHARACTER_SERVICE.createCharacter(expectedPlayer, expectedCharacterType);
        });

    }

}
