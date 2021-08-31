package com.example.psafe.ui.maps;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.psafe.R;

import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import androidx.annotation.NonNull;

public class MapFragment extends Fragment implements
        OnMapReadyCallback, PermissionsListener {


    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;


    private static final String SINGLE_ICON_ID = "single-icon-id";
    private static final String CLUSTER_ICON_ID = "cluster-icon-id";
    private static final String SOURCE_ID = "crash";
    private static final String POINT_COUNT = "point_count";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = root.findViewById(R.id.mapView0);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;



    }




    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

// Enable the most basic pulsing styling by ONLY using
// the `.pulseEnabled()` method
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getContext())
                    .pulseEnabled(true)
                    .build();

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                    // Disable any type of fading transition when icons collide on the map. This enhances the visual
// look of the data clustering together and breaking apart.
                    style.setTransition(new TransitionOptions(0, 0, false));

                    initLayerIcons(style);
                    addClusteredGeoJsonSource(style);
                }

            });
        } else {

        }
    }


    private void initLayerIcons(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(SINGLE_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24)));
        loadedMapStyle.addImage(CLUSTER_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_baseline_trip_origin_24)));
    }

    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {
// Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            loadedMapStyle.addSource(
// Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
// 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                    new GeoJsonSource(SOURCE_ID,
                            new URI("https://vicroadsopendata-vicroadsmaps.opendata.arcgis.com/datasets/e23ee376951548e6a3848249efadf76d_0.geojson?outSR=%7B%22latestWkid%22%3A3111%2C%22wkid%22%3A102171%7D"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );
        } catch (URISyntaxException uriSyntaxException) {
           // Timber.e("Check the URL %s" , uriSyntaxException.getMessage());
        }

        SymbolLayer unclusteredSymbolLayer = new SymbolLayer("unclustered-points", SOURCE_ID).withProperties(
                iconImage(SINGLE_ICON_ID),
                iconSize(
                        division(
                                get("mag"), literal(4.0f)
                        )
                )
        );
        unclusteredSymbolLayer.setFilter(has("mag"));

//Creating a SymbolLayer icon layer for single data/icon points
        loadedMapStyle.addLayer(unclusteredSymbolLayer);

// Use the earthquakes GeoJSON source to create three point ranges.
        int[] layers = new int[] {150, 20, 0};

        for (int i = 0; i < layers.length; i++) {
//Add clusters' SymbolLayers images
            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, SOURCE_ID);

            symbolLayer.setProperties(
                    iconImage(CLUSTER_ICON_ID)
            );
            Expression pointCount = toNumber(get(POINT_COUNT));

// Add a filter to the cluster layer that hides the icons based on "point_count"
            symbolLayer.setFilter(
                    i == 0
                            ? all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i]))
                    ) : all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i])),
                            lt(pointCount, literal(layers[i - 1]))
                    )
            );
            loadedMapStyle.addLayer(symbolLayer);
        }

//Add a SymbolLayer for the cluster data number point count
        loadedMapStyle.addLayer(new SymbolLayer("count", SOURCE_ID).withProperties(
                textField(Expression.toString(get(POINT_COUNT))),
                textSize(12f),
                textColor(Color.BLACK),
                textIgnorePlacement(true),
// The .5f offset moves the data numbers down a little bit so that they're
// in the middle of the triangle cluster image
                textOffset(new Float[] {0f, .5f}),
                textAllowOverlap(true)
        ));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }



}