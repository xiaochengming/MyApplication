package com.example.administrator.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2016/9/19.
 */
//应急服务
public class FuWuFragment extends Fragment implements View.OnClickListener {


    private View view;
    private TextView emergency_services, emergency_order;
    List<Order> products = new ArrayList<Order>();//存放订单信息
    Fragment[] fragments;
    TextView[] texts;
    int oldIndex;//用户看到的fragment
    int newIndex;//用户即将看到的fragment

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_list_fuwufragment, null);
        //点击事件
        emergency_services = (TextView) view.findViewById(R.id.emergency_services);
        emergency_order = (TextView) view.findViewById(R.id.emergency_order);
        emergency_services.setOnClickListener(this);
        emergency_order.setOnClickListener(this);
        //初始化控件
        initView();
        //开始显示的fragment

        return view;
    }

    public void initFragment() {
        //实现fragments
        EmergencyServiecesFragment emergencyServiecesFragment = new EmergencyServiecesFragment();
        EmergencyOrderFragment emergencyOrderFragment = new EmergencyOrderFragment();

        fragments = new Fragment[]{emergencyServiecesFragment, emergencyOrderFragment};
        newIndex = 1;
        oldIndex = 0;
    }

    public void initView() {
        // 初始化控件

        texts = new TextView[2];
        texts[0] = (TextView) view.findViewById(R.id.emergency_services);//服务
        texts[1] = (TextView) view.findViewById(R.id.emergency_order);//订单
        //按钮点击

        initFragment();
        //界面初始显示第一个fragment;添加第一个fragment
        getActivity().getSupportFragmentManager().beginTransaction().add
                (R.id.fragment_container, fragments[0], "emergencyServiecesFragment").commit();
        //初始时，按钮1选中
        texts[0].setSelected(true);
    }


    //按钮的点击事件:选中不同的按钮，不同的fragment显示
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //应急服务点击事件
            case R.id.emergency_services:
                newIndex = 0;//选中第一项
                // Log.d("111", "选中第一项");
                break;
            //应急订单点击事件
            case R.id.emergency_order:
                newIndex = 1;//选中第二项
                //  Log.d("111", "选中第二项");
                break;
        }
        switchFragment();

    }

    public void switchFragment() {
        FragmentTransaction transaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {

            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.hide(fragments[oldIndex]);//隐藏当前显示项


            //如果选中项没有加过，则添加
            if (!fragments[newIndex].isAdded()) {
                //添加fragment
                transaction.add(R.id.fragment_container, fragments[newIndex], "emergencyOrderFragment");


            }
            //显示当前选择项
            transaction.show(fragments[newIndex]).commit();
        }
        //之前选中的项，取消选中
        texts[oldIndex].setSelected(false);
        //当前选择项，按钮被选中
        texts[newIndex].setSelected(true);


        //当前选择项变为选中项
        oldIndex = newIndex;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("emergencyOrderFragment");
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        //super.onSaveInstanceState(outState);
    }
}
