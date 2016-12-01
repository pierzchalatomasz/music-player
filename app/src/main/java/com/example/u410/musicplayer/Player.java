package com.example.u410.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by U410 on 2016-12-01.
 */

public class Player {
    public Player() {
        player_ = new MediaPlayer();
    }

    public void setTrack(Track track) {
        track_ = track;

        try {
            player_.setDataSource(track_.getPath());
            player_.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        player_.start();
        trackProgressHandler_.postDelayed(trackProgressRunnable, 1000);
    }

    public void pause() {
        player_.pause();
    }

    public boolean isPlaying() {
        return player_.isPlaying();
    }

    public int getDuration() {
        return player_.getDuration();
    }

    public void setOnTrackProgressListener(OnTrackProgressListener onTrackProgressListener) {
        onTrackProgressListener_ = onTrackProgressListener;
    }

    public void seekTo(int msec) {
        player_.seekTo(msec);
    }

    private Track track_;

    private MediaPlayer player_;

    private OnTrackProgressListener onTrackProgressListener_;

    private Handler trackProgressHandler_ = new Handler();

    private Runnable trackProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if(onTrackProgressListener_ != null) {
                int currentPosition = player_.getCurrentPosition();
                onTrackProgressListener_.onTrackProgress(currentPosition);
            }

            trackProgressHandler_.postDelayed(this, 100);
        }
    };
}
