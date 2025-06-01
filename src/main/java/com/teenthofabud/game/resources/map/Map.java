package com.teenthofabud.game.resources.map;

import java.util.Objects;

public abstract class Map {

    protected String name;
    protected Integer magnitude;

    public Integer getMagnitude() {
        return this.magnitude;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map = (Map) o;
        return Objects.equals(getName(), map.getName()) && Objects.equals(getMagnitude(), map.getMagnitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMagnitude());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Map{");
        sb.append("magnitude=").append(magnitude);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
