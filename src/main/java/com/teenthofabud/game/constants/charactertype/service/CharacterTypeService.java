package com.teenthofabud.game.constants.charactertype.service;

import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.CharacterType;

public interface CharacterTypeService {

    public CharacterType retrieveCharacterType(int preSeed) throws CharacterTypeException;

    public CharacterType retrieveCharacterType(String key) throws CharacterTypeException;

}
