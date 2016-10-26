package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 非首次添加地址
 */
public class AddressActivity extends AppCompatActivity {

    @InjectView(R.id.address_toolbar)
    Toolbar addressToolbar;
    @InjectView(R.id.but_address)
    Button butAddress;
    @InjectView(R.id.lv_address)
    ListView lvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.inject(this);
        //设置导航图标
        addressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        addressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(addressToolbar);
        //设置toolBar的导航图标点击事件
        addressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到首页;
                finish();
            }
        });
    }

    @OnClick(R.id.but_address)
    public void onClick() {
        //进入到地址添加页面
        Intent intent=new Intent(AddressActivity.this,AddAddressActivity.class);
        startActivity(intent);
;
    }
}
