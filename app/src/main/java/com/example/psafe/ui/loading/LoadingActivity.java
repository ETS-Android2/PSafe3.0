package com.example.psafe.ui.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.location.Location;
import android.location.LocationListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.psafe.BottomActivity;
import com.example.psafe.data.model.AccidentLocation;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.psafe.R;
import com.example.psafe.data.model.Features;
import com.example.psafe.data.model.News;
import com.example.psafe.data.model.Traffic;
import com.example.psafe.database.Repository;
import com.example.psafe.databinding.ActivityLoadingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoadingActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    LocationManager locationManager;
    public ArrayList<AccidentLocation> accidentLocations = new ArrayList<>();
    ArrayList<Features> features;
    int progressStatus = 0;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    String channelId = "psafe";
    private ActivityLoadingBinding activityLoadingBinding;
    //Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        View view = activityLoadingBinding.getRoot();
        setContentView(view);
       /*
        features = new ArrayList<>();

        repository = new Repository();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    features.add(dataSnapshot.getValue(Features.class));
                    //test.get(test.size()-1).setImage(galleryViewModel.getRepository().getStorage().getReference().child("images").child(child.getValue(News.class).getId()).getPath().toString());
                    //Log.v(TAG,test.get(test.size()-1).getImage());
                }

                for(int i = 0; i < features.size(); i++)
                {
                    if (features.get(i).getProperties().getACCIDENT_T().equals("2"))
                    {
                        accidentLocations.add(new AccidentLocation(features.get(i).getGeometry().getCoordinates().get(1),features.get(i).getGeometry().getCoordinates().get(0)));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
               // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };



            repository.getmDatabase().child("traffic").child("features").addValueEventListener(postListener);



        */

        //ArrayList<AccidentLocation> accidentLocations = new ArrayList<>();
        accidentLocations.add(new AccidentLocation(-37.8152,144.9542));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progressBar = activityLoadingBinding.loadingProgressBar;
        progressBar.setVisibility(View.VISIBLE);


        new Thread(new Runnable() {
            public void run() {
                // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            //progressBar.setProgress(progressStatus);
                            //textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                startActivity(new Intent(LoadingActivity.this, BottomActivity.class));
            }
        }).start();

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



/*
        try {
            Thread.sleep(1000);
            //TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



 */
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
                (LocationListener) new MyLocationListener(accidentLocations, builder)
        );



    }

    public class MyLocationListener implements LocationListener {
        private ArrayList<AccidentLocation> accidentLocationArrayList;
        private NotificationCompat.Builder builder;

        public MyLocationListener(ArrayList<AccidentLocation> accidentLocationArrayList, NotificationCompat.Builder builder) {
            this.accidentLocationArrayList = accidentLocationArrayList;
            this.builder = builder;
        }

        public void onLocationChanged(Location location) {


            createNotificationChannel();
            //accidentLocationArrayList.add(new AccidentLocation(-37.8152,144.9542));


            boolean isFindHarzardZone = false;

            for (int i = 0; i < accidentLocationArrayList.size() && !isFindHarzardZone; i++) {


                Location location1 = new Location("POINT_LOCATION");
                location1.setLatitude(accidentLocationArrayList.get(i).getLatitude());
                location1.setLongitude(accidentLocationArrayList.get(i).getLongitude());
                Log.w("loadingString", "" + location.distanceTo(location1));
                if (location.distanceTo(location1) < 1000) {
                    isFindHarzardZone = true;
                    Log.w("loadingString", "weixian weixian");
                    Toast.makeText(getApplicationContext(), getString(R.string.nearDangerousZone), Toast.LENGTH_LONG).show();
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
}
