package com.teenthofabud.game.engine;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.service.impl.DefaultRPGServiceImpl;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.player.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class MenuControllerTest implements TestDataSourceProvider {

    private static RPGAPI MENU_API;
    private static BufferedReader STDIN;
    private static PlayerService PLAYER_SERVICE;
    private static CharacterTypeService CHARACTER_TYPE_SERVICE;
    private static CharacterService CHARACTER_SERVICE;
    private static FileManager<Checkpoint, Path> FILE_MANAGER_SERVICE;
    private static RenderingService RENDERING_SERVICE;
    private static MovementService MOVEMENT_SERVICE;
    private static ExplorationService EXPLORATION_SERVICE;

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
        MOVEMENT_SERVICE = DefaultMovementServiceImpl.getInstance();
        EXPLORATION_SERVICE = DefaultExplorationServiceImpl.getInstance(RENDERING_SERVICE, MOVEMENT_SERVICE);
        MENU_API = DefaultRPGServiceImpl.getInstance(STDIN, PLAYER_SERVICE, CHARACTER_TYPE_SERVICE, CHARACTER_SERVICE, FILE_MANAGER_SERVICE, RENDERING_SERVICE, EXPLORATION_SERVICE);
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
        assertThrowsExactly(RPGException.class, () -> {
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
            when(FILE_MANAGER_SERVICE.isDataAvailable()).thenReturn(true);
            when(FILE_MANAGER_SERVICE.readData()).thenReturn(checkpoint);

            Optional<Checkpoint> actualOptionalCheckpoint = MENU_API.resumeGame();

            Assertions.assertTrue(actualOptionalCheckpoint.isPresent());
            Assertions.assertEquals(checkpoint, actualOptionalCheckpoint.get());
        });
    }

}
