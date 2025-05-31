package com.teenthofabud.game.constants.charactertype.service.impl;

import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;

import java.util.Arrays;
import java.util.Random;

public class DefaultCharacterTypeServiceImpl implements CharacterTypeService {

    @Override
    public CharacterType retrieveCharacterType(int preSeed) throws CharacterTypeException {
        if(preSeed < 1) {
            throw new CharacterTypeException("preSeed " + preSeed + " is too less");
        }
        Long seed = preSeed * System.currentTimeMillis();
        Random random = new Random(seed);
        int randomizedIndex = random.ints(0, CharacterType.values().length)
                .findFirst()
                .getAsInt();
        return CharacterType.values()[randomizedIndex];
    }

    @Override
    public CharacterType retrieveCharacterType(String key) throws CharacterTypeException {
        if(key == null) {
            throw new CharacterTypeException("key " + key + " is required");
        }
        return Arrays.stream(CharacterType.values())
                .filter(e -> e.getKey().compareTo(key) == 0).
                findFirst()
                .orElseThrow(() -> new CharacterTypeException("key " + key + " not recognized"));
    }

    private static volatile CharacterTypeService instance;

    private DefaultCharacterTypeServiceImpl() {

    }

    public static CharacterTypeService getInstance() {
        CharacterTypeService result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultCharacterTypeServiceImpl.class) {
            if (instance == null) {
                instance = new DefaultCharacterTypeServiceImpl();
            }
            return instance;
        }
    }

}
