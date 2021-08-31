package com.example.psafe.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Weather {
    @GET("data/2.5/weather?q=Melbourne&appid=c33be4c47630037478a516ecd78fd804")
    Call<SearchResponse> getCurrentWeatherData();

    @GET("data/2.5/weather?appid=c33be4c47630037478a516ecd78fd804")
    Call<SearchResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon);
}