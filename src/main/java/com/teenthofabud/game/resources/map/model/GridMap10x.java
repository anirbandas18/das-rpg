package com.teenthofabud.game.resources.map.model;

import com.teenthofabud.game.resources.map.Map;

import java.io.Serializable;

public class GridMap10x extends Map implements Serializable {

    private GridMap10x(Builder builder) {
        this.name = this.getClass().getSimpleName();
        this.magnitude = builder.magnitude;
    }

    @Override
    public Integer getMagnitude() {
        return magnitude;
    }

    @Override
    public String getName() {
        return name;
    }

    public static class Builder {
        private Integer magnitude;

        public Builder magnitude(Integer magnitude) {
            this.magnitude = magnitude;
            return this;
        }

        public GridMap10x build() {
            return new GridMap10x(this);
        }
    }
}
