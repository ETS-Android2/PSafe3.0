package com.example.psafe.ui.tips;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psafe.R;
import com.example.psafe.data.model.Tips;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TipsFragment extends Fragment {

    private static final String TAG = "gallery";
    private TipsViewModel tipsViewModel;
    private RecyclerView.Adapter tipsAdapter;
    private RecyclerView.LayoutManager tipsLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Tips> test;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        View root = inflater.inflate(R.layout.tips_fragment, container, false);
        tipsViewModel =
                new ViewModelProvider(this).get(TipsViewModel.class);
        recyclerView = root.findViewById(R.id.tipsRecyclerView);
        recyclerView.setHasFixedSize(true);

        test = new ArrayList<>();


        //layout manager
        tipsLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(tipsLayoutManager);
        tipsAdapter = new TipRecyclerViewAdapter(getContext(),test);
        //Log.d(TAG,galleryViewModel.getAllNews().get(0).getId());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    test.add(child.getValue(Tips.class));
                    //test.get(test.size()-1).setImage(galleryViewModel.getRepository().getStorage().getReference().child("images").child(child.getValue(News.class).getId()).getPath().toString());
                    //Log.v(TAG,test.get(test.size()-1).getImage());
                }
                tipsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        tipsViewModel.getRepository().getmDatabase().child("tips").addValueEventListener(postListener);

        recyclerView.setAdapter(tipsAdapter);

        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fix size


    }
}