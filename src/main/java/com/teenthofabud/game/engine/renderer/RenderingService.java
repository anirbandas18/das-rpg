package com.teenthofabud.game.engine.renderer;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.enemy.Enemy;

public interface RenderingService {

    public void menu(String options) throws RenderingException;

    public void map(int magnitude, int x, int y) throws RenderingException;

    public void info(String message);

    public void success(String message);

    public void warn(String message);

    public void error(String message);

}
