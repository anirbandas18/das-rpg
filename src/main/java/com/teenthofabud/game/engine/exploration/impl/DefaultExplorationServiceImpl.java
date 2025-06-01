package com.teenthofabud.game.engine.exploration.impl;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.engine.exploration.ExplorationException;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.map.Map;

import java.awt.*;


public class DefaultExplorationServiceImpl implements ExplorationService {

    private RenderingService renderingService;
    private MovementService movementService;

    private boolean possibleToMoveUpOrLeftFromTopLeftCorner(Movement movement, Checkpoint checkpoint, Map map) {
        boolean flag = true;
        if(checkpoint.x() == 0 && checkpoint.y() == 0) {
            if(movement.compareTo(Movement.UP) == 0) {
                flag = false;
                renderingService.error("not possible upwards by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
            if(movement.compareTo(Movement.LEFT) == 0) {
                flag = false;
                renderingService.error("not possible to the left by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
        }
        return flag;
    }

    private boolean possibleToMoveUpOrRightFromTopRightCorner(Movement movement, Checkpoint checkpoint, Map map) {
        boolean flag = true;
        if(checkpoint.x() == 0 && checkpoint.y() == map.getMagnitude() - 1) {
            if(movement.compareTo(Movement.UP) == 0) {
                flag = false;
                renderingService.error("not possible upwards by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
            if(movement.compareTo(Movement.RIGHT) == 0) {
                flag = false;
                renderingService.error("not possible to the right by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
        }
        return flag;
    }

    private boolean possibleToMoveDownOrLeftFromBottomRightCorner(Movement movement, Checkpoint checkpoint, Map map) {
        boolean flag = true;
        if(checkpoint.x() == map.getMagnitude() - 1 && checkpoint.y() == 0) {
            if(movement.compareTo(Movement.DOWN) == 0) {
                flag = false;
                renderingService.error("not possible downwards by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
            if(movement.compareTo(Movement.LEFT) == 0) {
                flag = false;
                renderingService.error("not possible to the left by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
        }
        return flag;
    }

    private boolean possibleToMoveDownOrRightFromBottomRightCorner(Movement movement, Checkpoint checkpoint, Map map) {
        boolean flag = true;
        if(checkpoint.x() == map.getMagnitude() - 1 && checkpoint.y() == map.getMagnitude() - 1) {
            if(movement.compareTo(Movement.DOWN) == 0) {
                flag = false;
                renderingService.error("not possible downwards by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
            if(movement.compareTo(Movement.RIGHT) == 0) {
                flag = false;
                renderingService.error("not possible to the right by " + checkpoint.getCharacter() + " on map " + map.getName());
            }
        }
        return flag;
    }

    private boolean isCrossingBoundaryHorizontallyOrVertically(Movement movement, Checkpoint checkpoint, Map map) {
        boolean flag = false;
        if(checkpoint.x() == 0 && checkpoint.y() > 0 && checkpoint.y() < map.getMagnitude() - 1 && movement.compareTo(Movement.UP) == 0) {
            flag = true;
            renderingService.error("not possible upwards by " + checkpoint.getCharacter() + " on map " + map.getName());
        }
        if(checkpoint.x() == map.getMagnitude() - 1 && checkpoint.y() > 0 && checkpoint.y() < map.getMagnitude() - 1 && movement.compareTo(Movement.DOWN) == 0) {
            flag = true;
            renderingService.error("not possible downwards by " + checkpoint.getCharacter() + " on map " + map.getName());
        }
        if(checkpoint.y() == 0 && checkpoint.x() > 0 && checkpoint.x() < map.getMagnitude() - 1 && movement.compareTo(Movement.LEFT) == 0) {
            flag = true;
            renderingService.error("not possible to the left by " + checkpoint.getCharacter() + " on map " + map.getName());
        }
        if(checkpoint.y() == map.getMagnitude() - 1 && checkpoint.x() > 0 && checkpoint.x() < map.getMagnitude() - 1 && movement.compareTo(Movement.RIGHT) == 0) {
            flag = true;
            renderingService.error("not possible to the right by " + checkpoint.getCharacter() + " on map " + map.getName());
        }
        return flag;
    }

    @Override
    public void move(Map map, String movementKey, Checkpoint checkpoint) throws ExplorationException {
        Point newPosition = new Point(0, 0);
        if(map == null) {
            throw new ExplorationException("map is required");
        }
        if(checkpoint == null) {
            throw new ExplorationException("checkpoint is required");
        }
        if(checkpoint.x() < 0 || checkpoint.y() < 0) {
            throw new ExplorationException("from last position is invalid");
        }
        if(checkpoint.getCharacter() == null) {
            throw new ExplorationException("is missing character");
        }
        try {
            Movement movement = movementService.retrieveMovement(movementKey);
            boolean possibleToMoveUpOrLeftFromTopLeftCorner = possibleToMoveUpOrLeftFromTopLeftCorner(movement, checkpoint, map);
            boolean possibleToMoveUpOrRightFromTopRightCorner = possibleToMoveUpOrRightFromTopRightCorner(movement, checkpoint, map);
            boolean possibleToMoveDownOrLeftFromBottomRightCorner = possibleToMoveDownOrLeftFromBottomRightCorner(movement, checkpoint, map);
            boolean possibleToMoveDownOrRightFromBottomRightCorner = possibleToMoveDownOrRightFromBottomRightCorner(movement, checkpoint, map);
            boolean isCrossingBoundaryHorizontallyOrVertically = isCrossingBoundaryHorizontallyOrVertically(movement, checkpoint, map);
            if(possibleToMoveUpOrLeftFromTopLeftCorner & possibleToMoveUpOrRightFromTopRightCorner & possibleToMoveDownOrLeftFromBottomRightCorner & possibleToMoveDownOrRightFromBottomRightCorner & !isCrossingBoundaryHorizontallyOrVertically) {
                switch (movement) {
                    case UP -> {
                        newPosition.x = checkpoint.x() - 1;
                        newPosition.y = checkpoint.y();
                    }
                    case DOWN -> {
                        newPosition.x = checkpoint.x() + 1;
                        newPosition.y = checkpoint.y();
                    }
                    case LEFT -> {
                        newPosition.y = checkpoint.y() - 1;
                        newPosition.x = checkpoint.x();
                    }
                    case RIGHT -> {
                        newPosition.y = checkpoint.x() + 1;
                        newPosition.x = checkpoint.x();
                    }
                }
            }
            checkpoint.x(newPosition.x);
            checkpoint.y(newPosition.y);
        } catch (MovementException e) {
            renderingService.error(e.getMessage());
        }
    }

    private static volatile ExplorationService INSTANCE;

    private DefaultExplorationServiceImpl(RenderingService renderingService, MovementService movementService) {
        this.renderingService = renderingService;
        this.movementService = movementService;
    }

    public static ExplorationService getInstance(RenderingService renderingService,
                                                 MovementService movementService) {
        ExplorationService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultExplorationServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultExplorationServiceImpl(renderingService, movementService);
            }
            return INSTANCE;
        }
    }
}
