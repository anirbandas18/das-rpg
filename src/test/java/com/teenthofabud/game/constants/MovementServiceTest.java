package com.teenthofabud.game.constants;

import com.teenthofabud.game.constants.movement.Movement;
import com.teenthofabud.game.constants.movement.MovementException;
import com.teenthofabud.game.constants.movement.service.MovementService;
import com.teenthofabud.game.constants.movement.service.impl.DefaultMovementServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class MovementServiceTest {

    private static MovementService MOVEMENT_SERVICE;

    @BeforeAll
    static void beforeAll() {
        MOVEMENT_SERVICE = DefaultMovementServiceImpl.getInstance();
    }

    @Test
    public void testMovementRetrievalByKeyIsSuccessful() {
        assertDoesNotThrow(() -> {
            String expectedMovementKey = "U";
            Movement actualMovement = MOVEMENT_SERVICE.retrieveMovement(expectedMovementKey);
            Assertions.assertEquals(expectedMovementKey, actualMovement.getKey());
        });
    }

    @Test
    public void testMovementRetrievalByInvalidKeyFailed() {
        assertThrowsExactly(MovementException.class, () -> {
            String expectedMovementKey = null;
            MOVEMENT_SERVICE.retrieveMovement(expectedMovementKey);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "Y" })
    public void testMovementRetrievalByInvalidKeyFailed(String expectedMovementKey) {
        assertThrowsExactly(MovementException.class, () -> {
            MOVEMENT_SERVICE.retrieveMovement(expectedMovementKey);
        });
    }

}
