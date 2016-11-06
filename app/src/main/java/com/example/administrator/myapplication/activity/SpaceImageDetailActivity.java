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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpaceImageDetailActivity extends AppCompatActivity {
    @InjectView(R.id.vp_space_image)
    ViewPager vpSpaceImage;
    private List<String> mDatas;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lh_spaceimage_detail);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        mDatas = intent.getStringArrayListExtra("images");
        mPosition = intent.getIntExtra("postion", 0);
        //给你viewpage设置数据源
        vpSpaceImage.setAdapter(new myPageAdapter());
        //显示图片
        vpSpaceImage.setCurrentItem(mPosition);

    }
    class myPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(container.getChildAt(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(SpaceImageDetailActivity.this);
            container.addView(imageView);
            x.image().bind(imageView, mDatas.get(position));
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
}
