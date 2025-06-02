package com.teenthofabud.game.resources.enemy.service.impl;

import com.teenthofabud.game.persistence.FileManagementException;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.configuration.Configuration;
import com.teenthofabud.game.resources.enemy.Enemy;
import com.teenthofabud.game.resources.enemy.EnemyException;
import com.teenthofabud.game.resources.enemy.model.SimpleEnemy;
import com.teenthofabud.game.resources.enemy.service.EnemyService;

public class DefaultEnemyServiceImpl implements EnemyService {

    private FileManager<Configuration, String> configurationFileManager;

    private DefaultEnemyServiceImpl(FileManager<Configuration, String> configurationFileManager) {
        this.configurationFileManager = configurationFileManager;
    }

    private static volatile EnemyService INSTANCE;

    public static EnemyService getInstance(FileManager<Configuration, String> configurationFileManager) {
        EnemyService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultEnemyServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultEnemyServiceImpl(configurationFileManager);
            }
            return INSTANCE;
        }
    }

    @Override
    public Enemy spawnEnemy() throws EnemyException {
        try {
            Configuration configuration = configurationFileManager.readData();
            return new SimpleEnemy.Builder().name(configuration.getDefaultNameOfSimpleEnemy()).strength(configuration.getDefaultStrengthOfSimpleEnemy()).build();
        } catch (FileManagementException e) {
            throw new EnemyException(e.getMessage());
        }
    }
}
