package com.example.administrator.myapplication.activitymi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShowImageActivity extends AppCompatActivity {


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
        x.image().bind(ivXianshi, StringUtil.ip + imageList.get(postion));
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

//    class MyAdapter extends PagerAdapter {
//
//
//        @Override
//        public int getCount() {
//            return imageList.size();
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
////            super.destroyItem(container, position, object);
//            container.removeView(container.getChildAt(position));
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ViewGroup viewGroup= (ViewGroup) imageViewList.get(position).getParent();
//            if (viewGroup!=null){
//                viewGroup.removeView(imageViewList.get(position));
//            }
//            container.addView(imageViewList.get(position));
//            x.image().bind(imageViewList.get(position), StringUtil.ip + imageList.get(position));
//            imageViewList.get(position).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i("MyAdapter", "onClick: 退出");
//                    finish();
//                }
//            });
//            return imageViewList.get(position);
//        }
//    }
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
