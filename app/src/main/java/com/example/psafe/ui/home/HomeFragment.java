package com.example.psafe.ui.home;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.psafe.R;
import com.example.psafe.retrofit.SearchResponse;
import com.example.psafe.retrofit.Weather;
import com.example.psafe.ui.home.HomeViewModel;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psafe.R;
import com.example.psafe.data.model.ProximityAlertReceiver;
import com.example.psafe.data.model.Selfsaving;
import com.example.psafe.data.model.Tips;
import com.example.psafe.retrofit.SearchResponse;
import com.example.psafe.retrofit.Weather;
import com.example.psafe.ui.tips.TipRecyclerViewAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener{

    private HomeViewModel homeViewModel;
    private Button sos_button;
    private TextView searchButton;
    private RecyclerView recyclerView;
    private ArrayList<Selfsaving> test;
    private RecyclerView.Adapter homeAdapter;
    private TextView scrollingText;
    TextView cityField;
    TextView temField;
    TextView humField;

    public static String BaseUrl = "http://api.openweathermap.org";
    public static SearchResponse weatherResponse;


    String locationLatitude = "10.123";
    String locationLongitude = "33";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private RecyclerView.LayoutManager homeLayoutManager;
    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View rootView = inflater.inflate(R.layout.homeone_layout, container, false);

        sp = getActivity().getSharedPreferences("homeSp", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("fromHome", "no");
        boolean commit = edit.commit();




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


        cityField = rootView.findViewById(R.id.home_card_2_city);
        temField = rootView.findViewById(R.id.home_card_2_tem);
        humField = rootView.findViewById(R.id.home_card_2_hum);
        searchButton = rootView.findViewById(R.id.map_input_home);
        sos_button = rootView.findViewById(R.id.SOS_button);
        scrollingText = rootView.findViewById(R.id.home_text1);
        scrollingText.setSelected(true);


        sos_button.setOnLongClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:000"));
            startActivity(intent);
            return true;
        });

        searchButton.setOnClickListener(v->{
            edit.putString("fromHome", "yes");
            edit.commit();
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_home_to_nav_navigation);
        });









        /*
        recyclerView = rootView.findViewById(R.id.HomeRecyclerView);
        recyclerView.setHasFixedSize(true);

        test = new ArrayList<>();


        //layout manager
        homeLayoutManager = new LinearLayoutManager(getContext());
        //recyclerView.setLayoutManager(homeLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        homeAdapter = new HomeRecyclerViewAdapter(getContext(),test);
        //Log.d(TAG,galleryViewModel.getAllNews().get(0).getId());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    test.add(child.getValue(Selfsaving.class));
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
               // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        homeViewModel.getRepository().getmDatabase().child("selfSaving").orderByChild("id").addValueEventListener(postListener);

        recyclerView.setAdapter(homeAdapter);
        */

        return rootView;
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

                    cityField.setText(weatherResponse.name+ ", " + weatherResponse.sys.country +"\n");

                    humField.setText(
                            getString(R.string.humidity) +"\n"+
                                    weatherResponse.items.humidity + "%");
                    //weatherResponse.items.temp
                    double a = weatherResponse.items.temp -  273.15;
                    temField.setText(getString(R.string.temperature) +"\n"+
                            String.format("%.2f", a) + " ℃");

                    DateFormat df = DateFormat.getDateTimeInstance();
                    String updatedOn = df.format(new Date((long) (weatherResponse.dt *1000)));


                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                cityField.setText(t.getMessage());
            }
        });



    }




    @Override
    public void onStart() {
        super.onStart();
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

        // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
}
