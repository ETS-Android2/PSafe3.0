package com.example.psafe.ui.tips;


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


import com.example.psafe.data.model.Tips;


        import pl.droidsonroids.gif.GifImageButton;

public class TipRecyclerViewAdapter extends RecyclerView.Adapter<TipRecyclerViewAdapter.TipsViewHolder> {

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

    ArrayList<Tips> tips;
    Context context;

    public TipRecyclerViewAdapter(Context context, ArrayList<Tips> tips) {
        this.context = context;
        this.tips = tips;
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tips_layout, parent, false);

        TipsViewHolder holder = new TipsViewHolder(view, new MyClickListener() {

            @Override
            public void onLike(int p) {
                // Implement your functionality for onlike here
                view.findViewById(R.id.button_like_tips).setBackgroundResource(R.drawable.baseline_thumb_up_alt_24);
                view.findViewById(R.id.button_dislike_tips).setBackgroundResource(R.color.white);

            }

            @Override
            public void onDislike(int p) {
                // Implement your functionality for onDelete here
                view.findViewById(R.id.button_dislike_tips).setBackgroundResource(R.drawable.baseline_thumb_down_alt_24);
                view.findViewById(R.id.button_like_tips).setBackgroundResource(R.color.white);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        Tips oneTips = tips.get(position);


        if(isZh(context));
        //holder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_no_24);
        //holder.dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_up_no_24);
        holder.tipsTitle.setText(oneTips.getTip());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tipsTitle;
        View rootView;
        GifImageButton likeButton;
        GifImageButton dislikeButton;
        MyClickListener listener;



        public TipsViewHolder(View view, MyClickListener listener) {
            super(view);
            // Define click listener for the ViewHolder's View
            tipsTitle = view.findViewById(R.id.card_tips);
            rootView = view;
            likeButton = view.findViewById(R.id.button_like_tips);
            dislikeButton = view.findViewById(R.id.button_dislike_tips);
            this.listener = listener;
            likeButton.setOnClickListener(this);
            dislikeButton.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_like_tips:
                    listener.onLike(this.getLayoutPosition());
                    break;
                case R.id.button_dislike_tips:
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

