package com.example.u410.musicplayer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by U410 on 2016-12-04.
 */

public class PlayerService extends Service {
    public PlayerService() {
        super();
        player_ = new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return playerBind_;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class PlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    public void setTrack(Track track) {
        player_.reset();
        track_ = track;
        emittedTrackEnd_ = false;

        try {
            player_.setDataSource(track_.getPath());
            player_.prepare();

            if (wasPlaying_) {
                play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        player_.start();
        wasPlaying_ = true;
        trackProgressHandler_.postDelayed(trackProgressRunnable, 1000);
    }

    public void pause() {
        wasPlaying_ = false;
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

    public void setOnTrackEndListener(OnTrackEndListener onTrackEndListener) {
        onTrackEndListener_ = onTrackEndListener;
    }

    public void seekTo(int msec) {
        player_.seekTo(msec);
    }

    public ArrayList<Track> getPlaylist() {
        return playlist_;
    }

    public void setPlaylist(ArrayList playlist) {
        playlist_ = playlist;
    }

    public int getPlayingTrackIndex() {
        return playingTrackIndex_;
    }

    public void setPlayingTrackIndex(int index) {
        prevPlayingTrackIndex_ = playingTrackIndex_;
        playingTrackIndex_ = index;
    }

    public int getPrevPlayingTrackIndex() {
        return prevPlayingTrackIndex_;
    }

    public void setRandomState(boolean random) {
        random_ = random;
    }

    public boolean getRandomState() {
        return random_;
    }

    private Track track_;

    private MediaPlayer player_;

    private OnTrackProgressListener onTrackProgressListener_;

    private Handler trackProgressHandler_ = new Handler();

    private Handler trackEndHandler_ = new Handler();

    private Runnable trackEndRunnable_ = new Runnable() {
        @Override
        public void run() {
            if (onTrackEndListener_ != null) {
                onTrackEndListener_.onTrackEnd();
            }
        }
    };

    private Runnable trackProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (onTrackProgressListener_ != null) {
                int currentPosition = player_.getCurrentPosition();
                onTrackProgressListener_.onTrackProgress(currentPosition);

                if (getDuration() - currentPosition <= 1000 && !emittedTrackEnd_) {
                    emittedTrackEnd_ = true;
                    trackEndHandler_.postDelayed(trackEndRunnable_, 1000);
                }
            }

            trackProgressHandler_.postDelayed(this, 100);
        }
    };

    private final IBinder playerBind_ = new PlayerBinder();

    private OnTrackEndListener onTrackEndListener_;

    private ArrayList<Track> playlist_;

    private int playingTrackIndex_;

    private int prevPlayingTrackIndex_;

    private boolean random_;

    private boolean emittedTrackEnd_ = false;

    boolean wasPlaying_;
}
