package com.teenthofabud.game.persistence.configuration;

import java.util.Objects;
import java.util.StringJoiner;

public class Configuration {

    private Integer defaultMagnitudeOfGridMap;
    private String defaultNameOfGridMap;
    private Integer defaultStrengthOfSimpleEnemy;
    private String defaultNameOfSimpleEnemy;


    private Configuration(Builder builder) {
        this.defaultMagnitudeOfGridMap = builder.defaultMagnitudeOfGridMap;
        this.defaultNameOfGridMap = builder.defaultNameOfGridMap;
        this.defaultStrengthOfSimpleEnemy = builder.defaultStrengthOfSimpleEnemy;
        this.defaultNameOfSimpleEnemy = builder.defaultNameOfSimpleEnemy;
    }

    public Integer getDefaultMagnitudeOfGridMap() {
        return defaultMagnitudeOfGridMap;
    }

    public String getDefaultNameOfGridMap() {
        return defaultNameOfGridMap;
    }

    public String getDefaultNameOfSimpleEnemy() {
        return defaultNameOfSimpleEnemy;
    }

    public Integer getDefaultStrengthOfSimpleEnemy() {
        return defaultStrengthOfSimpleEnemy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(getDefaultMagnitudeOfGridMap(), that.getDefaultMagnitudeOfGridMap()) && Objects.equals(getDefaultNameOfGridMap(), that.getDefaultNameOfGridMap()) && Objects.equals(getDefaultStrengthOfSimpleEnemy(), that.getDefaultStrengthOfSimpleEnemy()) && Objects.equals(getDefaultNameOfSimpleEnemy(), that.getDefaultNameOfSimpleEnemy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDefaultMagnitudeOfGridMap(), getDefaultNameOfGridMap(), getDefaultStrengthOfSimpleEnemy(), getDefaultNameOfSimpleEnemy());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Configuration.class.getSimpleName() + "[", "]")
                .add("defaultMagnitudeOfGridMap=" + defaultMagnitudeOfGridMap)
                .add("defaultNameOfGridMap='" + defaultNameOfGridMap + "'")
                .add("defaultStrengthOfSimpleEnemy=" + defaultStrengthOfSimpleEnemy)
                .add("defaultNameOfSimpleEnemy='" + defaultNameOfSimpleEnemy + "'")
                .toString();
    }

    public static class Builder {
        private Integer defaultMagnitudeOfGridMap;
        private String defaultNameOfGridMap;
        private Integer defaultStrengthOfSimpleEnemy;
        private String defaultNameOfSimpleEnemy;
        public Builder defaultMagnitudeOfGridMap(Integer defaultMagnitudeOfGridMap) {
            this.defaultMagnitudeOfGridMap = defaultMagnitudeOfGridMap;
            return this;
        }

        public Builder defaultNameOfGridMap(String defaultNameOfGridMap) {
            this.defaultNameOfGridMap = defaultNameOfGridMap;
            return this;
        }

        public Builder defaultStrengthOfSimpleEnemy(Integer defaultStrengthOfSimpleEnemy) {
            this.defaultStrengthOfSimpleEnemy = defaultStrengthOfSimpleEnemy;
            return this;
        }

        public Builder defaultNameOfSimpleEnemy(String defaultNameOfSimpleEnemy) {
            this.defaultNameOfSimpleEnemy = defaultNameOfSimpleEnemy;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

}
