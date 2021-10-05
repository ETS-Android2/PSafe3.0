package com.example.psafe.data.model;

public class Features {
    Geomatry0 geometry;
    Properties0 properties;

    public Features() {
        properties = new Properties0();
        geometry = new Geomatry0();
    }

    public Features(Geomatry0 geometry, Properties0 properties) {
        this.geometry = geometry;
        this.properties = properties;
    }

    public Geomatry0 getGeometry() {
        return geometry;
    }

    public void setGeometry(Geomatry0 geometry) {
        this.geometry = geometry;
    }

    public Properties0 getProperties() {
        return properties;
    }

    public void setProperties(Properties0 properties) {
        this.properties = properties;
    }
}
