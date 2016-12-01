package com.example.u410.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kryguu on 01.12.2016.
 */

public class NewPlaylistItemAdapter extends BaseAdapter {

    static class PlaylistHolder {
        @BindView(R.id.song_name)
        TextView songNameTextView;

        public PlaylistHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    List<String> mPlaylist;

    public NewPlaylistItemAdapter(ArrayList<String> playlist) {
        // zmienić/usunąć argumenty konstruktora!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //mPlaylist = playlist;
        mPlaylist = new ArrayList<>();
        mPlaylist.add("a");
        mPlaylist.add("dwa");
    }

    @Override
    public int getCount() {
        return mPlaylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlaylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaylistHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_playlist_item_layout, parent, false);

            holder = new PlaylistHolder(convertView);
            //holder.fileName = (TextView) convertView.findViewById(R.id.fileName);

            convertView.setTag(holder);
        }
        else {
            holder = (NewPlaylistItemAdapter.PlaylistHolder) convertView.getTag();
        }

        holder.songNameTextView.setText(mPlaylist.get(position));

        return convertView;
    }

    @OnClick(R.id.delete_button)
    public void deleteButtonClick(View v) {
    }
}
