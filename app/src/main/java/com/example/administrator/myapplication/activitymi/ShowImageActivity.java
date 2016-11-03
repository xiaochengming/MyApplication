package com.example.administrator.myapplication.activitymi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.util.StringUtil;
import com.mob.tools.gui.ViewPagerAdapter;

import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowImageActivity extends AppCompatActivity {

    @InjectView(R.id.vp_showImage)
    ViewPager vpShowImage;

    List<String> imageList;
    int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.inject(this);
        initData();
        initEven();
        initShezhi();
    }

    public void initShezhi() {
        vpShowImage.setCurrentItem(postion);
    }

    public void initData() {
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("image");
        postion = intent.getIntExtra("postion", 0);

    }

    public void initEven() {
        vpShowImage.setAdapter(new MyAdapter());
    }

    class MyAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(container.getChildAt(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(ShowImageActivity.this);
            container.addView(imageView);
            x.image().bind(imageView, StringUtil.ip + imageList.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("MyAdapter", "onClick: 退出");
                    finish();
                }
            });
            return imageView;
        }
    }
  /*  new PagerAdapter() {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//                ImageView imageView = new ImageView(ShowImageActivity.this);
//                container.addView(imageView);
//                x.image().bind(imageView, StringUtil.ip + imageList.get(position));

            //    return imageView;
            TextView textView=new TextView(ShowImageActivity.this);
            container.addView(textView);
            textView.setText("页码"+position);
            return textView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    })*/
}
