package com.example.psafe.data.model;

import java.util.ArrayList;
import java.util.HashSet;

public class Traffic {

    ArrayList<Features> features;

    public Traffic() {
        features = new ArrayList<>();
    }

    public Traffic(ArrayList<Features> features) {
        this.features = features;
    }

    public ArrayList<Features> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Features> features) {
        this.features = features;
    }
}
