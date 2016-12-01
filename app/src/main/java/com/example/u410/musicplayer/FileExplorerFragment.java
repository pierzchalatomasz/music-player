package com.example.u410.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class FileExplorerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<String> mFiles = new ArrayList<>();
    private ArrayList<File> mFileFile = new ArrayList<>();
    private String mPath = "/storage";//Environment.getExternalStorageDirectory().toString();
    private ExplorerListItemAdapter mExplorerAdapter;
    private ListView mExplorerListView;

    public FileExplorerFragment() {
    }

    public static FileExplorerFragment getInstance(int i) {
        FileExplorerFragment fragment = new FileExplorerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explorer_listview, container, false);
        mExplorerAdapter = new ExplorerListItemAdapter(mFiles);
        mExplorerListView = (ListView) rootView.findViewById(R.id.explorer_list);
        mExplorerListView.setAdapter(mExplorerAdapter);
        //getAllFiles(mPath, mFileFile);
        //getMp3FileNames(mFileFile, mFiles);
        getContentFromPath(mPath, mFiles);
        mExplorerAdapter.notifyDataSetChanged();
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void getAllFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath(), files);
                }
            }
        }
    }

    public void getMp3FileNames(ArrayList<File> files, ArrayList<String> filesNames) {
        filesNames.clear();
        String extension;
        for (File file : files) {
            extension = file.getName().toString().substring(file.getName().toString().lastIndexOf(".") + 1, file.getName().toString().length());
            if (extension.equals("mp3")) {
                filesNames.add(file.getName().toString());
            }
        }
    }

    public void getContentFromPath(String path, ArrayList<String> filesNames) {
        File directory = new File(path);
        File[] files = directory.listFiles();

        filesNames.clear();
        filesNames.add("..");
        for (int i = 0; i < files.length; i++) {
            filesNames.add(files[i].getName().toString());
        }
    }

    @OnItemClick(R.id.explorer_list)
    public void onExplorerListViewItemClick(ListView parent, View view, int position, long id) {

        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
        cb.setChecked(!cb.isChecked());
        mExplorerAdapter.notifyDataSetChanged();


        String clickedFileName = (String) mExplorerAdapter.getItem(position);
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
                getContentFromPath(mPath, mFiles);
                mExplorerAdapter.setmPath(mPath);
                mExplorerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d("Wrong directory","No permissions to this directory!");
        }
    }
}