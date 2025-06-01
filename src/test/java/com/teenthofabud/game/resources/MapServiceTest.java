package com.teenthofabud.game.resources;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.MapException;
import com.teenthofabud.game.resources.map.service.MapService;
import com.teenthofabud.game.resources.map.service.impl.DefaultMapServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class MapServiceTest implements TestDataSourceProvider {

    private static MapService MAP_SERVICE;
    private static FileManager<Configuration, String> CONFIGURATION_FILE_MANAGER;

    private Configuration configuration;
    private Map map;

    @BeforeAll
    static void beforeAll() {
        CONFIGURATION_FILE_MANAGER = mock(FileManager.class);
        MAP_SERVICE = DefaultMapServiceImpl.getInstance(CONFIGURATION_FILE_MANAGER);
    }

    @BeforeEach
    void setUp() {
        configuration = configuration();
        map = defaultMap(configuration);
        reset(CONFIGURATION_FILE_MANAGER);
    }

    @Test
    public void testDefaultGridIsLoadedSuccessfully() {
        assertDoesNotThrow(() -> {
            when(CONFIGURATION_FILE_MANAGER.readData()).thenReturn(configuration);

            Map actual = MAP_SERVICE.getDefaultGrid();

            Assertions.assertEquals(map, actual);
        });
    }

    @Test
    public void testDefaultGridLoadFailure() {
        assertThrowsExactly(MapException.class, () -> {
            when(CONFIGURATION_FILE_MANAGER.readData()).thenThrow(new FileManagementException("Simulation"));
            MAP_SERVICE.getDefaultGrid();
        });
    }
}
