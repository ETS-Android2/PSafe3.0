package com.example.psafe.database;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.psafe.data.model.News;
import com.example.psafe.ui.gallery.NewsRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final String TAG = "NewsDatabase";
    //private FirebaseFirestore db;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    FirebaseStorage storage;


    SharedPreferences sp;


    public ArrayList<News> allNewsArray;

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public Repository() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        //storage = FirebaseStorage.getInstance();

        //myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");
        allNewsArray = new ArrayList<>();


       // News oneNews1 = new News("1","test news","test content", 999,9 ,"www.baidu.com");
        //mDatabase.child("news").child(oneNews1.getId()).setValue(oneNews1);



    }

    public void writeNewUser(String id, String title, String content, int like, int dislike, String source,String image,String date) {
        News oneNews = new News(id,title,content,like,dislike,source,image,date);
        mDatabase.child("news").child(id).setValue(oneNews);
    }





        //Log.d(TAG, "bbb" + allNewsArray.get(0).getId() +"  "+allNewsArray.get(0).getContent());


    public ArrayList<News> getAllNewsArray1(NewsRecyclerViewAdapter newsAdapter){



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    allNewsArray.add( child.getValue(News.class));
                    Log.d(TAG,allNewsArray.get(0).getSource());
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };
        mDatabase.child("news").addValueEventListener(postListener);

        return allNewsArray;

    }




}
