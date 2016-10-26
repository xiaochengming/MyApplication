package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 首次添加地址
 */
public class FirstAddressActivity extends AppCompatActivity {

    @InjectView(R.id.first_address_toolbar)
    Toolbar firstAddressToolbar;
    @InjectView(R.id.tv_first_address)
    TextView tvFirstAddress;
    @InjectView(R.id.but_first_address)
    Button butFirstAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_address);
        ButterKnife.inject(this);
        //设置导航图标
        firstAddressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        firstAddressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(firstAddressToolbar);
        //设置toolBar的导航图标点击事件
        firstAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到首页
                finish();
            }
        });
    }

    @OnClick(R.id.but_first_address)
    public void onClick() {
        //进入到地址添加页面
        Intent intent=new Intent(FirstAddressActivity.this,AddAddressActivity.class);
        startActivity(intent);

    }
}
