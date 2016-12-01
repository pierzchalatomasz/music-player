package com.example.u410.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends AppCompatActivity {
    @BindView(R.id.artistName) TextView artistName;
    @BindView(R.id.trackName) TextView trackName;
    @BindView(R.id.trackPicture) ImageView trackPicture;
    @BindView(R.id.playButton) Button playButton;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(50);

        setData();
        init();
    }

    protected void init() {
        setOnTrackProgressListener();
    }

    protected void setData() {
        String path = "/storage/sdcard/Android/data/com.example.u410.musicplayer/files/Music/Stereophonics - Mr and Mrs Smith.mp3";
        Track track = new Track(path);

        artistName.setText(track.getArtistName());
        trackName.setText(track.getTrackName());

        if (track.getPicture() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(track.getPicture(), 0, track.getPicture().length);
            trackPicture.setImageBitmap(bitmap);
        }

        player_.setTrack(track);
    }

    @OnClick(R.id.playButton)
    protected void playButtonClick() {
        if (!player_.isPlaying()) {
            player_.play();
        }
        else {
            player_.pause();
        }
    }

    protected void setOnTrackProgressListener() {
        player_.setOnTrackProgressListener(new OnTrackProgressListener() {
            @Override
            public void onTrackProgress(int currentPosition) {
                float progress = (float)currentPosition / (float)player_.getDuration() * 100;
                progressBar.setProgress((int) progress);
            }
        });
    }

    private Player player_ = new Player();
}
