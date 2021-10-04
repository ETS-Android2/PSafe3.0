package com.example.psafe;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;


import java.util.ArrayList;
import java.util.Locale;

import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;


import com.example.psafe.data.model.AccidentLocation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.geometry.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/**
 * main activty
 */
public class BottomActivity extends AppCompatActivity {
/*
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 60 * 1000; // in Milliseconds
    LocationManager locationManager;


    String channelId = "psafe";


 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        BottomNavigationView navView = findViewById(R.id.nav_view);






        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_navigation, R.id.nav_setting, R.id.nav_dashboard)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);




/*

        ArrayList<AccidentLocation> accidentLocations = new ArrayList<>();
        accidentLocations.add(new AccidentLocation(-37.8152,144.9542));
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
                (LocationListener) new MyLocationListener(accidentLocations)
        );

 */
    }




        /**
         * code of back button
         * @param item
         * @return
         */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                    //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                    //navController.navigate(R.id.action_nav_tips_to_dashboardFragment);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public class MyLocationListener implements LocationListener {
        private ArrayList<AccidentLocation> accidentLocationArrayList;
        public MyLocationListener(ArrayList<AccidentLocation> accidentLocationArrayList) {
            this.accidentLocationArrayList = accidentLocationArrayList;
        }

        public void onLocationChanged(Location location) {


            createNotificationChannel();
            //accidentLocationArrayList.add(new AccidentLocation(-37.8152,144.9542));


            boolean isFindHarzardZone = false;

            for(int i = 0; i < accidentLocationArrayList.size() && !isFindHarzardZone; i++) {

                Location location1 = new Location("POINT_LOCATION");
                location1.setLatitude(accidentLocationArrayList.get(i).getLatitude());
                location1.setLongitude(accidentLocationArrayList.get(i).getLongitude());
                if(location.distanceTo(location1) < 5) {
                    isFindHarzardZone = true;
                    Toast.makeText(BottomActivity.this, getString(R.string.nearDangerousZone), Toast.LENGTH_LONG).show();



                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(BottomActivity.this, channelId)
                                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                                    .setContentTitle(getString(R.string.channel_name))
                                    .setContentText(getString(R.string.nearDangerousZone))
                                    .setVibrate(new long[] { 1000, 1000})
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


                    Intent notificationIntent = new Intent(getApplicationContext(), BottomActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());


                }

            }

        }

        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name_drawer);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        public void onStatusChanged(String s, int i, Bundle b) {
        }

        public void onProviderDisabled(String s) {
        }

        public void onProviderEnabled(String s) {
        }
    }



 */

}