package com.teenthofabud.game.resources.map.model;

import com.teenthofabud.game.resources.map.Map;

import java.io.Serializable;

public class GridMap extends Map implements Serializable {

    private GridMap(Builder builder) {
        this.name = builder.name;
        this.magnitude = builder.magnitude;
    }

    public static class Builder {
        private Integer magnitude;
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder magnitude(Integer magnitude) {
            this.magnitude = magnitude;
            return this;
        }

        public GridMap build() {
            return new GridMap(this);
        }
    }
}
