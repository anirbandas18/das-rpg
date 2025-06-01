package com.teenthofabud.game.persistence.repository;

import com.teenthofabud.game.persistence.FileManagementException;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;

public interface FileManager<T> {

    static final String DATA_DIRECTORY_NAME = "das-rpg";

    public T readData() throws FileManagementException;

    public void writeData(T data) throws FileManagementException;

    public void clearData() throws FileManagementException;

    public String getFileName();

    public FileSystem getFileSystem();

    default Path dataFilePath() {
        return dataDirectoryPath().resolve(getFileName());
    }

    default Path dataDirectoryPath() {
        return getFileSystem().getPath(DATA_DIRECTORY_NAME);
    }

    default byte[] serialize(Object object) throws FileManagementException {
        try {
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            ObjectOutputStream ois = new ObjectOutputStream(boas);
            ois.writeObject(object);
            return boas.toByteArray();
        } catch (IOException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

    default Object deserialize(byte[] bytes) throws FileManagementException {
        try {
            InputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

}
