package com.example.psafe.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.psafe.R;
import com.example.psafe.data.model.News;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psafe.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pl.droidsonroids.gif.GifImageButton;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private static final String TAG = "adapter";



    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            Log.v("language", "zn");
            return true;
        }
        else
            return false;
    }

    ArrayList<News> news;
    Context context;

    public NewsRecyclerViewAdapter(Context context, ArrayList<News> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layoout, parent, false);

        NewsViewHolder holder = new NewsViewHolder(view, new MyClickListener() {

            @Override
            public void onLike(int p) {
                // Implement your functionality for onlike here
                view.findViewById(R.id.button_like).setBackgroundResource(R.drawable.baseline_thumb_up_alt_24);
                view.findViewById(R.id.button_dislike).setBackgroundResource(R.color.white);

            }

            @Override
            public void onDislike(int p) {
                // Implement your functionality for onDelete here
                view.findViewById(R.id.button_dislike).setBackgroundResource(R.drawable.baseline_thumb_down_alt_24);
                view.findViewById(R.id.button_like).setBackgroundResource(R.color.white);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News oneNews = news.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oneNews.getImage());

        if(isZh(context));
        //holder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_no_24);
        //holder.dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_up_no_24);
        holder.newsTitle.setText(oneNews.getTitle());
        holder.newsContent.setText(oneNews.getContent());
        Log.v(TAG,oneNews.getImage());
        // Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(context /* context */)
                .load(storageReference)
                .into(holder.newsImage);

        Log.d(TAG, oneNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView newsTitle;
        TextView newsContent;
        ImageView newsImage;
        View rootView;
        ImageButton likeButton;
        ImageButton dislikeButton;

        MyClickListener listener;



        public NewsViewHolder(View view, MyClickListener listener) {
            super(view);
            // Define click listener for the ViewHolder's View
            newsTitle = view.findViewById(R.id.card_title);
            newsContent = view.findViewById(R.id.card_content);
            //newsTitle.setTypeface(Typeface.createFromAsset(new AssetManager(),"");
            newsImage = (ImageView)view.findViewById(R.id.imageNews);
            rootView = view;
            likeButton = view.findViewById(R.id.button_like);
            dislikeButton = view.findViewById(R.id.button_dislike);

            this.listener = listener;
            likeButton.setOnClickListener(this);
            dislikeButton.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_like:
                    listener.onLike(this.getLayoutPosition());
                    break;
                case R.id.button_dislike:
                    listener.onDislike(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }
    public interface MyClickListener {
        void onLike(int p);
        void onDislike(int p);
    }
}
