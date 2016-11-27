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
    //private ArrayAdapter<String> mAdapter;
    private ExplorerListItemAdapter mAdapter;
    private ListView mExplorerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFiles);
        mAdapter = new ExplorerListItemAdapter(mFiles);

        mExplorerListView = (ListView) findViewById(R.id.explorer_list);

        mExplorerListView.setAdapter(mAdapter);

        getFiles();

        ButterKnife.bind(this);
        mAdapter.notifyDataSetChanged();
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
        String clickedFileName = (String) mAdapter.getItem(position);
        String tempPath = createPathOfClickedFile(clickedFileName);
        tryGoToPath(tempPath);
        //Clicking twice on directory with no permissions will crash an app (fileName will append to mPath but will not go to that path)
    }

    private String createPathOfClickedFile(String clickedFileName) {
        String tempPath;

        if (clickedFileName == ".."){
            File file = new File(mPath);
            tempPath = file.getParentFile().getPath();
        }
        else {
            tempPath = mPath + "/" + clickedFileName;
        }

        return tempPath;
    }

    private void tryGoToPath(String path) {
        File file = new File(path);
        try {
            if (file.isDirectory()) {
                mPath = path;
                getFiles();
                mAdapter.setmPath(mPath);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d("Wrong directory","No permissions to this directory!");
        }
    }
}
