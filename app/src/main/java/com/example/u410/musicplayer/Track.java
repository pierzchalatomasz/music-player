package com.example.u410.musicplayer;

import android.media.MediaMetadataRetriever;

/**
 * Created by U410 on 2016-11-22.
 */

public class Track {
    public Track(String path) {
        path_ = path;
        meta_ = new MediaMetadataRetriever();
        meta_.setDataSource(path);
    }

    public String getArtistName() {
        return meta_.extractMetadata(meta_.METADATA_KEY_ARTIST);
    }

    public String getTrackName() {
        return meta_.extractMetadata(meta_.METADATA_KEY_TITLE);
    }

    public String getAlbumName() {
        return meta_.extractMetadata(meta_.METADATA_KEY_ALBUM);
    }

    public String getGenre() {
        return meta_.extractMetadata(meta_.METADATA_KEY_GENRE);
    }

    public byte[] getPicture() {
        return meta_.getEmbeddedPicture();
    }

    public String getDuration() {
        return meta_.extractMetadata(meta_.METADATA_KEY_DURATION);
    }

    public String getPath() {
        return path_;
    }

    private MediaMetadataRetriever meta_;

    private String path_;
}
