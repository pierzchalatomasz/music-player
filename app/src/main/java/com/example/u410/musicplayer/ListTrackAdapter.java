package com.example.u410.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListTrackAdapter extends ArrayAdapter<Track>
{
        private ArrayList<Track> tracksList;
        private Context ctx;
        public ListTrackAdapter(Context context, int textViewResourceId, ArrayList<Track> tracks)
        {
            super(context, textViewResourceId, tracks);
            ctx = context;
            this.tracksList = tracks;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            if (v == null)
            {
                LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.tracklist_item_layout, null);
            }
            Track track = tracksList.get(position);


            if (track != null)
            {
                ImageView album = (ImageView) v.findViewById(R.id.albumPic);
                TextView title = (TextView) v.findViewById(R.id.title);
                TextView artist = (TextView) v.findViewById(R.id.artist);

                if(track.getPicture()!=null)
                {
                    Bitmap bmp = BitmapFactory.decodeByteArray(track.getPicture(), 0, track.getPicture().length);
                    album.setImageBitmap(bmp);
                    album.getLayoutParams().height = 120;
                    album.getLayoutParams().width = 120;
                    album.requestLayout();
                }

                if(track.getTrackName()!= null)
                {
                    title.setText(track.getTrackName());
                }
                else
                {
                    title.setText(track.getPath().substring(track.getPath().lastIndexOf("/")+1));
                }

                if(track.getArtistName()!=null)
                {
                    artist.setText(track.getArtistName());
                }
                else
                {
                    artist.setText("unknown");
                }
            }
            return v;
        }
}
