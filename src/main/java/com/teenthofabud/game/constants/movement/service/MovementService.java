package com.teenthofabud.game.constants.movement.service;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;

public interface MovementService {

    public Movement retrieveMovement(String key) throws MovementException;

}
