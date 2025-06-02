package com.teenthofabud.game.resources.enemy;

import com.teenthofabud.game.resources.map.Map;

import java.util.Objects;

public abstract class Enemy {

    protected String name;
    protected Integer strength;

    public Integer getStrength() {
        return this.strength;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map = (Map) o;
        return Objects.equals(getName(), map.getName()) && Objects.equals(getStrength(), map.getMagnitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getStrength());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enemy{");
        sb.append("strength=").append(strength);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
