package com.teenthofabud.game.persistence.configuration;

import java.util.Objects;
import java.util.StringJoiner;

public class Configuration {

    private Integer defaultMagnitudeOfGridMap;
    private String defaultNameOfGridMap;

    private Configuration(Builder builder) {
        this.defaultMagnitudeOfGridMap = builder.defaultMagnitudeOfGridMap;
        this.defaultNameOfGridMap = builder.defaultNameOfGridMap;
    }

    public Integer getDefaultMagnitudeOfGridMap() {
        return defaultMagnitudeOfGridMap;
    }

    public String getDefaultNameOfGridMap() {
        return defaultNameOfGridMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(getDefaultMagnitudeOfGridMap(), that.getDefaultMagnitudeOfGridMap()) && Objects.equals(getDefaultNameOfGridMap(), that.getDefaultNameOfGridMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDefaultMagnitudeOfGridMap(), getDefaultNameOfGridMap());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Configuration.class.getSimpleName() + "[", "]")
                .add("defaultMagnitudeOfGridMap=" + defaultMagnitudeOfGridMap)
                .add("defaultNameOfGridMap='" + defaultNameOfGridMap + "'")
                .toString();
    }

    public static class Builder {
        private Integer defaultMagnitudeOfGridMap;
        private String defaultNameOfGridMap;

        public Builder defaultMagnitudeOfGridMap(Integer defaultMagnitudeOfGridMap) {
            this.defaultMagnitudeOfGridMap = defaultMagnitudeOfGridMap;
            return this;
        }

        public Builder defaultNameOfGridMap(String defaultNameOfGridMap) {
            this.defaultNameOfGridMap = defaultNameOfGridMap;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

}
