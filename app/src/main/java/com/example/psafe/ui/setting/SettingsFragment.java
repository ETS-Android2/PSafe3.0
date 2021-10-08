package com.example.psafe.ui.setting;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.psafe.R;
import com.example.psafe.ui.login.LoginActivity;
import com.example.psafe.ui.login.SignupActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    Preference privacyPreference;
    Preference guidePreference;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        guidePreference = findPreference("guide");

        privacyPreference = findPreference("privacy");

        guidePreference.setOnPreferenceClickListener(preference -> {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_nav_setting_to_guideFragmentOne);
            return true;
        });
        privacyPreference.setOnPreferenceClickListener(preference -> {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_nav_setting_to_privacyFragment);
            return true;
        });

        SwitchPreferenceCompat switchPreferenceCompat = findPreference("alert");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("alert", switchPreferenceCompat.isChecked());
        editor.commit();

        Log.w("setting", String.valueOf(sharedPreferences.getBoolean("alert",false)));

        switchPreferenceCompat.setOnPreferenceChangeListener((preference, newValue) -> {
            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                SharedPreferences.Editor editor1 = sharedPreferences.edit();

                editor.putBoolean("alert", !switchPreferenceCompat.isChecked());
                editor.commit();
                Log.w("setting", String.valueOf(sharedPreferences1.getBoolean("alert",true)));

                return true;
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}