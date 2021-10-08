package com.example.psafe.ui.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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
import java.util.List;
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
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.psafe.R;
import com.example.psafe.data.model.Features;
import com.example.psafe.data.model.News;
import com.example.psafe.data.model.Traffic;
import com.example.psafe.database.Repository;
import com.example.psafe.databinding.ActivityLoadingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

public class LoadingActivity extends AppCompatActivity implements PermissionsListener {

    private ProgressBar progressBar;
    private ActivityLoadingBinding activityLoadingBinding;


    @SuppressWarnings({"MissingPermission"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        View view = activityLoadingBinding.getRoot();
        setContentView(view);

        progressBar = activityLoadingBinding.loadingProgressBar;
        progressBar.setVisibility(View.VISIBLE);



        new Thread(new Runnable() {
            public void run() {
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                startActivity(new Intent(LoadingActivity.this, BottomActivity.class));
            }
        }).start();


    }

    @Override
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {

    }


}
