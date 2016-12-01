package com.example.u410.musicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.ButterKnife;

/**
 * Created by kryguu on 01.12.2016.
 */

public class NewPlaylistFragment extends Fragment {

    private NewPlaylistItemAdapter mPlaylistAdapter;
    private ArrayList<String> mPlaylist = new ArrayList<>();
    private ListView mPlaylistListView;

    public NewPlaylistFragment() {
    }

    public static NewPlaylistFragment getInstance(int i) {
        NewPlaylistFragment fragment = new NewPlaylistFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explorer_listview, container, false);
        mPlaylistAdapter = new NewPlaylistItemAdapter(mPlaylist);
        mPlaylistListView = (ListView) rootView.findViewById(R.id.explorer_list);
        mPlaylistListView.setAdapter(mPlaylistAdapter);
        mPlaylistAdapter.notifyDataSetChanged();
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
