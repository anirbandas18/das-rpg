package com.teenthofabud.game.engine;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.engine.controller.MenuAPI;
import com.teenthofabud.game.engine.controller.MenuException;
import com.teenthofabud.game.engine.controller.impl.DefaultMenuControllerImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class MenuControllerTest implements TestDataSourceProvider {

    private static MenuAPI MENU_API;
    private static BufferedReader STDIN;
    private static PlayerService PLAYER_SERVICE;
    private static CharacterTypeService CHARACTER_TYPE_SERVICE;
    private static CharacterService CHARACTER_SERVICE;
    private static FileManager<Checkpoint> FILE_MANAGER_SERVICE;
    private static RenderingService RENDERING_SERVICE;

    private String playerName;
    private Player player;
    private CharacterType characterType;
    private Character character;
    private Checkpoint checkpoint;

    @BeforeEach
    void setUp() {
        playerName = UUID.randomUUID().toString();
        player = player(playerName);
        characterType = CharacterType.MIDFIELDER;
        character = character(player, characterType);
        checkpoint = checkpoint(character);
        reset(STDIN);
        reset(PLAYER_SERVICE);
        reset(CHARACTER_TYPE_SERVICE);
        reset(CHARACTER_SERVICE);
        reset(FILE_MANAGER_SERVICE);
    }

    @BeforeAll
    static void beforeAll() {
        STDIN = mock(BufferedReader.class);
        FILE_MANAGER_SERVICE = mock(FileManager.class);
        PLAYER_SERVICE = mock(PlayerService.class);
        CHARACTER_TYPE_SERVICE = mock(CharacterTypeService.class);
        CHARACTER_SERVICE = mock(CharacterService.class);
        RENDERING_SERVICE = DefaultStdoutRenderingImpl.getInstance();
        MENU_API = DefaultMenuControllerImpl.getInstance(STDIN, PLAYER_SERVICE, CHARACTER_TYPE_SERVICE, CHARACTER_SERVICE, FILE_MANAGER_SERVICE, RENDERING_SERVICE);
    }

    @Test
    public void testCreateCharacterIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(STDIN.readLine()).thenReturn(playerName);
            when(PLAYER_SERVICE.createPlayer(playerName)).thenReturn(player);
            when(CHARACTER_TYPE_SERVICE.retrieveCharacterType(Math.abs(player.hashCode()))).thenReturn(characterType);
            when(CHARACTER_SERVICE.createCharacter(player, characterType)).thenReturn(character);

            Character actualCharacter = MENU_API.createCharacter();

            Assertions.assertEquals(character, actualCharacter);
        });
    }

    @Test
    public void testCreateCharacterSystemFailure() {
        assertThrowsExactly(MenuException.class, () -> {
            when(STDIN.readLine()).thenThrow(new IOException("STDIN Simulation"));
            MENU_API.createCharacter();
        });
    }

    @Test
    public void testCreateCharacterBusinessFailure() {
        assertDoesNotThrow(() -> {
            when(STDIN.readLine()).thenReturn(playerName);
            when(PLAYER_SERVICE.createPlayer(playerName)).thenThrow(new PlayerException("Simulation"));

            Character actualCharacter = MENU_API.createCharacter();

            Assertions.assertNull(actualCharacter);
        });
    }

    @Test
    public void testSaveGameIsSuccessful() {
        assertDoesNotThrow(() -> {
            doNothing().when(FILE_MANAGER_SERVICE).writeData(checkpoint);
            MENU_API.saveGame(checkpoint);
        });
    }

    @Test
    public void testResumeGameIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(FILE_MANAGER_SERVICE.readData()).thenReturn(Optional.of(checkpoint));

            Optional<Checkpoint> actualOptionalCheckpoint = MENU_API.resumeGame();

            Assertions.assertTrue(actualOptionalCheckpoint.isPresent());
            Assertions.assertEquals(checkpoint, actualOptionalCheckpoint.get());
        });
    }

}
