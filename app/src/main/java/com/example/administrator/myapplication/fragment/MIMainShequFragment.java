package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


import com.example.administrator.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/24.
 */
public class MIMainShequFragment extends Fragment {


    MiSheQuFragment sheQuFragment;
    MiMyTieFragment myTieFragment;
    MiHuiFuFragment huiFuFragment;
   public  Fragment[] fragment;
    Button[] btn;
    int oldnumber;
    int nownumber;
    @InjectView(R.id.btn_alltiezi)
    Button btnAlltiezi;
    @InjectView(R.id.btn_wodetiezi)
    Button btnWodetiezi;
    @InjectView(R.id.btn_huifutiezi)
    Button btnHuifutiezi;
    @InjectView(R.id.fra_shequ)
    FrameLayout fraShequ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_shequ, null);
        ButterKnife.inject(this, v);
        sheQuFragment = new MiSheQuFragment();
        myTieFragment = new MiMyTieFragment();
        huiFuFragment = new MiHuiFuFragment();
        fragment = new Fragment[]{sheQuFragment, myTieFragment, huiFuFragment};
        btn = new Button[]{btnAlltiezi, btnWodetiezi, btnHuifutiezi};
        //替换第一个fragment
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fra_shequ, sheQuFragment).commit();
        btnAlltiezi.setSelected(true);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_alltiezi, R.id.btn_wodetiezi, R.id.btn_huifutiezi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alltiezi:
                nownumber = 0;
                break;
            case R.id.btn_wodetiezi:
                nownumber = 1;
                break;
            case R.id.btn_huifutiezi:
                nownumber = 2;
                break;
        }
        switchFragment();
    }

    public void switchFragment() {
        FragmentTransaction transaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (nownumber != oldnumber) {

            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.hide(fragment[oldnumber]);//隐藏当前显示项


            //如果选中项没有加过，则添加
            if (!fragment[nownumber].isAdded()) {
                //添加fragment
                transaction.add(R.id.fra_shequ, fragment[nownumber]);
            }
            //显示当前选择项
            transaction.show(fragment[nownumber]).commit();
        }
        //之前选中的项，取消选中
        btn[oldnumber].setSelected(false);
        //当前选择项，按钮被选中
        btn[nownumber].setSelected(true);


        //当前选择项变为选中项
        oldnumber = nownumber;
    }
}
