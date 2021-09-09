package com.example.psafe.data.model;


public class Selfsaving {
    private String id;
    private String step;

    public Selfsaving(String id, String step) {
        this.id = id;
        this.step = step;
    }

    public Selfsaving() {
        this.id = "id";
        this.step = "step";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}