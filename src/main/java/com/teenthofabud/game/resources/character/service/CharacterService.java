package com.teenthofabud.game.resources.character.service;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.player.Player;

public interface CharacterService {

    public Character createCharacter(Player player, CharacterType type) throws CharacterException;

}
