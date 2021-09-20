package com.example.psafe;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.psafe.data.model.ProximityAlertReceiver;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TestActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    private static final long POINT_RADIUS = 10; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;


    private static final String PROX_ALERT_INTENT =
            "com.psafe.android.lbs.ProximityAlert";


    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATE,
                MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                (LocationListener) new MyLocationListener()
        );


    }




public class MyLocationListener implements LocationListener {
    public void onLocationChanged(Location location) {


        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        latLngArrayList.add(new LatLng(-37.8152, 144.9542));
        latLngArrayList.add(new LatLng(-37.8152, 144.9543));


        for (int i = 0; i < latLngArrayList.size(); i++) {
            Location location1 = new Location("POINT_LOCATION");
            location1.setLatitude(latLngArrayList.get(i).getLatitude());
            location1.setLongitude(latLngArrayList.get(i).getLongitude());
            if (location.distanceTo(location1) < 5) {
                // Toast.makeText(this, "near dangerous zone", Toast.LENGTH_LONG).show();
            }

        }
            /*if(distance < 5) {
                Toast.makeText(TestActivity.this,
                        "Distance from Point:" + distance, Toast.LENGTH_LONG).show();
            }

             */
    }

    public void onStatusChanged(String s, int i, Bundle b) {
    }

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }
}
}
