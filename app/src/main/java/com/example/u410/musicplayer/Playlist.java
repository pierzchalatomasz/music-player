package com.example.u410.musicplayer;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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
        adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, tracklist);
        readPlaylist();
    }

    public ArrayAdapter<String> getAdapter()
    {
        return adapter;
    }

    public void addTracks(ArrayList<String> trackslist)
    {
        for(String item : trackslist)
            tracklist.add(item);
        adapter.notifyDataSetChanged();
    }

    public void addTrack(String track)
    {
        tracklist.add(track);
        adapter.notifyDataSetChanged();
    }

    public void deleteTrack(int position)
    {
        tracklist.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void moveTrack(int initialPos, int finalPos)
    {
        String item = getTrack(initialPos);
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
    }

    public String getTrack(int position)
    {
        return tracklist.get(position);
    }

    public void savePlaylist()
    {
        try
        {
            cleanFile();
            FileOutputStream fos = ctx.openFileOutput("appPlaylist.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            for(String item : tracklist)
                outputStreamWriter.write(item + ',');
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
                            addTrack(item.substring(0, item.length() - 1));
                        else
                            addTrack(item);
                    }
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
