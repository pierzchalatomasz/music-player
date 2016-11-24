package com.example.u410.musicplayer;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kryguu on 23.11.2016.
 */

public class ExplorerListItemAdapter extends BaseAdapter {

    static class ViewHolder {
        @BindView(R.id.fileName)
        TextView fileName;
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    List<String> mFiles;

    public ExplorerListItemAdapter(List<String> files) {
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

            holder = new ViewHolder(convertView);
            //holder.fileName = (TextView) convertView.findViewById(R.id.fileName);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fileName.setText(mFiles.get(position));
        String path = (String) getItem(position);
        File file = new File(path);
        holder.image.setImageResource(R.drawable.folder);
        if (file.isDirectory() == false) {
            holder.image.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
