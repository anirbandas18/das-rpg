package com.teenthofabud.game.constants.action.service.impl;

import com.teenthofabud.game.constants.action.Action;
import com.teenthofabud.game.constants.action.ActionException;
import com.teenthofabud.game.constants.action.service.ActionService;

import java.util.Arrays;

public class DefaultActionServiceImpl implements ActionService {

    public Action retrieveAction(String key) throws ActionException {
        if(key == null) {
            throw new ActionException("key " + key + " is required");
        }
        if(key.length() != 1) {
            throw new ActionException("key " + key + " is invalid");
        }
        char actualKey = key.charAt(0);
        return Arrays.stream(Action.values())
                .filter(e -> e.getKey() == actualKey)
                .findFirst()
                .orElseThrow(() -> new ActionException("key " + key + " not recognized"));
    }

    private static volatile ActionService INSTANCE;

    private DefaultActionServiceImpl() {

    }

    public static ActionService getInstance() {
        ActionService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultActionServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultActionServiceImpl();
            }
            return INSTANCE;
        }
    }
}
