package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.administrator.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 地图定位
 */
public class MapLocationActivity extends AppCompatActivity {

    @InjectView(R.id.map_toolbar)
    Toolbar mapToolbar;
    @InjectView(R.id.et_map)
    EditText etMap;
    @InjectView(R.id.but_map)
    Button butMap;
    @InjectView(R.id.frame_layout_map)
    FrameLayout frameLayoutMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        ButterKnife.inject(this);
        //设置导航图标
        mapToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        mapToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(mapToolbar);
        //设置toolBar的导航图标点击事件
        mapToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到添加服务地址
               finish();

            }
        });
    }
}
