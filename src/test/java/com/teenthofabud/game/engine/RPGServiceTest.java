package com.teenthofabud.game.engine;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.engine.service.RPGAPI;
import com.teenthofabud.game.engine.service.RPGException;
import com.teenthofabud.game.engine.service.impl.DefaultRPGServiceImpl;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.checkpoint.Checkpoint;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.map.MapException;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.player.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class RPGServiceTest implements TestDataSourceProvider {

    private static RPGAPI RPGAPI;
    private static BufferedReader STDIN;
    private static PlayerService PLAYER_SERVICE;
    private static CharacterTypeService CHARACTER_TYPE_SERVICE;
    private static CharacterService CHARACTER_SERVICE;
    private static FileManager<Checkpoint, Path> CHECKPOINT_FILE_MANAGER;
    private static FileManager<Configuration, String> CONFIGURATION_FILE_MANAGER;
    private static RenderingService RENDERING_SERVICE;
    private static MovementService MOVEMENT_SERVICE;
    private static ExplorationService EXPLORATION_SERVICE;
    private static MapService MAP_SERVICE;

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
        reset(CHECKPOINT_FILE_MANAGER);
        reset(CONFIGURATION_FILE_MANAGER);
        reset(MAP_SERVICE);
    }

    @BeforeAll
    static void beforeAll() {
        STDIN = mock(BufferedReader.class);
        CHECKPOINT_FILE_MANAGER = mock(FileManager.class);
        CONFIGURATION_FILE_MANAGER = mock(FileManager.class);
        PLAYER_SERVICE = mock(PlayerService.class);
        CHARACTER_TYPE_SERVICE = mock(CharacterTypeService.class);
        CHARACTER_SERVICE = mock(CharacterService.class);
        MAP_SERVICE = mock(MapService.class);
        RENDERING_SERVICE = DefaultStdoutRenderingImpl.getInstance();
        MOVEMENT_SERVICE = DefaultMovementServiceImpl.getInstance();
        EXPLORATION_SERVICE = DefaultExplorationServiceImpl.getInstance(RENDERING_SERVICE, MOVEMENT_SERVICE);
        RPGAPI = DefaultRPGServiceImpl.getInstance(STDIN, PLAYER_SERVICE, CHARACTER_TYPE_SERVICE, CHARACTER_SERVICE, CHECKPOINT_FILE_MANAGER, RENDERING_SERVICE, EXPLORATION_SERVICE, MAP_SERVICE);
    }

    @Test
    public void testInitIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(MAP_SERVICE.getDefaultGrid()).thenReturn(defaultMap(configuration()));

            RPGAPI.init();

            verify(MAP_SERVICE, times(1)).getDefaultGrid();
        });
    }

    @Test
    public void testInitFailure() {
        assertThrowsExactly(RPGException.class, () -> {
            when(MAP_SERVICE.getDefaultGrid()).thenThrow(new MapException("Simulation"));
            RPGAPI.init();
        });
    }

    @Test
    public void testCreateCharacterIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(STDIN.readLine()).thenReturn(player.getName());
            when(PLAYER_SERVICE.createPlayer(player.getName())).thenReturn(player);
            when(CHARACTER_TYPE_SERVICE.retrieveCharacterType(Math.abs(player.hashCode()))).thenReturn(characterType);
            when(CHARACTER_SERVICE.createCharacter(player, characterType)).thenReturn(character);
            RPGAPI.init();

            RPGAPI.createCharacter();

            verify(STDIN, times(1)).readLine();
            verify(PLAYER_SERVICE, times(1)).createPlayer(player.getName());
            verify(CHARACTER_TYPE_SERVICE, times(1)).retrieveCharacterType(Math.abs(player.hashCode()));
            verify(CHARACTER_SERVICE, times(1)).createCharacter(player, characterType);
        });
    }

    @Test
    public void testCreateCharacterFailure() {
        assertThrowsExactly(RPGException.class, () -> {
            when(STDIN.readLine()).thenThrow(new IOException("Simulation"));
            RPGAPI.init();

            RPGAPI.createCharacter();
        });
    }

    @Test
    public void testResumeGameWhenCheckpointAvailableIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(true);
            when(CHECKPOINT_FILE_MANAGER.readData()).thenReturn(checkpoint);
            RPGAPI.init();

            boolean success = RPGAPI.resumeGame();

            Assertions.assertTrue(success);
            verify(CHECKPOINT_FILE_MANAGER, times(1)).isDataAvailable();
            verify(CHECKPOINT_FILE_MANAGER, times(1)).readData();
        });
    }

    @Test
    public void testResumeGameWhenCheckpointUnavailableIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(false);
            RPGAPI.init();

            boolean success = RPGAPI.resumeGame();

            Assertions.assertFalse(success);
            verify(CHECKPOINT_FILE_MANAGER, times(1)).isDataAvailable();
        });
    }

    @Test
    public void testResumeGameWhenCheckpointIsDirectoryFailure() {
        assertThrowsExactly(RPGException.class, () -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(true);
            when(CHECKPOINT_FILE_MANAGER.readData()).thenThrow(new FileManagementException("Simulation"));
            RPGAPI.init();

            boolean success = RPGAPI.resumeGame();
        });
    }

    @Test
    public void testSaveGame() {
        assertDoesNotThrow(() -> {
            doNothing().when(CHECKPOINT_FILE_MANAGER).writeData(checkpoint);
            when(MAP_SERVICE.getDefaultGrid()).thenReturn(defaultMap(configuration()));
            when(STDIN.readLine()).thenReturn(player.getName());
            when(PLAYER_SERVICE.createPlayer(player.getName())).thenReturn(player);
            when(CHARACTER_TYPE_SERVICE.retrieveCharacterType(Math.abs(player.hashCode()))).thenReturn(characterType);
            when(CHARACTER_SERVICE.createCharacter(player, characterType)).thenReturn(character);
            RPGAPI.init();
            RPGAPI.createCharacter();

            RPGAPI.saveGame();

            verify(CHECKPOINT_FILE_MANAGER, times(1)).writeData(checkpoint);
            verify(MAP_SERVICE, times(1)).getDefaultGrid();
            verify(STDIN, times(1)).readLine();
            verify(PLAYER_SERVICE, times(1)).createPlayer(player.getName());
            verify(CHARACTER_TYPE_SERVICE, times(1)).retrieveCharacterType(Math.abs(player.hashCode()));
            verify(CHARACTER_SERVICE, times(1)).createCharacter(player, characterType);
        });
    }

    @Test
    public void testDeleteGameYesIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(true);
            doNothing().when(CHECKPOINT_FILE_MANAGER).clearData();
            when(STDIN.readLine()).thenReturn("Y");
            RPGAPI.init();

            RPGAPI.deleteGame();

            verify(STDIN, times(1)).readLine();
            verify(CHECKPOINT_FILE_MANAGER, times(1)).isDataAvailable();
            verify(CHECKPOINT_FILE_MANAGER, times(1)).clearData();
        });
    }

    @Test
    public void testDeleteGameNoIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(true);
            doNothing().when(CHECKPOINT_FILE_MANAGER).clearData();
            when(STDIN.readLine()).thenReturn("N");
            RPGAPI.init();

            RPGAPI.deleteGame();

            verify(STDIN, times(1)).readLine();
            verify(CHECKPOINT_FILE_MANAGER, times(1)).isDataAvailable();
            verify(CHECKPOINT_FILE_MANAGER, times(0)).clearData();
        });
    }

    @Test
    public void testDeleteGameNoDataAvailableIsSuccessful() {
        assertDoesNotThrow(() -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(false);
            doNothing().when(CHECKPOINT_FILE_MANAGER).clearData();
            when(STDIN.readLine()).thenReturn("N");
            RPGAPI.init();

            RPGAPI.deleteGame();

            verify(STDIN, times(0)).readLine();
            verify(CHECKPOINT_FILE_MANAGER, times(1)).isDataAvailable();
            verify(CHECKPOINT_FILE_MANAGER, times(0)).clearData();
        });
    }

    @Test
    public void testDeleteGameDataIsDirectoryFailure() {
        assertThrowsExactly(RPGException.class, () -> {
            when(CHECKPOINT_FILE_MANAGER.isDataAvailable()).thenReturn(true);
            doThrow(new FileManagementException("Simulation")).when(CHECKPOINT_FILE_MANAGER).clearData();
            when(STDIN.readLine()).thenReturn("Y");
            RPGAPI.init();

            RPGAPI.deleteGame();
        });
    }

}
