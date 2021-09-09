package com.example.psafe.ui.home;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.psafe.R;
import com.example.psafe.data.model.Selfsaving;

import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;

import android.widget.TextView;
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeViewHolder> {

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

    ArrayList<Selfsaving> tips;
    Context context;

    public HomeRecyclerViewAdapter(Context context, ArrayList<Selfsaving> tips) {
        this.context = context;
        this.tips = tips;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_tips_layout, parent, false);

        HomeViewHolder holder = new HomeRecyclerViewAdapter.HomeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.HomeViewHolder holder, int position) {
        Selfsaving oneTips = tips.get(position);


        if(isZh(context));
        holder.tipsTitle.setText(oneTips.getStep());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView tipsTitle;
        View rootView;
        public HomeViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tipsTitle = view.findViewById(R.id.card_home_tips);
            rootView = view;


        }

    }

}

