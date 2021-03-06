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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class FileExplorerFragment extends Fragment {

    private ArrayList<String> mFilesNames = new ArrayList<>();
    private ArrayList<File> mFiles = new ArrayList<>();
    private String mPath = "/storage/";//"/storage/3/Muzyka/"; //Environment.getExternalStorageDirectory().toString();
    private ArrayList<String> mExtensions = new ArrayList<>(Arrays.asList("mp3","wav"));
    private ExplorerListItemAdapter mExplorerAdapter;
    private ListView mExplorerListView;

    public FileExplorerFragment() {
    }

    public static FileExplorerFragment getInstance() {
        FileExplorerFragment fragment = new FileExplorerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explorer_listview, container, false);

        TabExplorerActivity tabExplorerActivity = (TabExplorerActivity) getActivity();

        mExplorerAdapter = new ExplorerListItemAdapter(tabExplorerActivity, mFilesNames, mPath);
        mExplorerListView = (ListView) rootView.findViewById(R.id.explorer_list);
        mExplorerListView.setAdapter(mExplorerAdapter);
        //getAllFilesNamesWithExtension(mPath, mExtensions); // gets ALL files with specified extensions
        getContentFromPath(mPath, mFilesNames); // gets only directories and files from path
        mExplorerAdapter.notifyDataSetChanged();
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void getAllFilesNamesWithExtension(String directoryName, ArrayList<String> extension) {
        getAllFiles(directoryName, mFiles);
        getOnlyFilesWithSpecifiedExtension(mFiles, mFilesNames, extension);
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

    public void getOnlyFilesWithSpecifiedExtension(ArrayList<File> files, ArrayList<String> filesNames, ArrayList<String> extensions) {
        filesNames.clear();
        getFilesWithSpecifiedExtension(files, filesNames, extensions);
    }

    public void getFilesWithSpecifiedExtension(ArrayList<File> files, ArrayList<String> filesNames, ArrayList<String> extensions) {
        for (File file : files) {
            if (checkExtension(file, extensions)) {
                filesNames.add(file.getName().toString());
            }
        }
    }

    public void getContentFromPath(String path, ArrayList<String> filesNames) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        boolean isDirectoryOrFileWithSpecifiedExtension;

        filesNames.clear();
        filesNames.add("..");
        for (int i = 0; i < files.length; i++) {
            isDirectoryOrFileWithSpecifiedExtension = files[i].isDirectory() || checkExtension(files[i], mExtensions);
            if (isDirectoryOrFileWithSpecifiedExtension) {
                filesNames.add(files[i].getName().toString());
            }
        }
    }

    public boolean checkExtension(File file, ArrayList<String> extensions) {
        String tempExtension = file.getName().toString().substring(file.getName().toString().lastIndexOf(".") + 1, file.getName().toString().length());
        for (String extension : extensions) {
            if (tempExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    @OnItemClick(R.id.explorer_list)
    public void onExplorerListViewItemClick(ListView parent, View view, int position, long id) {

        mExplorerAdapter.notifyDataSetChanged();

        String clickedFileName = (String) mExplorerAdapter.getItem(position);
        String tempPath = createPathOfClickedFile(clickedFileName);
        tryGoToPath(tempPath);
        mExplorerListView.setSelectionAfterHeaderView();
    }

    private String createPathOfClickedFile(String clickedFileName) {
        String tempPath;

        if (clickedFileName.equals("..")){
            File file = new File(mPath);
            try {
                tempPath = file.getParentFile().getPath();
            }
            catch (Exception e) {
                tempPath = mPath;
            }
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
                getContentFromPath(mPath, mFilesNames);
                mExplorerAdapter.setmPath(mPath);
                mExplorerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d("Wrong directory","No permissions to this directory!");
        }
    }
}