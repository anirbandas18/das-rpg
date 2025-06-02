package com.teenthofabud.game.constants.action.service;

import com.teenthofabud.game.constants.action.Action;
import com.teenthofabud.game.constants.action.ActionException;

public interface ActionService {

    public Action retrieveAction(String key) throws ActionException;

}
