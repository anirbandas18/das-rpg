package com.teenthofabud.game.constants.movement.service.impl;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;
import com.teenthofabud.game.constants.movement.service.MovementService;

import java.util.Arrays;

public class DefaultMovementServiceImpl implements MovementService {

    public Movement retrieveMovement(String key) throws MovementException {
        if(key == null) {
            throw new MovementException("key " + key + " is required");
        }
        return Arrays.stream(Movement.values())
                .filter(e -> e.getKey().compareTo(key) == 0).
                findFirst()
                .orElseThrow(() -> new MovementException("key " + key + " not recognized"));
    }

    private static volatile MovementService instance;

    private DefaultMovementServiceImpl() {

    }

    public static MovementService getInstance() {
        MovementService result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMovementServiceImpl.class) {
            if (instance == null) {
                instance = new DefaultMovementServiceImpl();
            }
            return instance;
        }
    }

}
