package com.example.administrator.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.fragment.AllOrderFragment;
import com.example.administrator.myapplication.fragment.ToBeEvaluatedFragment;
import com.example.administrator.myapplication.fragment.ToBePaidFragment;
import com.example.administrator.myapplication.fragment.ToBeServedFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    View view = null;
    Fragment[] fragments;
    int oldIndex;//用户看到的fragment
    int newIndex;//用户即将看到的fragment
    TextView[] texts;
    FragmentManager fragmentManager = getSupportFragmentManager();
    AllOrderFragment allOrderFragment;
    ToBePaidFragment toBePaidFragment;
    ToBeEvaluatedFragment toBeEvaluatedFragment;
    ToBeServedFragment toBeServedFragment;
    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        ButterKnife.inject(this);
        initView();
        initEven();

    }
    //初始化toobar
    public void initEven(){

        tbWorkerlist.setNavigationIcon(R.mipmap.back);
        tbWorkerlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {
        // 初始化控件
        TextView tvAllOrder = (TextView) findViewById(R.id.tv_allOrder);
        TextView tvDaifukuang = (TextView) findViewById(R.id.tv_daifukuang);
        TextView tvDaipingjia = (TextView) findViewById(R.id.tv_daipingjia);
        TextView tvDaifuwu = (TextView) findViewById(R.id.tv_daifuwu);
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

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_dingdan, fragments[0], "allOrderFragment").commit();

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
            transaction = getSupportFragmentManager().beginTransaction();

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
