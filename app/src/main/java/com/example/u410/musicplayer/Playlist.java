package com.example.u410.musicplayer;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;


public class Playlist
{
    private Context ctx;
    private ArrayList<Track> tracklist;
    private ListTrackAdapter adapter;

    public Playlist(Context context)
    {
        ctx = context;
        tracklist = new ArrayList<Track>();
        adapter = new ListTrackAdapter(ctx, R.id.playlistView, tracklist);
        readPlaylist();
    }

    public ListTrackAdapter getAdapter()
    {
        return adapter;
    }
    public ArrayList<Track> getTracklist() { return tracklist; }
    public Track getTrack(int position)
    {
        return tracklist.get(position);
    }


    public void addTracks(ArrayList<Track> trackslist)
    {
        for(Track item : trackslist)
            tracklist.add(item);
        adapter.notifyDataSetChanged();
        savePlaylist();
    }
    public void addTrack(Track track)
    {
        tracklist.add(track);
        adapter.notifyDataSetChanged();
        saveOneTrack(track);
    }
    public void deleteTrack(int position)
    {
        tracklist.remove(position);
        adapter.notifyDataSetChanged();
        savePlaylist();
    }
    public void moveTrack(int initialPos, int finalPos)
    {
        Track item = getTrack(initialPos);
        if(finalPos>-1)
        {
            tracklist.remove(initialPos);
            tracklist.add(finalPos, item);
        }
        else
        {
            tracklist.remove(initialPos);
            tracklist.add(item);
        }
        adapter.notifyDataSetChanged();
        savePlaylist();
    }


    public void savePlaylist()
    {
        try
        {
            cleanFile();
            FileOutputStream fos = ctx.openFileOutput("appPlaylist.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            for(Track item : tracklist)
                outputStreamWriter.write(item.getPath() + ',');
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void saveOneTrack(Track item)
    {
        try
        {
            FileOutputStream fos = ctx.openFileOutput("appPlaylist.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(item.getPath() + ',');
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void readPlaylist()
    {
        try
        {
            InputStream inStream = ctx.openFileInput("appPlaylist.txt");

            if ( inStream != null )
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null )
                {
                    stringBuilder.append(receiveString);
                }

                inStream.close();
                if(stringBuilder.toString().length()!=0) {
                    for (String item : stringBuilder.toString().split(","))
                    {
                        if(item.substring(item.length() - 1) == ",")
                            tracklist.add(new Track(item.substring(0, item.length() - 1)));
                        else
                            tracklist.add(new Track(item));
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        }
        catch (FileNotFoundException e)
        {
            Log.e("login activity", "File not found: " + e.toString());
        }
        catch (IOException e)
        {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    public void cleanFile()
    {
        try
        {
            FileOutputStream writer = new FileOutputStream(new File(ctx.getFilesDir(), "appPlaylist.txt"));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(writer);
            outputStreamWriter.write("");
            outputStreamWriter.close();
        }
        catch(IOException e)
        {
            Log.e("Clean file exception:", e.getMessage());
        }
    }
}
