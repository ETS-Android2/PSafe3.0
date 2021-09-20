package com.example.psafe.ui.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psafe.BottomActivity;
import com.example.psafe.R;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.example.psafe.databinding.FragmentDashboardBinding;
import com.example.psafe.databinding.FragmentNavigationBinding;
import com.example.psafe.ui.dashboard.DashboardViewModel;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
//import com.mapbox.mapboxandroiddemo.R;

import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.turf.TurfMeta;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class NavigationFragment extends Fragment implements
        OnMapReadyCallback, OnLocationClickListener, PermissionsListener, OnCameraTrackingChangedListener, View.OnClickListener {

    private FragmentNavigationBinding binding;

    private NavigationViewModel mViewModel;

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private MapView mapView;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private Point origin;
    private Point destination;
    private Style.Builder thisStyleBuilder;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";



    FeatureCollection featureCollection;
    private GeoJsonSource source;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        binding = FragmentNavigationBinding.inflate(getLayoutInflater());
        mViewModel =
                new ViewModelProvider(this).get(NavigationViewModel.class);
        View root = binding.getRoot();
        // This contains the MapView in XML and needs to be called after the access token is configured.
        // Setup the MapView
        thisStyleBuilder = new Style.Builder().fromUri("mapbox://styles/chesterhu0008/cksq0gwaw0rt417jrbggk3x1j");
        mapView = binding.mapViewNew;
        mapView.onCreate(savedInstanceState);
        iniButton();
        //new LoadGeoJsonDataTask().execute();
        mapView.getMapAsync(this);

        Toast.makeText(getContext(), getString(R.string.red_point_instruction), Toast.LENGTH_LONG).show();


        return root;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;



        mapboxMap.setStyle(thisStyleBuilder, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSearchFab();
                //addUserLocations();


                enableLocationComponent(style);
            }
        });

    }



    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }


    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

        // Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.outline_place_24)));
        // Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, 0f})));
    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param mapboxMap   the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination, String type) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(type)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                //Timber.d("Response code: " + response.code());
                if (response.body() == null) {
                    // Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    //Timber.e("No routes found");
                    return;
                }

                // Get the directions route
                currentRoute = response.body().routes().get(0);

                // Make a toast which displays the route's distance
                Toast.makeText(getContext(), "Distance: " + currentRoute.distance().toString() + "m", Toast.LENGTH_SHORT).show();

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Retrieve and update the source designated for showing the directions route
                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                            // Create a LineString with the directions route's geometry and
                            // reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                //Timber.e("Error: " + throwable.getMessage());
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


//------------------------------CURRENT LOCATION------------------------------------------------------------

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getContext())
                    .build();

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);

            // Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onLocationComponentClick() {
        if (locationComponent.getLastKnownLocation() != null) {
            Toast.makeText(getContext(), String.format(getString(R.string.current_location),
                    locationComponent.getLastKnownLocation().getLatitude(),
                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onCameraTrackingDismissed() {
        //isInTrackingMode = false;
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }


//-----------------------------------------------------------SEARCH--------------------------------------

    private void initSearchFab() {
        binding.mapInput.setOnClickListener(this);
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            binding.mapInput.setText(selectedCarmenFeature.text());
            binding.destinationText.setText(selectedCarmenFeature.text());

// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

// Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);

                    this.destination = (Point) selectedCarmenFeature.geometry();


                    origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLatitude());

                    binding.navigationMenu.setVisibility(View.VISIBLE);
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull MapboxMap mapboxMap) {
                            mapboxMap.setStyle(thisStyleBuilder, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {

                                    // enableLocationComponent(style);
                                    origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());
                                    initSource(style);
                                    initLayers(style);
                                    // Get the directions route from the Mapbox Directions API
                                    getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_WALKING);


                                    // move the camera to fix the route

                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(origin.latitude(), origin.longitude())) // orange
                                            .include(new LatLng(destination.latitude(), destination.longitude())) // destination
                                            .build();


                                    binding.walkButton.setImageResource(R.drawable.ic_baseline_directions_walk_24_purple);
                                    binding.carButton.setImageResource(R.drawable.ic_baseline_directions_car_24);
                                    binding.bikeButton.setImageResource(R.drawable.ic_baseline_directions_bike_24);


                                    mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 1000);


                                }
                            });
                        }
                    });

                    //bike
                    binding.bikeButton.setOnClickListener(v -> {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                mapboxMap.setStyle(thisStyleBuilder, new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

                                        origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());
                                        initSource(style);
                                        initLayers(style);
                                        // Get the directions route from the Mapbox Directions API
                                        getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_CYCLING);

                                        // move the camera to fix the route

                                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                                .include(new LatLng(origin.latitude(), origin.longitude())) // orange
                                                .include(new LatLng(destination.latitude(), destination.longitude())) // destination
                                                .build();

                                        binding.walkButton.setImageResource(R.drawable.ic_baseline_directions_walk_24);
                                        binding.carButton.setImageResource(R.drawable.ic_baseline_directions_car_24);
                                        binding.bikeButton.setImageResource(R.drawable.ic_baseline_directions_bike_24_purple);

                                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 1000);


                                    }
                                });
                            }
                        });
                    });


                    //walk
                    binding.walkButton.setOnClickListener(v -> {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                mapboxMap.setStyle(thisStyleBuilder, new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

                                        // enableLocationComponent(style);
                                        origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());
                                        initSource(style);
                                        initLayers(style);
                                        // Get the directions route from the Mapbox Directions API
                                        getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_WALKING);

                                        // move the camera to fix the route

                                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                                .include(new LatLng(origin.latitude(), origin.longitude())) // orange
                                                .include(new LatLng(destination.latitude(), destination.longitude())) // destination
                                                .build();

                                        binding.walkButton.setImageResource(R.drawable.ic_baseline_directions_walk_24_purple);
                                        binding.carButton.setImageResource(R.drawable.ic_baseline_directions_car_24);
                                        binding.bikeButton.setImageResource(R.drawable.ic_baseline_directions_bike_24);

                                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 1000);


                                    }
                                });
                            }
                        });
                    });


                    //car
                    binding.carButton.setOnClickListener(v -> {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                mapboxMap.setStyle(thisStyleBuilder, new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

                                        // enableLocationComponent(style);
                                        origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());
                                        initSource(style);
                                        initLayers(style);
                                        // Get the directions route from the Mapbox Directions API
                                        getRoute(mapboxMap, origin, destination, DirectionsCriteria.PROFILE_DRIVING);

                                        // move the camera to fix the route

                                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                                .include(new LatLng(origin.latitude(), origin.longitude())) // orange
                                                .include(new LatLng(destination.latitude(), destination.longitude())) // destination
                                                .build();
                                        binding.walkButton.setImageResource(R.drawable.ic_baseline_directions_walk_24);
                                        binding.carButton.setImageResource(R.drawable.ic_baseline_directions_car_24_purple);
                                        binding.bikeButton.setImageResource(R.drawable.ic_baseline_directions_bike_24);

                                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 1000);

                                    }
                                });
                            }
                        });
                    });



                    binding.startNavButton.setOnClickListener(v -> {
                        setCameraTrackingMode(CameraMode.TRACKING);
                        binding.navigationMenu.setVisibility(View.GONE);
                        binding.navMenu2.setVisibility(View.VISIBLE);
                        binding.testCardButton.setVisibility(View.GONE);
                    });


                    binding.helpNavButton.setOnLongClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:000"));
                        startActivity(intent);
                        return true;
                    });

                    binding.endNavButton.setOnClickListener(v -> {
                        NavController navController = Navigation.findNavController(v);
                        navController.navigate(R.id.action_nav_navigation_to_safeFragment);

                    });
                }
            }
        }
    }


    private void iniButton() {
        binding.walkButton.setImageResource(R.drawable.ic_baseline_directions_walk_24);
        binding.carButton.setImageResource(R.drawable.ic_baseline_directions_car_24_purple);
        binding.bikeButton.setImageResource(R.drawable.ic_baseline_directions_bike_24);
        // setCameraTrackingMode(CameraMode.NONE);
        binding.navigationMenu.setVisibility(View.GONE);
        binding.navMenu2.setVisibility(View.GONE);
        binding.testCardButton.setVisibility(View.VISIBLE);
    }


    //---------------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        //((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the Directions API request
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setCameraTrackingMode(@CameraMode.Mode int mode) {
        locationComponent.setCameraMode(mode, new OnLocationCameraTransitionListener() {
            @Override
            public void onLocationCameraTransitionFinished(@CameraMode.Mode int cameraMode) {
                if (mode != CameraMode.NONE) {
                    locationComponent.zoomWhileTracking(15, 750, new MapboxMap.CancelableCallback() {
                        @Override
                        public void onCancel() {
// No impl
                        }

                        @Override
                        public void onFinish() {
                            locationComponent.tiltWhileTracking(45);
                        }
                    });
                } else {
                    mapboxMap.easeCamera(CameraUpdateFactory.tiltTo(0));
                }
            }

            @Override
            public void onLocationCameraTransitionCanceled(@CameraMode.Mode int cameraMode) {
// No impl
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.map_input:
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .bbox(139, -40, 151, -34)
                                .addInjectedFeature(mViewModel.home)
                                .addInjectedFeature(mViewModel.work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
                break;


        }


    }




    /**
     * AsyncTask to load data from the assets folder.
     */
    private static class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {


        NavigationFragment fragment;
        @Override
        protected FeatureCollection doInBackground(Void... params) {

            String geoJson = loadGeoJsonFromAsset(fragment,"earthquakes.geojson");
            return FeatureCollection.fromJson(geoJson);
        }

        @Override
        protected void onPostExecute(FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);



// This example runs on the premise that each GeoJSON Feature has a "selected" property,
// with a boolean value. If your data's Features don't have this boolean property,
// add it to the FeatureCollection 's features with the following code:

/*            for (Feature singleFeature : featureCollection.features()) {
                singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
            }



            activity.setUpData(featureCollection);
            new BottomActivity.GenerateViewIconTask(activity).execute(featureCollection);

 */
        }

        static String loadGeoJsonFromAsset(Fragment fragment, String filename) {
            try {
// Load GeoJSON file from local asset folder
                InputStream is = fragment.getActivity().getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer, Charset.forName("UTF-8"));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }



    /**
     * Utility class to generate Bitmaps for Symbol.
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }

    /*
    public void setUpData(final FeatureCollection collection) {
        featureCollection = collection;
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                setupSource(style);
                setUpImage(style);
                setUpMarkerLayer(style);
                setUpInfoWindowLayer(style);
            });
        }
    }


     */
    /**
     * Adds the GeoJSON source to the map
     */


    /**
     * Updates the display of data on the map after the FeatureCollection has been modified
     */
    private void refreshSource() {
        if (source != null && featureCollection != null) {
            source.setGeoJson(featureCollection);
        }
    }


}