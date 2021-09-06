package com.example.psafe.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.psafe.R;
import com.example.psafe.databinding.FragmentDashboardBinding;
import com.example.psafe.ui.gallery.GalleryFragment;
import com.example.psafe.ui.gallery.GalleryViewModel;
import com.example.psafe.ui.login.LoginActivity;
import com.example.psafe.ui.login.SignupActivity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    FragmentDashboardBinding fragmentDashboardBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fragmentDashboardBinding = FragmentDashboardBinding.inflate(getLayoutInflater());
        View root = fragmentDashboardBinding.getRoot();



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tips
        fragmentDashboardBinding.dashboardTipsButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_dashboardFragment_to_nav_tips);
        });

        //news
        fragmentDashboardBinding.dashboardNewsButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_dashboard_to_nav_gallery);
        });

        //weather
        fragmentDashboardBinding.dashboardWeatherButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_dashboard_to_nav_weather);
        });

        fragmentDashboardBinding.testCardButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_dashboard_to_nav_weather);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


}