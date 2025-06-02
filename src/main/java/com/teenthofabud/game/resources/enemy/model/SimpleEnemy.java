package com.teenthofabud.game.resources.enemy.model;

import com.teenthofabud.game.resources.enemy.Enemy;

import java.io.Serializable;
import java.util.StringJoiner;

public class SimpleEnemy extends Enemy implements Serializable {

    private SimpleEnemy(Builder builder) {
        this.name = builder.name;
        this.strength = builder.strength;
    }

    public static class Builder {
        private Integer strength;
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder strength(Integer strength) {
            this.strength = strength;
            return this;
        }

        public SimpleEnemy build() {
            return new SimpleEnemy(this);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SimpleEnemy.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("strength=" + strength)
                .toString();
    }
}
