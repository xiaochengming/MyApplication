package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by on 2016/9/19.
 */
public class OrderFragment extends Fragment implements View.OnClickListener {
    View view = null;
    Fragment[] fragments;
    int oldIndex;//用户看到的fragment
    int newIndex;//用户即将看到的fragment
    TextView[] texts;
    FragmentManager fragmentManager = getFragmentManager();
    AllOrderFragment allOrderFragment;
    ToBePaidFragment toBePaidFragment;
    ToBeEvaluatedFragment toBeEvaluatedFragment;
    ToBeServedFragment toBeServedFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_home_main, null);
        initView();
        return view;
    }


    public void initView() {
        // 初始化控件
        TextView tvAllOrder = (TextView) view.findViewById(R.id.tv_allOrder);
        TextView tvDaifukuang = (TextView) view.findViewById(R.id.tv_daifukuang);
        TextView tvDaipingjia = (TextView) view.findViewById(R.id.tv_daipingjia);
        TextView tvDaifuwu = (TextView) view.findViewById(R.id.tv_daifuwu);
        tvAllOrder.setOnClickListener(this);
        tvDaifukuang.setOnClickListener(this);
        tvDaipingjia.setOnClickListener(this);
        tvDaifuwu.setOnClickListener(this);
        texts = new TextView[4];
        texts[0] = tvAllOrder;//全部订单
        texts[1] = tvDaifukuang;//待付款
        texts[2] = tvDaipingjia;//待评价
        texts[3] = tvDaifuwu;//待服务

        //按钮点击
        initFragment();
        //界面初始显示第一个fragment;添加第一个fragment

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_dingdan, fragments[0], "allOrderFragment").commit();

        //初始时，按钮1选中
        texts[0].setSelected(true);
    }

    public void initFragment() {
        //实现fragments
        allOrderFragment = new AllOrderFragment();
        toBePaidFragment = new ToBePaidFragment();
        toBeEvaluatedFragment = new ToBeEvaluatedFragment();
        toBeServedFragment = new ToBeServedFragment();
        fragments = new Fragment[]{allOrderFragment, toBePaidFragment, toBeEvaluatedFragment, toBeServedFragment};

        oldIndex = 0;
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
                transaction.add(R.id.fragment_dingdan, fragments[newIndex], fragments[newIndex] + "");
                if (fragmentManager != null) {

                }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_allOrder:
                newIndex = 0;//选中第一项
                break;
            case R.id.tv_daifukuang:
                newIndex = 1;//选中第二项
                break;
            case R.id.tv_daipingjia:
                newIndex = 2;//选中第三项
                break;
            case R.id.tv_daifuwu:
                newIndex = 3;//选中第四项
                break;

        }
        switchFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }
}
