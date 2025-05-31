package com.teenthofabud.game.engine.renderer;

public interface RenderingService {

    public void menu(String options);

    public void info(String message);

    public void success(String message);

    public void warn(String message);

    public void error(String message);

}
