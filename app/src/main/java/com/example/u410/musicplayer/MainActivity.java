package com.example.u410.musicplayer;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private Playlist myPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize()
    {
        //creates text if listview is empty
        ListView myListView = (ListView)findViewById(R.id.playlistView);
        TextView emptyText = (TextView)findViewById(R.id.empty);
        myListView.setEmptyView(emptyText);

        //adds onclick event
        FloatingActionButton myAddTracksButton = (FloatingActionButton)findViewById(R.id.addTracksButton);
        myAddTracksButton.setOnClickListener(clickHandler);

        //lets Playlist object manage listview
        myPlaylist = new Playlist(this);
        myListView.setAdapter(myPlaylist.getAdapter());
    }

    View.OnClickListener clickHandler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            //run explorer activity here instead of this
            myPlaylist.addTrack("nowy utwor");
        }
    };
}
