package com.teenthofabud.game.persistence.impl;

import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Optional;

public class DefaultCheckpointFileManagerImpl implements FileManager<Checkpoint> {

    private FileSystem fileSystem;

    @Override
    public Optional<Checkpoint> readData() throws FileManagementException  {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        if(!Files.exists(dataFilePath())) {
            return optionalCheckpoint;
        }
        if(Files.exists(dataFilePath()) && Files.isDirectory(dataFilePath())) {
            throw new FileManagementException("path " + dataFilePath() + " is a directory");
        }
        try {
            byte[] rawBytes = Files.readAllBytes(dataFilePath());
            Checkpoint checkpoint = (Checkpoint) deserialize(rawBytes);
            optionalCheckpoint = Optional.of(checkpoint);
            return optionalCheckpoint;
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
    public String getFileName() {
        return "checkpoint.data";
    }

    @Override
    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    private static volatile FileManager<Checkpoint> instance;

    private DefaultCheckpointFileManagerImpl(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    private DefaultCheckpointFileManagerImpl() {
        this.fileSystem = FileSystems.getDefault();
    }

    public static FileManager<Checkpoint> getInstance(Optional<FileSystem> fileSystem) {
        FileManager<Checkpoint> result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultCheckpointFileManagerImpl.class) {
            if (instance == null) {
                instance = fileSystem.isEmpty() ?
                        new DefaultCheckpointFileManagerImpl()
                        : new DefaultCheckpointFileManagerImpl(fileSystem.get());
            }
            return instance;
        }
    }

}
