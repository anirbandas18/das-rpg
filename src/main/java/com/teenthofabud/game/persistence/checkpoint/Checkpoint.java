package com.teenthofabud.game.persistence.checkpoint;

import com.teenthofabud.game.resources.character.Character;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class Checkpoint implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private Character character;
    private Point coordinates;

    private Checkpoint(Builder builder) {
        this.character = builder.character;
        this.coordinates = builder.coordinates;
    }

    public Character getCharacter() {
        return character;
    }

    public int x() {
        return (int) Math.round(coordinates.getX());
    }

    public int y() {
        return (int) Math.round(coordinates.getLocation().getY());
    }

    public void x(int x) {
        this.coordinates.x = x;
    }

    public void y(int y) {
        this.coordinates.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkpoint that = (Checkpoint) o;
        return Objects.equals(getCharacter(), that.getCharacter()) && Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCharacter(), coordinates);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Checkpoint.class.getSimpleName() + "[", "]")
                .add("character=" + character)
                .add("coordinates=" + coordinates)
                .toString();
    }

    public static class Builder {
        private Character character;
        private Point coordinates;

        public Builder character(Character character) {
            this.character = character;
            return this;
        }

        public Builder x(int x) {
            if(this.coordinates == null) {
                this.coordinates = new Point();
            }
            this.coordinates.x = x;
            return this;
        }

        public Builder y(int y) {
            if(this.coordinates == null) {
                this.coordinates = new Point();
            }
            this.coordinates.y = y;
            return this;
        }

        public Checkpoint build() {
            return new Checkpoint(this);
        }
    }
}
