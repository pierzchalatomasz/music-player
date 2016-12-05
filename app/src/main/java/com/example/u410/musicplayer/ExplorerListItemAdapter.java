package com.example.u410.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;

/**
 * Created by kryguu on 23.11.2016.
 */

public class ExplorerListItemAdapter extends BaseAdapter {

    static class ViewHolder {
        @BindView(R.id.fileName)
        TextView fileName;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.add_button)
        Button addButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    TabExplorerActivity mTabExplorerActivity;
    ArrayList<String> mFiles;
    String mPath = Environment.getRootDirectory().toString();

    public void setmPath(String path) {
        this.mPath = path;
    }

    public ExplorerListItemAdapter(TabExplorerActivity tabExplorerActivity, ArrayList<String> files) {
        mTabExplorerActivity = tabExplorerActivity;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String clickedFileName = holder.fileName.getText().toString();
                String clickedFilePath = mPath + "/" + clickedFileName;
                mTabExplorerActivity.addTomPlaylist(new File(clickedFilePath));
            }
        });

        holder.fileName.setText(mFiles.get(position));
        String fileName = (String) getItem(position);
        String tempPath = mPath + "/" + fileName;
        File file = new File(tempPath);
        holder.image.setImageResource(R.drawable.folder);
        if (file.isDirectory()) {
            holder.image.setVisibility(View.VISIBLE);
            holder.addButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.image.setVisibility(View.INVISIBLE);
            holder.addButton.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
