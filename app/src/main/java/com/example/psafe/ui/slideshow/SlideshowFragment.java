package com.example.psafe.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.psafe.R;
import com.example.psafe.databinding.FragmentSlideshowBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private Button button;
    FragmentSlideshowBinding fragmentSlideshowBinding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        fragmentSlideshowBinding = FragmentSlideshowBinding.inflate(getLayoutInflater());
        View view = fragmentSlideshowBinding.getRoot();







        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                fragmentSlideshowBinding.textSlideshow.setText(s);
            }
        });
        return view;
    }
}