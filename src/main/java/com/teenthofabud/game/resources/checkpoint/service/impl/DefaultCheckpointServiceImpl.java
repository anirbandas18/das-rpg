package com.teenthofabud.game.resources.checkpoint.service.impl;

import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.checkpoint.CheckpointException;
import com.teenthofabud.game.resources.checkpoint.service.CheckpointService;

public class DefaultCheckpointServiceImpl implements CheckpointService {

    @Override
    public void save(Character character) throws CheckpointException {

    }

    @Override
    public Checkpoint resume() throws CheckpointException {
        return null;
    }
}
