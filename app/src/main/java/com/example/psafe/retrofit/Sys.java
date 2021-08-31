package com.example.psafe.retrofit;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("id")

    public int id;
    @SerializedName("message")

    public String message;
    @SerializedName("country")

    public String country;
    @SerializedName("sunrise")

    public float sunrise;
    @SerializedName("sunset")

    public float sunset;


}
