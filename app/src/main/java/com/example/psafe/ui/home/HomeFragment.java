package com.example.psafe.ui.home;

import android.app.AlertDialog;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.psafe.R;
import com.example.psafe.retrofit.SearchResponse;
import com.example.psafe.retrofit.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment implements ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    private HomeViewModel homeViewModel;

    public static String BaseUrl = "http://api.openweathermap.org";
    public static SearchResponse weatherResponse;


    String locationLatitude = "10.123";
    String locationLongitude = "33";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    //TextView textView;
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    GifImageView weatherIcon;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        //textView = root.findViewById(R.id.text_home);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (GifImageView) rootView.findViewById(R.id.weather_icon);
        //weatherIcon.setTypeface(weatherFont);

        return rootView;
    }

    @Override
    @SuppressWarnings({"MissingPermission"})

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Log.v("weather1","weatjer");
            retrofit();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        retrofit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Weather service = retrofit.create(Weather.class);
        Call<SearchResponse> call = service.getCurrentWeatherData("" + currentLatitude, "" + currentLongitude);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if (response.code() == 200) {
                    weatherResponse = response.body();
                    assert weatherResponse != null;

                    cityField.setText(weatherResponse.name+ ", " + weatherResponse.sys.country);

                    detailsField.setText(
                            weatherResponse.weather.get(0).description.toUpperCase(Locale.US) +
                                    "\n" + "Humidity: " + weatherResponse.items.humidity + "%" +
                                    "\n" + "Pressure: " + weatherResponse.items.pressure + " hPa");
                    //weatherResponse.items.temp
                    double a = weatherResponse.items.temp -  273.15;
                    currentTemperatureField.setText(
                            String.format("%.2f", a) + " ℃");

                    DateFormat df = DateFormat.getDateTimeInstance();
                    String updatedOn = df.format(new Date((long) (weatherResponse.dt *1000)));
                    updatedField.setText("Last update: " + updatedOn);

                    setWeatherIcon(weatherResponse.weather.get(0).id,
                            (int)weatherResponse.sys.sunrise * 1000,
                            (int)weatherResponse.sys.sunrise * 1000);

                    //textView.setText(stringBuilder);

                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                cityField.setText(t.getMessage());
            }
        });



    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;




        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                weatherIcon.setBackgroundResource(R.drawable.sunny);
            } else {
                weatherIcon.setBackgroundResource(R.drawable.clearnight);
            }
        } else {
            switch (id) {
                case 2:
                    weatherIcon.setBackgroundResource(R.drawable.thunder);
                    break;
                case 3:
                    weatherIcon.setBackgroundResource(R.drawable.drizzle);
                    break;
                case 7:
                    weatherIcon.setBackgroundResource(R.drawable.foggy);
                    break;
                case 8:
                    weatherIcon.setBackgroundResource(R.drawable.cloudy);
                    break;
                case 6:
                    weatherIcon.setBackgroundResource(R.drawable.snowy);
                    break;
                case 5:
                    weatherIcon.setBackgroundResource(R.drawable.rainy);
                    break;
            }
        }
    }


}
