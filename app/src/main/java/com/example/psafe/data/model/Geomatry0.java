package com.example.psafe.data.model;

import java.util.ArrayList;

public class Geomatry0 {
    ArrayList<Double> coordinates;

    public Geomatry0() {
        coordinates = new ArrayList<>();
        coordinates.add(0.0);
        coordinates.add(0.0);
    }

    public Geomatry0(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
