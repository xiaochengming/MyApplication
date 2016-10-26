package com.example.administrator.myapplication.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

/**
 * Created by luhai on 2016/10/6.
 * 地址详情
 */
public class AddressDetailsFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //解析布局文件
        View v=inflater.inflate(R.layout.fragment_details_address,null);
        return v;
    }
}
