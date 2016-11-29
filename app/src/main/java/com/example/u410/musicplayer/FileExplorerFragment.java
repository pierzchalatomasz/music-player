package com.example.u410.musicplayer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.u410.musicplayer.ExplorerListItemAdapter;
import com.example.u410.musicplayer.R;
import com.example.u410.musicplayer.TabExplorerActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class FileExplorerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<String> mFiles = new ArrayList<>();
    private String mPath = Environment.getRootDirectory().toString();
    private ExplorerListItemAdapter mAdapter;
    private ListView mExplorerListView;

    public FileExplorerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FileExplorerFragment newInstance(int sectionNumber) {
        FileExplorerFragment fragment = new FileExplorerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explorer_listview, container, false);
        mAdapter = new ExplorerListItemAdapter(mFiles);
        mExplorerListView = (ListView) rootView.findViewById(R.id.explorer_list);
        mExplorerListView.setAdapter(mAdapter);

        getFiles();
        mAdapter.notifyDataSetChanged();
        ButterKnife.bind(this, rootView);
        return rootView;
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
    public void onExplorerListViewItemClick(ListView parent, View view, int position, long id) {

        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
        cb.setChecked(!cb.isChecked());
        mAdapter.notifyDataSetChanged();


        String clickedFileName = (String) mAdapter.getItem(position);
        String tempPath = createPathOfClickedFile(clickedFileName);
        tryGoToPath(tempPath);
        mExplorerListView.setSelectionAfterHeaderView();
        //Clicking twice on directory with no permissions will crash an app (fileName will append to mPath but will not go to that path)
    }

    private String createPathOfClickedFile(String clickedFileName) {
        String tempPath;

        if (clickedFileName.equals("..")){
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