package com.example.u410.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends AppCompatActivity {
    @BindView(R.id.artistName) TextView artistName;
    @BindView(R.id.trackName) TextView trackName;
    @BindView(R.id.trackPicture) ImageView trackPicture;
    @BindView(R.id.playButton) Button playButton;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.duration) TextView duration;
    @BindView(R.id.currentPosition) TextView currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        playerIntent_ = new Intent(this, PlayerService.class);
        startService(playerIntent_);
        bindService(playerIntent_, playerServiceConnection, this.BIND_AUTO_CREATE);


    }

    protected void init() {
        setOnTrackProgressListener();
        setOnSeekBarChangeListener();
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

        setDuration();
    }

    @OnClick(R.id.playButton)
    protected void playButtonClick() {
        if (!player_.isPlaying()) {
            player_.play();
            playButton.setText("PAUSE");
        }
        else {
            player_.pause();
            playButton.setText("PLAY");
        }
    }

    protected void setDuration() {
        long durationMs = (long) player_.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) - minutes * 60;

        duration.setText(minutes + ":" + seconds);
    }

    protected void setCurrentPosition(long position) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(position);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(position) - minutes * 60;

        currentPosition.setText(minutes + ":" + seconds);
    }

    protected void setOnTrackProgressListener() {
        player_.setOnTrackProgressListener(new OnTrackProgressListener() {
            @Override
            public void onTrackProgress(int currentPosition) {
                float progress = (float)currentPosition / (float)player_.getDuration() * 100;
                seekBar.setProgress((int)progress);
                setCurrentPosition((long)currentPosition);
            }
        });
    }

    protected void setOnSeekBarChangeListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int position = player_.getDuration() * progress / 100;
                    player_.seekTo(position);
                    Log.v("MUSIC_PLAYER", "" + position);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            player_ = binder.getService();

            setData();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Intent playerIntent_;

    private PlayerService player_;
}
