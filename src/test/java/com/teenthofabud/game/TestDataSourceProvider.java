package com.teenthofabud.game;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.Player;

import java.util.UUID;

public interface TestDataSourceProvider {

    default Player player() {
        return new Player.Builder().name(UUID.randomUUID().toString()).build();
    }

    default Character character(Player player, CharacterType characterType) {
        return new Character.Builder().type(characterType).player(player).build();
        /*Character character = null;
        try {
            *//*Player player = player();
            CharacterTypeService characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
            CharacterType expectedCharacterType = characterTypeService.retrieveCharacterType(Math.abs(player.hashCode()));*//*
            character = new Character.Builder().type(characterType).player(player()).build();
        } catch (CharacterTypeException e) {
            System.err.println(e.getMessage());
        }
        return character;*/
    }

    default Checkpoint checkpoint(Character character) {
        return new Checkpoint.Builder().character(character).build();
    }

}
