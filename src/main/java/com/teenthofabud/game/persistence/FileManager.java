package com.teenthofabud.game.persistence;

import java.io.*;
import java.nio.file.FileSystem;

public interface FileManager<T, K> {

    public boolean isDataAvailable() throws FileManagementException;

    public T readData() throws FileManagementException;

    public void writeData(T data) throws FileManagementException;

    public void clearData() throws FileManagementException;

    public String getFileName();

    public FileSystem getFileSystem();

    public K dataFilePath();

    public K dataDirectoryPath();

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
