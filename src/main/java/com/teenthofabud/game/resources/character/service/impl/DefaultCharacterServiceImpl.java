package com.teenthofabud.game.resources.character.service.impl;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.character.service.CharacterService;

public class DefaultCharacterServiceImpl implements CharacterService {

    @Override
    public Character createCharacter(Player player, CharacterType type) throws CharacterException {
        if(player == null) {
            throw new CharacterException("player is required");
        }
        if(type == null) {
            throw new CharacterException("type is required");
        }
        Character character = new Character.Builder().type(type).player(player).build();
        return character;
    }

    private static volatile CharacterService instance;

    private DefaultCharacterServiceImpl() {

    }

    public static CharacterService getInstance() {
        CharacterService result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DefaultCharacterServiceImpl.class) {
            if (instance == null) {
                instance = new DefaultCharacterServiceImpl();
            }
            return instance;
        }
    }

}
