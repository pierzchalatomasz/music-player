package com.example.u410.musicplayer;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

        @BindView(R.id.delete_button)
        Button deleteButton;

        public PlaylistHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    ArrayList<String> mPlaylist;
    TabExplorerActivity mTabExplorerActivity;

    public NewPlaylistItemAdapter(TabExplorerActivity tabExplorerActivity) {
        mTabExplorerActivity = tabExplorerActivity;
        mPlaylist = new ArrayList<>();
        mPlaylist.add("a");
        mPlaylist.add("dwa");
        mTabExplorerActivity.setmPlaylist(mPlaylist);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlaylistHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_playlist_item_layout, parent, false);

            holder = new PlaylistHolder(convertView);

            convertView.setTag(holder);
        }
        else {
            holder = (NewPlaylistItemAdapter.PlaylistHolder) convertView.getTag();
        }

        ArrayList<String> tempPlaylist = mTabExplorerActivity.getmPlaylist();

        holder.songNameTextView.setText(tempPlaylist.get(position));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> tempPlaylist = mTabExplorerActivity.getmPlaylist();
                tempPlaylist.remove(position);
                NewPlaylistItemAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
