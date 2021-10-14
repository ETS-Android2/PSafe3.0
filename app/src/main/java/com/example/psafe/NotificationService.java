package com.example.psafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.psafe.data.model.AccidentLocation;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NotificationService extends Service implements PermissionsListener {
    SharedPreferences sp;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    LocationManager locationManager;
    public ArrayList<AccidentLocation> accidentLocations = new ArrayList<>();

    String channelId = "psafe";
    Integer notificationId;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {

    }


    class MyBinder extends Binder {
        //Activity绑定之后可以调用的方法
        public void play(){
            Log.e("m_tag","===play===");
        }
        public NotificationService getService(){
            return NotificationService.this;
        }

    }
    @Override
    public String toString() {
        return "MusicService ---- toString";
    }
    //第一次创建时触发
    @Override
    public void onCreate() {
        super.onCreate();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        notificationId = 0;
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
        //    accidentLocations.add(new AccidentLocation(-37.8152,144.9542));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "psafe")
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

            Log.v("permission in serverse","nono");
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Log.v("permission in serverse","yes");
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATE,
                MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                (LocationListener) new MyLocationListener(accidentLocations, builder)
        );
        Log.e("m_tag","MusicService == onCreate");
    }
    //每次startService都会触发
    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //服务处理事情的业务
        //intent可以获取绑定传递过来的参数、序列化参数等……C
        Log.e("m_tag","====onStartCommand===");
        return START_STICKY;
    }

    //销毁时触发
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("m_tag","====onDestroy===");
    }

    //接触绑定调用的方法
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("m_tag","MusicService == onUnbind");
        return true;
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

            if(sp.getBoolean("alert",false)) {
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


}