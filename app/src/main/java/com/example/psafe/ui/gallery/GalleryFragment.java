package com.example.psafe.ui.gallery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psafe.R;
import com.example.psafe.data.model.News;
import com.example.psafe.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private static final String TAG = "read 12312";
    private GalleryViewModel galleryViewModel;
    private RecyclerView.Adapter newsAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private RecyclerView recyclerView;

    SharedPreferences sp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView titleTextView = root.findViewById(R.id.news_title);
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        recyclerView = root.findViewById(R.id.recyclerView);





        recyclerView.setHasFixedSize(true);

        ArrayList<News> test = new ArrayList<News>();
        test.add(new News("test news 1"));
        test.add(new News("test news 2"));
        test.add(new News("test news 3"));
        test.add(new News("test news 4"));
        test.add(new News("test news 5"));
        Log.d(TAG,test.get(0).getId());
        //layout manager
        newsLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(newsLayoutManager);

        newsAdapter = new NewsRecyclerViewAdapter(test);
        //newsAdapter = new NewsRecyclerViewAdapter(galleryViewModel.getAllNews());
        //Log.d(TAG,galleryViewModel.getAllNews().get(0).getId());



        recyclerView.setAdapter(newsAdapter);




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fix size


    }
}