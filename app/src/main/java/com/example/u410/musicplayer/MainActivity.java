package com.example.u410.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
{
    private Playlist myPlaylist;
    private ListView myListView;
    private float begx, begy;
    private int pos, posb, sortPos;
    private boolean isSorted = false, isSortedTwo = false;
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
        myListView = (ListView)findViewById(R.id.playlistView);
        TextView emptyText = (TextView)findViewById(R.id.empty);
        myListView.setEmptyView(emptyText);

        //adds onclick event
        final FloatingActionButton myAddTracksButton = (FloatingActionButton)findViewById(R.id.addTracksButton);
        myAddTracksButton.setOnClickListener(clickHandler);

        //lets Playlist object manage listview
        myPlaylist = new Playlist(this);
        myListView.setAdapter(myPlaylist.getAdapter());

        myListView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()== MotionEvent.ACTION_DOWN)
                {
                    handler.postDelayed(mLongPressed, 900);
                    begx = event.getX();
                    begy = event.getY();
                    pos = getPosition((int) event.getX(), (int) event.getY());

                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    handler.removeCallbacks(mLongPressed);
                    if(isSorted)
                    {
                        isSorted = false;
                        isSortedTwo = true;
                        return true;
                    }
                    posb = getPosition((int) event.getX(), (int) event.getY());
                    if(abs(begx - event.getX()) > 200 && posb>-1)
                    {
                        myPlaylist.deleteTrack(posb);
                        return true;
                    }
                    else if(abs(begy - event.getY()) < 40)
                    {
                        if(isSortedTwo)
                        {
                            myPlaylist.moveTrack(sortPos, posb);
                            isSortedTwo = false;
                            return true;
                        }
                        else
                        {
                            if(posb==-1)
                                return true;

                            //delete this
                            Log.e("click", "item clicked");
                            //array of tracks: myPlaylist.getTracklist();
                            //clickedTrack = myPlaylist.getTrack(posb);
                            //also tracklist provides method to get track:
                            //myPlaylist.getTracklist().get(posb);

                            //intent.putExtra("playlist", myPlaylist.getTrackList());

                            //start player here
                            Intent intent = new Intent(getBaseContext(), PlayerActivity.class);
                            intent.putExtra("PLAYLIST", myPlaylist.getTracklist());
                            intent.putExtra("TRACK_INDEX", posb);
                            startActivity(intent);

                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }


    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable()
    {
        public void run()
        {
            isSorted = true;
            sortPos = pos;
        }
    };


    View.OnClickListener clickHandler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, TabExplorerActivity.class);
            startActivityForResult(intent, 33);
        }
    };


    public int getPosition(int x, int y)
    {
        return myListView.pointToPosition(x, y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 33 & resultCode == Activity.RESULT_OK & data != null) {
            ArrayList<String> paths = data.getExtras().getStringArrayList(IntentExtras.PLAYLIST);
            Track tempTrack;
            for (String path : paths) {
                try {
                    tempTrack = new Track(path);
                    myPlaylist.addTrack(tempTrack);
                }
                catch (Exception e) {}
            }
            myPlaylist.getAdapter().notifyDataSetChanged();
        }
    }
}
