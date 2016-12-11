package com.example.u410.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
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
        private Bitmap bmp;
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
                byte[] pic = track.getPicture();
                String trackName = track.getTrackName();
                String artistName = track.getArtistName();

                bmp = pic != null ? BitmapFactory.decodeByteArray(track.getPicture(), 0, track.getPicture().length):
                        BitmapFactory.decodeResource(ctx.getResources(), R.drawable.music_note);

                album.setImageBitmap(bmp);
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, ctx.getResources().getDisplayMetrics());
                album.getLayoutParams().height = (int) px;
                album.getLayoutParams().width = (int) px;
                album.requestLayout();

                title.setText(trackName != null ? trackName : track.getPath().substring(track.getPath().lastIndexOf("/")+1));

                artist.setText(artistName != null ? artistName : "unknown");
            }
            return v;
        }
}
