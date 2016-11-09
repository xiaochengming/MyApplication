package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SpaceImageDetailActivity extends AppCompatActivity {
    List<String> imageList;
    int postion;
    List<ImageView> imageViewList = new ArrayList<>();
    @InjectView(R.id.iv_xianshi)
    ImageView ivXianshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.inject(this);
        initData();
        initEven();

//        initShezhi();
    }

//    public void initShezhi() {
//        vpShowImage.setCurrentItem(postion);
//    }

    public void initData() {
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("image");
        postion = intent.getIntExtra("postion", 0);
        x.image().bind(ivXianshi,  imageList.get(postion));
//        for(int i=0;i<imageList.size();i++){
//            ImageView imageView = new ImageView(ShowImageActivity.this);
//            imageViewList.add(imageView);
//        }

    }

    public void initEven() {
//        vpShowImage.setAdapter(new MyAdapter());
    }

    @OnClick(R.id.iv_xianshi)
    public void onClick() {
        finish();
    }

}
