package com.example.u410.musicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
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
    @BindView(R.id.nextButton) Button nextButton;
    @BindView(R.id.prevButton) Button prevButton;
    @BindView(R.id.playlistActivityButton) FloatingActionButton playlistActivityButton;
    @BindView(R.id.appBarImage) ImageView appBarImage;
    @BindView(R.id.randomCheckBox) CheckBox randomCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        notificationManager_ = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        playerIntent_ = new Intent(this, PlayerService.class);
        startService(playerIntent_);
        bindService(playerIntent_, playerServiceConnection, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManager_.cancelAll();
    }

    protected void init() {
        setOnTrackProgressListener();
        setOnSeekBarChangeListener();
        setOnNextButtonClickListener();
        setOnPrevButtonClickListener();
        setOnPlaylistActivityButtonClick();
        setOnTrackEndListener();
        setOnRandomCheckBoxCheckListener();
    }

    protected void setActivityStartingData() {
        if (getIntent().getParcelableArrayListExtra("PLAYLIST") !=  null) {
            ArrayList<Track> playlist = getIntent().getParcelableArrayListExtra("PLAYLIST");
            player_.setPlaylist(playlist);
            int playingTrackIndex =  getIntent().getIntExtra("TRACK_INDEX", 0);
            player_.setPlayingTrackIndex(playingTrackIndex);

            setTrack(player_.getPlayingTrackIndex(), true);
            player_.play();

            getIntent().removeExtra("PLAYLIST"); // Prevent setting playlist again on rotation
        }

        else {
            setTrack(player_.getPlayingTrackIndex(), false);
        }

        togglePlayButtonText();
        randomCheckBox.setChecked(player_.getRandomState());

        if (player_.isPlaying()) {
            showNotification();
        }
    }

    protected void setTrack(int trackIndex, boolean setTrack) {
        Track track = player_.getPlaylist().get(trackIndex);

        String artist = track.getArtistName() != null ? track.getArtistName() : "Unknown artist";
        String trackName_ = track.getTrackName() != null ? track.getTrackName() : "Unknown track";

        artistName.setText(artist);
        trackName.setText(trackName_);

        if (track.getPicture() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(track.getPicture(), 0, track.getPicture().length);
            trackPicture.setImageBitmap(bitmap);

            appBarImage.setImageBitmap(bitmap);
        }
        else {
            appBarImage.setImageResource(R.drawable.music);
        }

        if (setTrack) {
            player_.setTrack(track);
        }

        if (player_.isPlaying()) {
            showNotification();
        }

        setDuration();
    }

    protected void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Track currentlyPlaying = player_.getPlaylist().get(player_.getPlayingTrackIndex());

        Intent resultIntent = new Intent(this, PlayerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PlayerActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(false);
        builder.setOngoing(true);

        String artistName_ = currentlyPlaying.getArtistName() != null ? currentlyPlaying.getArtistName() : "Unknown artist";
        String trackName_ = currentlyPlaying.getTrackName() != null ? currentlyPlaying.getTrackName() : "Unknown track";

        builder.setContentTitle(artistName_)
            .setContentText(trackName_)
            .setSmallIcon(R.drawable.music_note);


        notificationManager_.notify(1, builder.build());
    }

    @OnClick(R.id.playButton)
    protected void playButtonClick() {
        if (!player_.isPlaying()) {
            player_.play();
            showNotification();
        }
        else {
            player_.pause();
            notificationManager_.cancelAll();
        }

        togglePlayButtonText();
    }

    protected void togglePlayButtonText() {
        if (player_.isPlaying()) {
            playButton.setText("PAUSE");
        }
        else {
            playButton.setText("PLAY");
        }
    }

    protected void setDuration() {
        long durationMs = (long) player_.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) - minutes * 60;

        String minutesString = minutes < 10 ? "0" + minutes : "" + minutes;
        String secondsString = seconds < 10 ? "0" + seconds : "" + seconds;

        duration.setText(minutesString + ":" + secondsString);
    }

    protected void setCurrentPosition(long position) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(position);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(position) - minutes * 60;

        String minutesString = minutes < 10 ? "0" + minutes : "" + minutes;
        String secondsString = seconds < 10 ? "0" + seconds : "" + seconds;

        currentPosition.setText(minutesString + ":" + secondsString);
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

    protected void setOnRandomCheckBoxCheckListener() {
        randomCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_.setRandomState(randomCheckBox.isChecked());
            }
        });
    }

    protected void setOnTrackEndListener() {
        player_.setOnTrackEndListener(new OnTrackEndListener() {
            @Override
            public void onTrackEnd() {
                nextTrack();
                setTrack(player_.getPlayingTrackIndex(), true);
            }
        });
    };

    protected void setOnNextButtonClickListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTrack();
                setTrack(player_.getPlayingTrackIndex(), true);
            }
        });
    }

    protected void setOnPrevButtonClickListener() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (randomCheckBox.isChecked()) {
                    player_.setPlayingTrackIndex(player_.getPrevPlayingTrackIndex());
                }
                else if (player_.getPlayingTrackIndex() > 0) {
                    player_.setPlayingTrackIndex(player_.getPlayingTrackIndex() -1);
                }
                else {
                    player_.setPlayingTrackIndex(player_.getPlaylist().size() - 1);
                }

                setTrack(player_.getPlayingTrackIndex(), true);
            }
        });
    }

    protected void setOnPlaylistActivityButtonClick() {
        playlistActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            player_ = binder.getService();

            setActivityStartingData();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    protected void nextTrack() {
        if (randomCheckBox.isChecked()) {
            player_.setPlayingTrackIndex(getRandomTrackIndex());
            return;
        }

        if (player_.getPlayingTrackIndex() < player_.getPlaylist().size() - 1) {
            player_.setPlayingTrackIndex(player_.getPlayingTrackIndex() + 1);
        }
        else {
            player_.setPlayingTrackIndex(0);
        }
    }

    protected int getRandomTrackIndex() {
        Random random = new Random();
        int nextTrackIndex = player_.getPlayingTrackIndex();

        while (nextTrackIndex == player_.getPlayingTrackIndex()) {
            nextTrackIndex = random.nextInt(player_.getPlaylist().size());
        }

        return nextTrackIndex;
    }

    private Intent playerIntent_;

    private PlayerService player_;

    private NotificationManager notificationManager_;
}
