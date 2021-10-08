package com.example.psafe;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.MenuItem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Location;
import android.location.LocationListener;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.psafe.data.model.AccidentLocation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

/**
 * main activty
 */
public class BottomActivity extends AppCompatActivity implements PermissionsListener {

    SharedPreferences sp;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    LocationManager locationManager;
    public ArrayList<AccidentLocation> accidentLocations = new ArrayList<>();

    String channelId = "psafe";
    Integer notificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        BottomNavigationView navView = findViewById(R.id.nav_view);



        notificationId = 0;
        accidentLocations.add(new AccidentLocation(-37.8152,144.9542));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_navigation, R.id.nav_setting, R.id.nav_dashboard)
                .build();



        try {
            InputStream is = getAssets().open("only_coors.csv");
            if (is != null) {
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while (buffreader.readLine() != null) {

                    String tempLine = buffreader.readLine();
                    String tempLocationLon = tempLine.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("]", "").split(",")[0];
                    String tempLocationLat = tempLine.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("]", "").split(",")[1];

                    accidentLocations.add(new AccidentLocation(Double.parseDouble(tempLocationLat.trim()), Double.parseDouble(tempLocationLon.trim())));

                    if (buffreader.readLine() == null) {
                        break;
                    }
                }

            }


        } catch (IOException e) {
            Log.e("Error", e + "");

        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                        .setContentTitle(getString(R.string.channel_name))
                        .setContentText(getString(R.string.nearDangerousZone))
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        Intent notificationIntent = new Intent(getApplicationContext(), BottomActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATE,
                MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                (LocationListener) new MyLocationListener(accidentLocations, builder)
        );



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

    @Override
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {

    }


    @SuppressWarnings({"MissingPermission"})

    public class MyLocationListener implements LocationListener {
        private ArrayList<AccidentLocation> accidentLocationArrayList;
        private NotificationCompat.Builder builder;

        public MyLocationListener(ArrayList<AccidentLocation> accidentLocationArrayList, NotificationCompat.Builder builder) {
            this.accidentLocationArrayList = accidentLocationArrayList;
            this.builder = builder;
        }

        @SuppressWarnings({"MissingPermission"})
        public void onLocationChanged(Location location) {


            createNotificationChannel();

            if(sp.getBoolean("alert",true)) {
                boolean isFindHarzardZone = false;

                for (int i = 0; i < accidentLocationArrayList.size() && !isFindHarzardZone; i++) {


                    Location location1 = new Location("POINT_LOCATION");
                    location1.setLatitude(accidentLocationArrayList.get(i).getLatitude());
                    location1.setLongitude(accidentLocationArrayList.get(i).getLongitude());
                    Log.w("loadingString", "" + location.distanceTo(location1));
                    if (location.distanceTo(location1) < 100) {
                        isFindHarzardZone = true;
                        Log.w("loadingString", "weixian weixian");
                        Toast.makeText(getApplicationContext(), getString(R.string.nearDangerousZone), Toast.LENGTH_LONG).show();
                        // Add as notification
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(notificationId, builder.build());
                        notificationId++;


                    }

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

    /*   public class ReadAccident extends AsyncTask<String, Integer, ArrayList<AccidentLocation>> {

           private Context context;

           public ReadAccident(Context context) {
               this.context = context;
           }

           protected ArrayList<AccidentLocation> doInBackground(String... inputString) {
               ArrayList<AccidentLocation> list = new ArrayList<>();
               try {
                   InputStream is = context.getAssets().open("only_coors.csv");
                   if (is != null) {
                       String search = inputString[0].toString();
                       InputStreamReader inputreader = new InputStreamReader(is,
                               "UTF-8");
                       BufferedReader buffreader = new BufferedReader(inputreader);
                       int antallTreff = 0;

                       while (buffreader.readLine() != null) {


                           AccidentLocation accidentLocation = new AccidentLocation();
                           String [] tempLocation = buffreader.readLine().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","").split(",");
                           for (int i = 0; i < tempLocation.length; i++) {
                               if(i == 1)
                                   accidentLocation.setLatitude(Double.parseDouble(tempLocation[1]));
                               else
                                   accidentLocation.setLongitude(Double.parseDouble(tempLocation[0]));
                           }
                           if (buffreader.readLine() == null) {
                               break;
                           }
                       }

                   }



               } catch (IOException e) {
                   Log.e("Error", e + "");

               }

               return list;
           }
           protected void onProgressUpdate(Integer... progress) {
               //setProgressPercent(progress[0]);
           }

           protected void onPostExecute(ArrayList<AccidentLocation> accidentLocations) {

           }
       }

       public void setAccidentLocations(ArrayList<AccidentLocation> accidentLocations) {
           this.accidentLocations = accidentLocations;
       }

     */
    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
    }

}