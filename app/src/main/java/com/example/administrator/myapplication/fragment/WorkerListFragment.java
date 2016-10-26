package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2016/9/20.
 *
 */
public class WorkerListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView listView= (ListView) inflater.inflate(R.layout.worker_list,null);


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
