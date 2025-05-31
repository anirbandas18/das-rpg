package com.teenthofabud.game.resources.character;

import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.resources.player.Player;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class Character implements Serializable {

    private Player player;
    private CharacterType type;

    private Character(Builder builder) {
        this.player = builder.player;
        this.type = builder.type;
    }

    public CharacterType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(getPlayer(), character.getPlayer()) && getType() == character.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer(), getType());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Character.class.getSimpleName() + "[", "]")
                .add("player=" + player)
                .add("type=" + type)
                .toString();
    }

    public static class Builder {
        private Player player;
        private CharacterType type;

        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        public Builder type(CharacterType type) {
            this.type = type;
            return this;
        }

        public Character build() {
            return new Character(this);
        }
    }
}
