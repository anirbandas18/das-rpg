package com.teenthofabud.game.persistence.checkpoint;

import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DefaultCheckpointFileManagerImpl implements FileManager<Checkpoint, Path> {

    private FileSystem fileSystem;

    @Override
    public boolean isDataAvailable() throws FileManagementException {
        return Files.exists(dataFilePath()) && !Files.isDirectory(dataFilePath());
    }

    @Override
    public Checkpoint readData() throws FileManagementException  {
        if(!Files.exists(dataFilePath())) {
            throw new FileManagementException("no save game available");
        }
        if(Files.exists(dataFilePath()) && Files.isDirectory(dataFilePath())) {
            throw new FileManagementException("path " + dataFilePath() + " is a directory");
        }
        try {
            byte[] rawBytes = Files.readAllBytes(dataFilePath());
            Checkpoint checkpoint = (Checkpoint) deserialize(rawBytes);
            return checkpoint;
        } catch (IOException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

    @Override
    public void writeData(Checkpoint data) throws FileManagementException {
        if(data == null) {
            throw new FileManagementException("null data can not be written");
        }
        try {
            if(!Files.exists(dataDirectoryPath())) {
                Files.createDirectories(dataDirectoryPath());
            }
            if(Files.isDirectory(dataFilePath())) {
                throw new FileManagementException("path " + dataFilePath() + " is a directory");
            }
            byte[] rawBytes = serialize(data);
            Files.write(dataFilePath(), rawBytes);
        } catch (IOException e) {
            throw new FileManagementException(e.getMessage());
        }

    }

    @Override
    public void clearData() throws FileManagementException {
        if(!Files.exists(dataFilePath())) {
            throw new FileManagementException("no save game available");
        }
        try {
            Files.delete(dataFilePath());
            Files.delete(dataDirectoryPath());
        } catch (IOException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

    @Override
    public String getFileName() {
        return "checkpoint.data";
    }

    @Override
    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    public Path dataFilePath() {
        return dataDirectoryPath().resolve(getFileName());
    }

    public Path dataDirectoryPath() {
        return getFileSystem().getPath("das-rpg");
    }

    private static volatile FileManager<Checkpoint, Path> INSTANCE;

    private DefaultCheckpointFileManagerImpl(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    private DefaultCheckpointFileManagerImpl() {
        this.fileSystem = FileSystems.getDefault();
    }

    public static FileManager<Checkpoint, Path> getInstance(Optional<FileSystem> fileSystem) {
        FileManager<Checkpoint, Path> result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultCheckpointFileManagerImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = fileSystem.isEmpty() ?
                        new DefaultCheckpointFileManagerImpl()
                        : new DefaultCheckpointFileManagerImpl(fileSystem.get());
            }
            return INSTANCE;
        }
    }

}
