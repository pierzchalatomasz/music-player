package com.example.u410.musicplayer;

import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by U410 on 2016-11-22.
 */

public class Track implements Parcelable{
    public Track(String path) {
        path_ = path;
        meta_ = new MediaMetadataRetriever();
        meta_.setDataSource(path);
    }

    protected Track(Parcel in) {
        path_ = in.readString();
        meta_ = new MediaMetadataRetriever();
        meta_.setDataSource(path_);
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

    public byte[] getPicture() { return meta_.getEmbeddedPicture(); }

    public String getDuration() {
        return meta_.extractMetadata(meta_.METADATA_KEY_DURATION);
    }

    public String getPath() {
        return path_;
    }

    private MediaMetadataRetriever meta_;

    private String path_;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path_);
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[0];
        }
    };
}
