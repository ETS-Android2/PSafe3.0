package com.example.psafe.retrofit;

import com.google.gson.annotations.SerializedName;

public class WeatherOne {
    @SerializedName("id")
      public int  id;

    @SerializedName("main")
    public String main;

    @SerializedName("description")
    public String    description;

    @SerializedName("icon")
    public String icon;

}
