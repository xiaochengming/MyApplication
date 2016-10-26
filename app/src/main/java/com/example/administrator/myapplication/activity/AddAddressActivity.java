package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加地址服务
 */
public class AddAddressActivity extends AppCompatActivity {

    @InjectView(R.id.add_address_toolbar)
    Toolbar addAddressToolbar;
    @InjectView(R.id.et_address_name)
    EditText etAddressName;
    @InjectView(R.id.et_address_phone)
    EditText etAddressPhone;
    @InjectView(R.id.iv_set_address)
    ImageView ivSetAddress;
    @InjectView(R.id.but_save_address)
    Button butSaveAddress;
    @InjectView(R.id.frame_layout_address)
    FrameLayout frameLayoutAddress;
    @InjectView(R.id.tv_display_address)
    TextView tvDisplayAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.inject(this);
        //设置导航图标
        addAddressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        addAddressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(addAddressToolbar);
        //设置toolBar的导航图标点击事件
        addAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //返回到地址（非首次）

                //首次

                finish();
            }
        });
    }

    @OnClick({R.id.iv_set_address, R.id.but_save_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set_address:
                //点击进入地图界面
                Intent intent=new Intent(AddAddressActivity.this,MapLocationActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.but_save_address:
                //点击保存地址
                break;
        }
    }
}
