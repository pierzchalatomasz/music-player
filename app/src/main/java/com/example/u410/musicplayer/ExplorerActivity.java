package com.example.u410.musicplayer;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ExplorerActivity extends AppCompatActivity {

    private ArrayList<String> mFiles = new ArrayList<>();
    private String mPath = Environment.getRootDirectory().toString();
    private ArrayAdapter<String> mAdapter;
    private ListView mExplorerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFiles);

        mExplorerListView = (ListView) findViewById(R.id.explorer_list);

        mExplorerListView.setAdapter(mAdapter);

        getFiles();

        Log.d("jakis tag","jakis tekst");

        ButterKnife.bind(this);

    }

    public void getFiles() {
        File directory = new File(mPath);
        File[] files = directory.listFiles();

        mFiles.clear();
        mFiles.add("..");
        for (int i = 0; i < files.length; i++) {
            mFiles.add(files[i].getName().toString());
        }
    }

    @OnItemClick(R.id.explorer_list)
    public void onExplorerListViewItemClick(int position) {
        String clickedFile = mAdapter.getItem(position);
        mPath+=("/" + clickedFile);
        getFiles();
        mAdapter.notifyDataSetChanged();
    }
}
