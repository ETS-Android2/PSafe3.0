package com.example.psafe.retrofit;

import com.google.gson.annotations.SerializedName;

public class Items {


    @SerializedName("temp")
    public float temp;
    @SerializedName("temp_min")
    public float minTemp;
    @SerializedName("temp_max")
    public float maxTemp;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;


}