package com.teenthofabud.game.renderer.impl;

import com.teenthofabud.game.renderer.RenderingService;

public class DefaultStdoutRenderingImpl implements RenderingService {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public void info(String message) {
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }

    @Override
    public void success(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    @Override
    public void warn(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    @Override
    public void error(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    private static volatile RenderingService INSTANCE;

    private DefaultStdoutRenderingImpl() {

    }

    public static RenderingService getInstance() {
        RenderingService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultStdoutRenderingImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultStdoutRenderingImpl();
            }
            return INSTANCE;
        }
    }

}
