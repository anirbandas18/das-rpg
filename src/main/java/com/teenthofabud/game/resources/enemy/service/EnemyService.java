package com.teenthofabud.game.resources.enemy.service;

import com.teenthofabud.game.resources.enemy.Enemy;
import com.teenthofabud.game.resources.enemy.EnemyException;

public interface EnemyService {

    public Enemy spawnEnemy() throws EnemyException;

}
