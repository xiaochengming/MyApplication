package com.example.administrator.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.DataCleanManager;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @InjectView(R.id.set_toolbar)
    Toolbar setToolbar;
    @InjectView(R.id.tv_exit_login)
    TextView tvExitLogin;
    MyApplication myApplication;
    @InjectView(R.id.relative_layout_set1)
    RelativeLayout relativeLayoutSet1;
    @InjectView(R.id.relative_layout_set2)
    RelativeLayout relativeLayoutSet2;
    @InjectView(R.id.relative_layout_set3)
    RelativeLayout relativeLayoutSet3;
    @InjectView(R.id.relative_layout_set4)
    RelativeLayout relativeLayoutSet4;
    @InjectView(R.id.relative_layout_set5)
    RelativeLayout relativeLayoutSet5;
    String strHuanCun;
    @InjectView(R.id.tv_huan_cun)
    TextView tvHuanCun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        try {
            strHuanCun = DataCleanManager.getTotalCacheSize(getApplicationContext());
            Log.i("SettingActivity", "onCreate strHuanCun :"+strHuanCun);
            if (strHuanCun != null&&!strHuanCun.equals("0K")) {
                tvHuanCun.setText(strHuanCun);
            }else {
                tvHuanCun.setText("暂无缓存");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        myApplication = (MyApplication) getApplication();
        //设置导航图标
        setToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        setToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(setToolbar);
        Log.i("TAG", "onCreate  myApplication.getUser()=" + myApplication.getUser());
        //设置toolBar的导航图标点击事件
        setToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到主界面
                finish();
            }
        });
        //如果传回的Id存在，说明用户已经登录，显示“退出登录”
        if (myApplication.isFlag() == true) {
            //显示退出登录
            tvExitLogin.setVisibility(View.VISIBLE);
            tvExitLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("SettingActivity", "onClick  ");
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    myApplication.setFlag(false);
                    myApplication.setUser(new User(0, null, 0, null, 2, null, null, getDate("0000-00-00"), null));
                    startActivity(intent);
                    System.exit(0);
                    tvExitLogin.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public Date getDate(String dateSte) {
        Date date = new Date(0);
        return date;
    }

    @OnClick({R.id.relative_layout_set1, R.id.relative_layout_set2, R.id.relative_layout_set3, R.id.relative_layout_set4, R.id.relative_layout_set5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_set1:
                //用户协议
                break;
            case R.id.relative_layout_set2:
                //清除缓存

                DataCleanManager.clearAllCache(getApplicationContext());
                tvHuanCun.setText("暂无缓存");
                Log.i("SettingActivity", "onClick  strHuanCun2:"+strHuanCun);
                break;
            case R.id.relative_layout_set3:
                //联系客服
                break;
            case R.id.relative_layout_set4:
                //检查新版本
                break;
            case R.id.relative_layout_set5:
                //关于我们
                break;
        }
    }
}
