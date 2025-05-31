package com.teenthofabud.game.resources.checkpoint.service;

import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.checkpoint.CheckpointException;

public interface CheckpointService {

    public void save(Character character) throws CheckpointException;

    public Checkpoint resume() throws CheckpointException;

}
