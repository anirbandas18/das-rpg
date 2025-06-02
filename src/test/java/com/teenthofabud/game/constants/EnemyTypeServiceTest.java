package com.teenthofabud.game.constants;

import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EnemyTypeServiceTest {

    private static CharacterTypeService CHARACTER_TYPE_SERVICE;

    @BeforeAll
    static void beforeAll() {
        CHARACTER_TYPE_SERVICE = DefaultCharacterTypeServiceImpl.getInstance();
    }

    @Test
    public void testCharacterTypeRetrievalByPreSeedIsSuccessful() {
        assertDoesNotThrow(() -> {
            int expectedPreSeed = Math.abs(ThreadLocalRandom.current().nextInt());
            CharacterType actualCharacterType = CHARACTER_TYPE_SERVICE.retrieveCharacterType(expectedPreSeed);
            Assertions.assertNotNull(actualCharacterType);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    public void testCharacterTypeRetrievalByInvalidPreSeedFailed(int expectedPreSeed) {
        assertThrowsExactly(CharacterTypeException.class, () -> {
            CHARACTER_TYPE_SERVICE.retrieveCharacterType(expectedPreSeed);
        });
    }

    @Test
    public void testCharacterTypeRetrievalByKeyIsSuccessful() {
        assertDoesNotThrow(() -> {
            String expectedCharacterTypeKey = "M";
            CharacterType actualCharacterType = CHARACTER_TYPE_SERVICE.retrieveCharacterType(expectedCharacterTypeKey);
            Assertions.assertEquals(expectedCharacterTypeKey, actualCharacterType.getKey());
        });
    }

    @Test
    public void testCharacterTypeRetrievalByInvalidKeyFailed() {
        assertThrowsExactly(CharacterTypeException.class, () -> {
            String expectedCharacterTypeKey = null;
            CHARACTER_TYPE_SERVICE.retrieveCharacterType(expectedCharacterTypeKey);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "Y" })
    public void testCharacterTypeRetrievalByInvalidKeyFailed(String expectedCharacterTypeKey) {
        assertThrowsExactly(CharacterTypeException.class, () -> {
            CHARACTER_TYPE_SERVICE.retrieveCharacterType(expectedCharacterTypeKey);
        });
    }

}
