package com.example.u410.musicplayer;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
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
    private ArrayList<String> tracklist;
    private ArrayAdapter<String> adapter;

    public Playlist(Context context)
    {
        ctx = context;
        tracklist = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, tracklist);
        addTrack(readPlaylist());
    }

    public ArrayAdapter<String> getAdapter()
    {
        return adapter;
    }

    public void addTrack(String track)
    {
        tracklist.add(track);
        adapter.notifyDataSetChanged();
        savePlaylist(track);
    }

    public void savePlaylist(String data)
    {
        try
        {
            FileOutputStream fos = ctx.openFileOutput("appPlaylist.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public String readPlaylist()
    {
        String ret = "";

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
                ret = stringBuilder.toString();
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

        return ret;
    }
}
