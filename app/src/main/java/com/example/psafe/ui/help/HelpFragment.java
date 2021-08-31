package com.example.psafe.ui.help;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.psafe.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class HelpFragment extends Fragment {






    VideoView videoView;
    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"));

    int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_help, container, false);


        videoView =view.findViewById(R.id.videoView);
        final MediaController mediacontroller = new MediaController(getContext());
        mediacontroller.setAnchorView(videoView);


        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(Uri.parse(arrayList.get(index)));
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
                if (index++ == arrayList.size()) {
                    index = 0;
                    mp.release();
                    Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
                } else {
                    videoView.setVideoURI(Uri.parse(arrayList.get(index)));
                    videoView.start();
                }


            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });

        return view;
    }


}