package com.example.psafe.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResponse {
    @SerializedName("main")
    public Items items;

    @SerializedName("name")
    public String name;

    @SerializedName("sys")

    public Sys sys;

    @SerializedName("weather")

    public ArrayList<WeatherOne> weather;

    @SerializedName("dt")

    public float dt;
}

