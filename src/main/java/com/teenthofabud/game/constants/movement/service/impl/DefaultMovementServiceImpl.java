package com.teenthofabud.game.constants.movement.service.impl;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;
import com.teenthofabud.game.constants.movement.service.MovementService;

import java.util.Arrays;
import java.util.Random;

public class DefaultMovementServiceImpl implements MovementService {

    public Movement retrieveMovement(int preSeed) throws MovementException {
        if(preSeed < 1) {
            throw new MovementException("preSeed " + preSeed + " is too less");
        }
        Long seed = preSeed * System.currentTimeMillis();
        Random random = new Random(seed);
        int randomizedIndex = random.ints(0, Movement.values().length)
                .findFirst()
                .getAsInt();
        return Movement.values()[randomizedIndex];
    }

    public Movement retrieveMovement(String key) throws MovementException {
        if(key == null) {
            throw new MovementException("key " + key + " is required");
        }
        return Arrays.stream(Movement.values())
                .filter(e -> e.getKey().compareTo(key) == 0).
                findFirst()
                .orElseThrow(() -> new MovementException("key " + key + " not recognized"));
    }

}
