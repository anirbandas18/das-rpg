package com.teenthofabud.game.resources.map.service.impl;

import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.MapException;
import com.teenthofabud.game.resources.map.model.GridMap;
import com.teenthofabud.game.resources.map.service.MapService;

public class DefaultMapServiceImpl implements MapService {

    private FileManager<Configuration, String> configurationFileManager;

    private DefaultMapServiceImpl(FileManager<Configuration, String> configurationFileManager) {
        this.configurationFileManager = configurationFileManager;
    }

    private static volatile MapService INSTANCE;

    public static MapService getInstance(FileManager<Configuration, String> configurationFileManager) {
        MapService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMapServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultMapServiceImpl(configurationFileManager);
            }
            return INSTANCE;
        }
    }

    @Override
    public Map getDefaultGrid() throws MapException {
        try {
            Configuration configuration = configurationFileManager.readData();
            return new GridMap.Builder().name(configuration.getDefaultNameOfGridMap()).magnitude(configuration.getDefaultMagnitudeOfGridMap()).build();
        } catch (FileManagementException e) {
            throw new MapException(e.getMessage());
        }
    }
}
