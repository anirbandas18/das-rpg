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

    default Player player(String name) {
        return new Player.Builder().name(name).build();
    }

    default Character character(Player player, CharacterType characterType) {
        return new Character.Builder().type(characterType).player(player).build();
    }

    default Checkpoint checkpoint(Character character) {
        return new Checkpoint.Builder().character(character).x(2).y(4).build();
    }

}
