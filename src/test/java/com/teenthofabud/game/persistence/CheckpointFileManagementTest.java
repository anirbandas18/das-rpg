package com.teenthofabud.game.persistence;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.persistence.repository.impl.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.persistence.repository.FileManager;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CheckpointFileManagementTest implements TestDataSourceProvider {

    private static FileManager<Checkpoint> CHECKPOINT_FILE_MANAGER;
    private static FileSystem FILE_SYSTEM;

    private Checkpoint checkpoint;

    private void cleanResources(Path path) throws IOException {
        if(Files.exists(path)) {
            Files.delete(path);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            cleanResources(CHECKPOINT_FILE_MANAGER.dataFilePath());
            cleanResources(CHECKPOINT_FILE_MANAGER.dataDirectoryPath());
        } catch (IOException e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        Character character = new Character.Builder().player(player()).type(CharacterType.MIDFIELDER).build();
        checkpoint = new Checkpoint.Builder().character(character).build();
    }

    @BeforeAll
    static void beforeAll() {
        FILE_SYSTEM = Jimfs.newFileSystem(Configuration.unix());
        CHECKPOINT_FILE_MANAGER = DefaultCheckpointFileManagerImpl.getInstance(Optional.of(FILE_SYSTEM));
    }

    @AfterAll
    static void afterAll() {
        try {
            FILE_SYSTEM.close();
        } catch (IOException e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @Test
    public void testFileNameIsProvided() {
        Assertions.assertNotNull(CHECKPOINT_FILE_MANAGER.getFileName());
        Assertions.assertTrue(CHECKPOINT_FILE_MANAGER.getFileName().length() != 0);
    }

    @Test
    public void testFileSystemIsProvided() {
        Assertions.assertNotNull(CHECKPOINT_FILE_MANAGER.getFileSystem());
    }

    @Test
    public void testReadDataWhenNoSaveAvailableIsSuccessful() {
        assertDoesNotThrow(() -> {
            Optional<Checkpoint> optionalCheckpoint = CHECKPOINT_FILE_MANAGER.readData();
            Assertions.assertTrue(optionalCheckpoint.isEmpty());
        });
    }

    @Test
    public void testReadDataWhenSaveLocationIsDirectoryFailure() {
        assertThrowsExactly(FileManagementException.class, () -> {
            Files.createDirectories(CHECKPOINT_FILE_MANAGER.dataFilePath());
            CHECKPOINT_FILE_MANAGER.readData();
        });
    }

    @Test
    public void testReadDataIsSuccessful() {
        assertDoesNotThrow(() -> {
            Files.createDirectories(CHECKPOINT_FILE_MANAGER.dataDirectoryPath());
            CHECKPOINT_FILE_MANAGER.writeData(checkpoint);
            Optional<Checkpoint> optionalCheckpoint = CHECKPOINT_FILE_MANAGER.readData();
            Assertions.assertTrue(optionalCheckpoint.isPresent());
            Assertions.assertEquals(checkpoint.getCharacter(), optionalCheckpoint.get().getCharacter());
        });
    }

    @Test
    public void testWriteDataWithNullCheckpointFailure() {
        assertThrowsExactly(FileManagementException.class, () -> {
            CHECKPOINT_FILE_MANAGER.writeData(null);
        });
    }

    @Test
    public void testWriteDataWhenSaveNotAvailableIsSuccessful() {
        assertDoesNotThrow(() -> {
            CHECKPOINT_FILE_MANAGER.writeData(checkpoint);
        });
    }

    @Test
    public void testWriteDataWhenSaveLocationIsDirectoryFailure() {
        assertThrowsExactly(FileManagementException.class, () -> {
            Files.createDirectories(CHECKPOINT_FILE_MANAGER.dataFilePath());
            CHECKPOINT_FILE_MANAGER.writeData(checkpoint);
        });
    }

    @Test
    public void testWriteDataOverwriteIsSuccessful() {
        assertDoesNotThrow(() -> {
            CHECKPOINT_FILE_MANAGER.writeData(checkpoint);
            Checkpoint newCheckpoint = checkpoint(character(player(), CharacterType.GOALKEEPER));
            CHECKPOINT_FILE_MANAGER.writeData(newCheckpoint);
        });
    }

}
