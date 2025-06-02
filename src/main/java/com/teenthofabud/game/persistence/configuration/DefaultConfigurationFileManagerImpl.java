package com.teenthofabud.game.persistence.configuration;

import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.util.Properties;

public class DefaultConfigurationFileManagerImpl implements FileManager<Configuration, String> {

    @Override
    public boolean isDataAvailable() throws FileManagementException {
        try {
            InputStream is = DefaultConfigurationFileManagerImpl.class.getClassLoader().getResourceAsStream(getFileName());
            boolean available = is.available() > 0;
            is.close();
            return available;
        } catch (IOException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

    @Override
    public Configuration readData() throws FileManagementException  {
        try {
            InputStream is = DefaultConfigurationFileManagerImpl.class.getClassLoader().getResourceAsStream(getFileName());
            Properties prop = new Properties();
            if (is == null) {
                throw new FileManagementException(getFileName() + " not found");
            }
            prop.load(is);
            Configuration defaultConfigurations = new Configuration.Builder()
                    .defaultMagnitudeOfGridMap(Integer.valueOf(prop.getProperty("map.grid.magnitude")))
                    .defaultNameOfGridMap(prop.getProperty("map.grid.name"))
                    .defaultNameOfSimpleEnemy(prop.getProperty("enemy.simple.name"))
                    .defaultStrengthOfSimpleEnemy(Integer.valueOf(prop.getProperty("enemy.simple.strength")))
                    .build();
            is.close();
            return defaultConfigurations;
        } catch (IOException |  NumberFormatException e) {
            throw new FileManagementException(e.getMessage());
        }
    }

    @Override
    public void writeData(Configuration data) throws FileManagementException {
        throw new UnsupportedOperationException("Writing configurations not allowed");
    }

    @Override
    public void clearData() throws FileManagementException {
        throw new UnsupportedOperationException("Deleting configurations not allowed");
    }

    @Override
    public String getFileName() {
        return "default-config.properties";
    }

    @Override
    public FileSystem getFileSystem() {
        throw new UnsupportedOperationException("External file system not allowed");
    }

    @Override
    public String dataFilePath() {
        throw new UnsupportedOperationException("Specific file path not allowed");
    }

    @Override
    public String dataDirectoryPath() {
        throw new UnsupportedOperationException("Specific directory not allowed");
    }

    private static volatile FileManager<Configuration, String> INSTANCE;

    private DefaultConfigurationFileManagerImpl() {
    }

    public static FileManager<Configuration, String> getInstance() {
        FileManager<Configuration, String> result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultConfigurationFileManagerImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultConfigurationFileManagerImpl();
            }
            return INSTANCE;
        }
    }

}
