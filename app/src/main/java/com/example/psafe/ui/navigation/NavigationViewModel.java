package com.example.psafe.ui.navigation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class NavigationViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    CarmenFeature home;
    CarmenFeature work;
    LiveData<LatLng> latLngLiveData;

    public NavigationViewModel() {
        addUserLocations();

    }

    private void addUserLocations() {
        home = CarmenFeature.builder().text("Home")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Work")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }





}