package com.zhotkit.justin.zhotkit;

import android.app.Fragment;
import android.widget.ListView;

import com.zhotkit.justin.zhotkit.adapter.ListRowAdapter;

import java.util.ArrayList;

public class BaseFragment extends Fragment {
    protected ListView listView;
    protected ListRowAdapter adapter;
    protected ArrayList<String> mTitles = new ArrayList<String>();
    protected ArrayList<String> mIDs = new ArrayList<String>();
    protected ArrayList<String> mUrls = new ArrayList<String>();

    // derived class protype function
    public void loadJsonContent() {

    }

    protected void resetAdapter() {
        mIDs.clear();
        mTitles.clear();
        mUrls.clear();
    }

}