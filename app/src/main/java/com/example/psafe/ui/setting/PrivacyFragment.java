package com.example.psafe.ui.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psafe.R;
import com.github.barteksc.pdfviewer.PDFView;


public class PrivacyFragment extends Fragment {


    PDFView pdfView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        pdfView = view.findViewById(R.id.pdfView);

        pdfView.fromAsset("privacy.pdf").load();
        return view;
    }
}