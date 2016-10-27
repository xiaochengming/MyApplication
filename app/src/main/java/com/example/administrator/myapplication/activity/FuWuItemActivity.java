package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by king on 2016/10/19.
 */
public class FuWuItemActivity extends AppCompatActivity implements View.OnClickListener {

    int[] resId = {R.mipmap.add_photo, R.mipmap.address, R.mipmap.aa};
    @InjectView(R.id.prod_info_cart)
    Button prodInfoCart;
    @InjectView(R.id.prod_info_nowbuy)
    Button prodInfoNowbuy;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;
    @InjectView(R.id.flipper_photo)
    ViewFlipper flipperPhoto;
    @InjectView(R.id.prod_info_tv_des)
    TextView prodInfoTvDes;
    @InjectView(R.id.prod_info_tv_des_name)
    TextView prodInfoTvDesName;
    @InjectView(R.id.prod_info_tv_price)
    TextView prodInfoTvPrice;
    @InjectView(R.id.prod_info_tv_pnum)
    TextView prodInfoTvPnum;
    @InjectView(R.id.prod_info_tv_prod_record)
    TextView prodInfoTvProdRecord;
    @InjectView(R.id.lv_user_remark)
    ListView lvUserRemark;
    @InjectView(R.id.prod_info_tv_prod_comment)
    TextView prodInfoTvProdComment;
    @InjectView(R.id.prod_info_linearly)
    LinearLayout prodInfoLinearly;

    private float startX;
    private float startY;

    ImageView imageView;
    Handler handler = new Handler();
    Category category;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fu_wu_item);
        ButterKnife.inject(this);
        prodInfoCart.setOnClickListener(this);
        prodInfoNowbuy.setOnClickListener(this);
        getData();
        imageRotation();
        initView();//  imageRotation();

    }

    //动态获取图片
    public ImageView getImageView(int resId) {
        imageView = new ImageView(this);
        imageView.setBackgroundResource(resId);
        return imageView;
    }

    //图片轮转
    public void imageRotation() {

        for (int i = 0; i < resId.length; i++) {
            flipperPhoto.addView(getImageView(resId[i]));
        }
    }

    //获取categories信息
    public void getData() {
        Intent intent = getIntent();
        String categoriesJson = intent.getStringExtra("categoriesJson");
        String userJson = intent.getStringExtra("userJson");

        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        category = gson.fromJson(categoriesJson, Category.class);
        user = gson.fromJson(userJson, User.class);

    }

    //bug当不再图片上滑动也可以滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //手指落下
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();

                break;
            //手指滑动
            case MotionEvent.ACTION_MOVE:

                break;
            //手指离开
            case MotionEvent.ACTION_UP:
                //向右滑动
                if (event.getX() - startX > 200) {
                    flipperPhoto.setInAnimation(this, R.anim.right_in);
                    flipperPhoto.setOutAnimation(this, R.anim.right_out);
                    flipperPhoto.showPrevious();//显示前一页
                }
                //向左滑动
                if (event.getX() - startX < 200) {
                    flipperPhoto.setInAnimation(this, R.anim.left_in);
                    flipperPhoto.setOutAnimation(this, R.anim.left_out);
                    flipperPhoto.showNext();//显示后一页

                }

                break;
        }

        return super.

                onTouchEvent(event);

    }

    public void initView() {
        prodInfoTvDesName.setText(category.getName());
        prodInfoTvPrice.setText(category.getPrices().get(0).getPrice() + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prod_info_cart:
                //转到客服
                break;
            case R.id.prod_info_nowbuy:
                //转到下单页面
                if (user.getUserId() != 0) {
                    Intent intent = new Intent(this, EmergencyPlaceAnOrderActivity.class);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String categoryJson = gson.toJson(category);
                    String userJson = gson.toJson(user);
                    intent.putExtra("categoryJson", categoryJson);
                    intent.putExtra("userJson", userJson);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "未登入", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}
