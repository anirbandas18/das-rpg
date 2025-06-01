package com.teenthofabud.game.resources.map.service.impl;

import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.resources.map.Map;
import com.teenthofabud.game.resources.map.model.GridMap10x;
import com.teenthofabud.game.resources.map.service.MapService;

public class DefaultMapServiceImpl implements MapService {

    private static final int MAGNITUDE = 10;

    private RenderingService renderingService;

    private DefaultMapServiceImpl(RenderingService renderingService) {
        this.renderingService = renderingService;
    }

    @Override
    public Map get10xGrid() {
        return new GridMap10x.Builder().magnitude(MAGNITUDE).build();
    }

    private static volatile MapService INSTANCE;

    public static MapService getInstance(RenderingService renderingService) {
        MapService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultMapServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultMapServiceImpl(renderingService);
            }
            return INSTANCE;
        }
    }

}
