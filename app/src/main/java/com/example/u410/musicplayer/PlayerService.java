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
        boolean wasPlaying = isPlaying();

        player_.reset();
        track_ = track;

        try {
            player_.setDataSource(track_.getPath());
            player_.prepare();

            if (wasPlaying) {
                play();
            }
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

    public void setOnTrackEndListener(OnTrackEndListener onTrackEndListener) {
        onTrackEndListener_ = onTrackEndListener;
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

                if (getDuration() - currentPosition <= 100 && onTrackEndListener_ != null) {
                    onTrackEndListener_.onTrackEnd();
                }
            }

            trackProgressHandler_.postDelayed(this, 100);
        }
    };

    private final IBinder playerBind_ = new PlayerBinder();

    private OnTrackEndListener onTrackEndListener_;
}
