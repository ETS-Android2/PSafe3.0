package com.example.psafe;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import static com.google.gson.internal.$Gson$Types.arrayOf;

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
        else
        {

            Log.w("bottom","yes");
            Intent it3 = new Intent(BottomActivity.this,NotificationService.class);
            startService(it3);
        }

        if(!isIgnoringBatteryOptimizations())
            requestIgnoreBatteryOptimizations();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

            Log.v("permission in serverse","nono");
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }


        notificationId = 0;
    //    accidentLocations.add(new AccidentLocation(-37.8152,144.9542));
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try{
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+ getPackageName()));
            startActivity(intent);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
/*
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


 */
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
        Log.w("lifecycle","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*
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


         */






        /*
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATE,
                MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                (LocationListener) new MyLocationListener(accidentLocations, builder)
        );

         */
        Log.w("lifecycle","onPouse");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /*
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo =
                pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }


     */

    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (background) {
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            boolean foreground = false, background = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //foreground permission allowed
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        Toast.makeText(getApplicationContext(), "Foreground location permission allowed", Toast.LENGTH_SHORT).show();
                        continue;
                    } else {
                        Toast.makeText(getApplicationContext(), "Location Permission denied", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        background = true;
                        Toast.makeText(getApplicationContext(), "Background location location permission allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Background location location permission denied", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            if (foreground) {
                if (background) {
                    handleLocationUpdates();
                } else {
                    handleForegroundLocationUpdates();
                }
            }
        }
    }

    private void handleLocationUpdates() {
        //foreground and background
        Toast.makeText(getApplicationContext(),"Start Foreground and Background Location Updates",Toast.LENGTH_SHORT).show();
    }

    private void handleForegroundLocationUpdates() {
        //handleForeground Location Updates
        Toast.makeText(getApplicationContext(),"Start foreground location updates",Toast.LENGTH_SHORT).show();
    }

}