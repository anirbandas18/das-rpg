package com.teenthofabud.game.engine;

import com.teenthofabud.game.TestDataSourceProvider;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import com.teenthofabud.game.engine.exploration.ExplorationException;
import com.teenthofabud.game.engine.exploration.ExplorationService;
import com.teenthofabud.game.engine.exploration.impl.DefaultExplorationServiceImpl;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.map.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ExplorationServiceTest implements TestDataSourceProvider {

    private static ExplorationService EXPLORATION_SERVICE;
    private static RenderingService RENDERING_SERVICE;
    private static MovementService MOVEMENT_SERVICE;

    private Character character;
    private Map map;
    private Point point;

    @BeforeAll
    static void beforeAll() {
        RENDERING_SERVICE = DefaultStdoutRenderingImpl.getInstance();
        MOVEMENT_SERVICE = DefaultMovementServiceImpl.getInstance();
        EXPLORATION_SERVICE = DefaultExplorationServiceImpl.getInstance(RENDERING_SERVICE, MOVEMENT_SERVICE);
    }

    @BeforeEach
    void setUp() {
        character = character(player(UUID.randomUUID().toString()), CharacterType.GOALKEEPER);
        map = defaultMap(configuration());
        point = new Point(0, 0);
    }

    @Test
    public void testInitIsSuccessful() {
        assertDoesNotThrow(() -> {
            EXPLORATION_SERVICE.init(character, point, map);
        });
    }

    @Test
    public void testInitWithNullCharacterFailure() {
        assertThrowsExactly(ExplorationException.class, () -> {
            EXPLORATION_SERVICE.init(null, point, map);
        });
    }

    @Test
    public void testInitWithInvalidPointCharacterFailure() {
        assertThrowsExactly(ExplorationException.class, () -> {
            point.x = map.getMagnitude() + 1;
            EXPLORATION_SERVICE.init(character, point, map);
        });
    }

    @Test
    public void testInitWithNullMapFailure() {
        assertThrowsExactly(ExplorationException.class, () -> {
            EXPLORATION_SERVICE.init(character, point, null);
        });
    }

    @Test
    public void testInvalidMovementFailure() {
        assertThrowsExactly(ExplorationException.class, () -> {
            EXPLORATION_SERVICE.init(character, point, map);

            EXPLORATION_SERVICE.move(UUID.randomUUID().toString());
        });
    }

    @Test
    public void testMovementBeyondTopLeftCornerFailure() {
        assertDoesNotThrow(() -> {
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.UP.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondTopRightCornerFailure() {
        assertDoesNotThrow(() -> {
            point.y = map.getMagnitude() - 1;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.RIGHT.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondBottomRightCornerFailure() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() - 1;
            point.y = map.getMagnitude() - 1;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.DOWN.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondBottomLeftCornerFailure() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() - 1;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.LEFT.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondLeftEdgeFailure() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() / 2;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.LEFT.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondRightEdgeFailure() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() / 2;
            point.y = map.getMagnitude() - 1;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.RIGHT.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondTopEdgeFailure() {
        assertDoesNotThrow(() -> {
            point.y = map.getMagnitude() / 2;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.UP.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementBeyondBottomEdgeFailure() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() - 1;
            point.y = map.getMagnitude() / 2;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.DOWN.getKey());

            Assertions.assertFalse(success);
        });
    }

    @Test
    public void testMovementWithinLimitsIsSuccessful() {
        assertDoesNotThrow(() -> {
            point.x = map.getMagnitude() / 2;
            point.y = map.getMagnitude() / 2;
            EXPLORATION_SERVICE.init(character, point, map);

            boolean success = EXPLORATION_SERVICE.move(Movement.DOWN.getKey());

            Assertions.assertTrue(success);
        });
    }

    @Test
    public void testCheckpointIsInNewPosition() {
        assertDoesNotThrow(() -> {
            Point expectedPoint = new Point(map.getMagnitude() / 2, map.getMagnitude() / 2);
            EXPLORATION_SERVICE.init(character, expectedPoint, map);
            boolean success = EXPLORATION_SERVICE.move(Movement.DOWN.getKey());

            Point actualPoint = EXPLORATION_SERVICE.checkpoint();

            Assertions.assertNotEquals(expectedPoint, actualPoint);
        });
    }

    @Test
    public void testCheckpointIsInSamePositionWhenMovementIsNotPossible() {
        assertDoesNotThrow(() -> {
            EXPLORATION_SERVICE.init(character, point, map);
            boolean success = EXPLORATION_SERVICE.move(Movement.UP.getKey());

            Point actualPoint = EXPLORATION_SERVICE.checkpoint();

            Assertions.assertEquals(point, actualPoint);
        });
    }

    @Test
    public void testClearResetsLastPositionToDefaultValue() {
        assertDoesNotThrow(() -> {
            EXPLORATION_SERVICE.init(character, point, map);
            boolean success = EXPLORATION_SERVICE.move(Movement.UP.getKey());

            EXPLORATION_SERVICE.clear();
            Point actualPoint = EXPLORATION_SERVICE.checkpoint();

            Assertions.assertTrue(point.x == 0);
            Assertions.assertTrue(point.y == 0);
        });
    }

}
