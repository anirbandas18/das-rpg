package com.teenthofabud.game.engine;

import com.teenthofabud.game.engine.renderer.RenderingException;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.engine.renderer.impl.DefaultStdoutRenderingImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;


public class RenderingServiceTest {

    private static RenderingService RENDERING_SERVICE;

    @BeforeAll
    static void beforeAll() {
        RENDERING_SERVICE = DefaultStdoutRenderingImpl.getInstance();
    }

    @Test
    public void testMenuIsSuccessful() {
        assertDoesNotThrow(() -> {
            RENDERING_SERVICE.menu("menu");
        });
    }

    @Test
    public void testMenuFailure() {
        assertThrowsExactly(RenderingException.class, () -> {
            RENDERING_SERVICE.menu(null);
        });
    }

    @Test
    public void testMapIsSuccessful() {
        assertDoesNotThrow(() -> {
            RENDERING_SERVICE.map(10, 2, 1);
        });
    }

    @Test
    public void testMapFailure() {
        assertThrowsExactly(RenderingException.class, () -> {
            RENDERING_SERVICE.map(3, 99, -1);
        });
    }
}
