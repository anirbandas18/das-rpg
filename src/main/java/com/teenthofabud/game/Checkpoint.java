package com.teenthofabud.game;

import com.teenthofabud.game.resources.character.Character;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class Checkpoint implements Serializable {

    private Character character;

    private Checkpoint(Builder builder) {
        this.character = builder.character;
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkpoint that = (Checkpoint) o;
        return Objects.equals(getCharacter(), that.getCharacter());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCharacter());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Checkpoint.class.getSimpleName() + "[", "]")
                .add("character=" + character)
                .toString();
    }

    public static class Builder {
        private Character character;

        public Builder character(Character character) {
            this.character = character;
            return this;
        }

        public Checkpoint build() {
            return new Checkpoint(this);
        }
    }
}
