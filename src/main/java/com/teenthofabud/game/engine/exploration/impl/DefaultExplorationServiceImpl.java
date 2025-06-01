package com.teenthofabud.game.engine.exploration.impl;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.engine.exploration.ExplorationException;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.map.Map;

import java.awt.*;

public class DefaultExplorationServiceImpl implements ExplorationService {

    private RenderingService renderingService;
    private MovementService movementService;
    private Character character;
    private Point point;
    private Map map;

    private void render(String movementName) {
        renderingService.error(character + " can't explore " + movementName + " on map " + map.getName());
    }

    private boolean possibleToMoveUpOrLeftFromTopLeftCorner(Movement movement) {
        boolean flag = true;
        if(point.x == 0 && point.y == 0) {
            if(movement.compareTo(Movement.UP) == 0) {
                flag = false;
                render(movement.name());
            }
            if(movement.compareTo(Movement.LEFT) == 0) {
                flag = false;
                render(movement.name());
            }
        }
        return flag;
    }

    private boolean possibleToMoveUpOrRightFromTopRightCorner(Movement movement) {
        boolean flag = true;
        if(point.x == 0 && point.y == map.getMagnitude() - 1) {
            if(movement.compareTo(Movement.UP) == 0) {
                flag = false;
                render(movement.name());
            }
            if(movement.compareTo(Movement.RIGHT) == 0) {
                flag = false;
                render(movement.name());
            }
        }
        return flag;
    }

    private boolean possibleToMoveDownOrLeftFromBottomRightCorner(Movement movement) {
        boolean flag = true;
        if(point.x == map.getMagnitude() - 1 && point.y == 0) {
            if(movement.compareTo(Movement.DOWN) == 0) {
                flag = false;
                render(movement.name());
            }
            if(movement.compareTo(Movement.LEFT) == 0) {
                flag = false;
                render(movement.name());
            }
        }
        return flag;
    }

    private boolean possibleToMoveDownOrRightFromBottomRightCorner(Movement movement) {
        boolean flag = true;
        if(point.x == map.getMagnitude() - 1 && point.y == map.getMagnitude() - 1) {
            if(movement.compareTo(Movement.DOWN) == 0) {
                flag = false;
                render(movement.name());
            }
            if(movement.compareTo(Movement.RIGHT) == 0) {
                flag = false;
                render(movement.name());
            }
        }
        return flag;
    }

    private boolean isCrossingBoundaryHorizontallyOrVertically(Movement movement) {
        boolean flag = false;
        if(point.x == 0 && point.y > 0 && point.y < map.getMagnitude() - 1 && movement.compareTo(Movement.UP) == 0) {
            flag = true;
            render(movement.name());
        }
        if(point.x == map.getMagnitude() - 1 && point.y > 0 && point.y < map.getMagnitude() - 1 && movement.compareTo(Movement.DOWN) == 0) {
            flag = true;
            render(movement.name());
        }
        if(point.y == 0 && point.x > 0 && point.x < map.getMagnitude() - 1 && movement.compareTo(Movement.LEFT) == 0) {
            flag = true;
            render(movement.name());
        }
        if(point.y == map.getMagnitude() - 1 && point.x > 0 && point.x < map.getMagnitude() - 1 && movement.compareTo(Movement.RIGHT) == 0) {
            flag = true;
            render(movement.name());
        }
        return flag;
    }

    @Override
    public void init(Character character, Point point, Map map) throws ExplorationException {
        if(map == null) {
            throw new ExplorationException("map is required");
        }
        if(point == null) {
            throw new ExplorationException("point is required");
        }
        if(point.x < 0 || point.y < 0 || point.x >= map.getMagnitude() || point.y >= map.getMagnitude()) {
            throw new ExplorationException("from position is invalid");
        }
        if(character == null) {
            throw new ExplorationException("is missing character");
        }
        this.map = map;
        this.character = character;
        this.point = new Point(point.x, point.y);
        renderingService.info(character + " loaded on map " + map.getName() + " at (" + point.x + "," + point.y + ")");
    }

    @Override
    public boolean move(String movementKey) throws ExplorationException {
        boolean success = false;
        try {
            Movement movement = movementService.retrieveMovement(movementKey);
            boolean possibleToMoveUpOrLeftFromTopLeftCorner = possibleToMoveUpOrLeftFromTopLeftCorner(movement);
            boolean possibleToMoveUpOrRightFromTopRightCorner = possibleToMoveUpOrRightFromTopRightCorner(movement);
            boolean possibleToMoveDownOrLeftFromBottomRightCorner = possibleToMoveDownOrLeftFromBottomRightCorner(movement);
            boolean possibleToMoveDownOrRightFromBottomRightCorner = possibleToMoveDownOrRightFromBottomRightCorner(movement);;
            boolean isCrossingBoundaryHorizontallyOrVertically = isCrossingBoundaryHorizontallyOrVertically(movement);
            success = possibleToMoveUpOrLeftFromTopLeftCorner & possibleToMoveUpOrRightFromTopRightCorner & possibleToMoveDownOrLeftFromBottomRightCorner & possibleToMoveDownOrRightFromBottomRightCorner & !isCrossingBoundaryHorizontallyOrVertically;
            if(success) {
                switch (movement) {
                    case UP -> point.x = point.x - 1;
                    case DOWN -> point.x = point.x + 1;
                    case LEFT -> point.y = point.y - 1;
                    case RIGHT -> point.y = point.y + 1;
                }
            }

        } catch (MovementException e) {
            throw new ExplorationException(e.getMessage());
        }
        return success;
    }

    @Override
    public Point checkpoint() {
        return point;
    }

    @Override
    public void clear() {
        this.character = null;
        this.point = new Point(0, 0);
        this.map = null;
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
