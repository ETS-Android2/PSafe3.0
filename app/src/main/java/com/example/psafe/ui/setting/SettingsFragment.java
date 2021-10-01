package com.example.psafe.ui.setting;

import android.os.Bundle;
import com.example.psafe.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
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