package com.example.u410.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
{
    private Playlist myPlaylist;
    private ListView myListView;
    private float begx;
    private int pos, counter = 0;
    static final int MIN_DISTANCE = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this, TabExplorerActivity.class);
        //startActivity(intent);

        initialize();
    }

    private void initialize()
    {
        //creates text if listview is empty
        myListView = (ListView)findViewById(R.id.playlistView);
        TextView emptyText = (TextView)findViewById(R.id.empty);
        myListView.setEmptyView(emptyText);

        //adds onclick event
        final FloatingActionButton myAddTracksButton = (FloatingActionButton)findViewById(R.id.addTracksButton);
        myAddTracksButton.setOnClickListener(clickHandler);

        //lets Playlist object manage listview
        myPlaylist = new Playlist(this);
        myListView.setAdapter(myPlaylist.getAdapter());

        myListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()== MotionEvent.ACTION_DOWN)
                {
                    begx = event.getX();
                    pos = getPosition((int) event.getX(), (int) event.getY());
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    int position = getPosition((int) event.getX(), (int) event.getY());
                    if(abs(begx - event.getX()) > 150 && position>-1)
                    {
                        myPlaylist.deleteTrack(position);
                        myPlaylist.savePlaylist();
                    }
                    else if(pos != position)
                    {
                        myPlaylist.moveTrack(pos, position);
                    }
                    else
                    {
                        //start player here
                        Log.e("click", "item clicked");
                    }
                }
                return true;
            }
        });


    }

    View.OnClickListener clickHandler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
           // Intent intent = new Intent(this, TabExplorerActivity.class);
            //startActivity(intent);

            myPlaylist.addTrack("new track" + String.valueOf(counter++));
            //or myPlaylist.addTracks(whole array);
            myPlaylist.savePlaylist();
        }
    };

    public int getPosition(int x, int y)
    {
        return myListView.pointToPosition(x, y);
    }
}
