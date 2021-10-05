package com.example.psafe.ui.help;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psafe.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuideFragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideFragmentOne extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public GuideFragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuideFragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideFragmentOne newInstance(String param1, String param2) {
        GuideFragmentOne fragment = new GuideFragmentOne();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_one, container, false);
    }
}