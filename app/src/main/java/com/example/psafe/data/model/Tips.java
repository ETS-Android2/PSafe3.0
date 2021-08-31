package com.example.psafe.data.model;

public class Tips {
    private String id;
    private String source;
    private String tip;

    public Tips(String id, String source, String tip) {
        this.id = id;
        this.source = source;
        this.tip = tip;
    }

    public Tips() {
        id = "id";
        source = "source";
        tip = "tips";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
