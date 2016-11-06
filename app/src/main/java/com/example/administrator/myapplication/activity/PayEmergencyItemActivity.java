package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PayEmergencyItemActivity extends AppCompatActivity implements View.OnClickListener {


    Order order;


    public static final int UNPAY = 1;//待付款
    public static final int UNSERVICE = 2;//待服务
    public static final int UNREMARK = 3;//待评价
    public static final int COMPLETE = 4;//订单完成
    public static final int COMPLAINT = 5;//申请售后
    public static final int CLOSE = 6;//交易关闭
    public static final int REFUND = 7;//退款
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
    @InjectView(R.id.id_prod_list_tv)
    TextView idProdListTv;
    @InjectView(R.id.id_prod_list_iv_right)
    ImageView idProdListIvRight;
    @InjectView(R.id.title_bar_rl_cartview)
    RelativeLayout titleBarRlCartview;
    @InjectView(R.id.line1)
    LinearLayout line1;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.user_address)
    TextView userAddress;
    @InjectView(R.id.img_housekeeper_photo)
    ImageView imgHousekeeperPhoto;
    @InjectView(R.id.ed_category_name)
    TextView edCategoryName;
    @InjectView(R.id.order_profile)
    TextView orderProfile;
    @InjectView(R.id.rela_jianjie)
    RelativeLayout relaJianjie;
    @InjectView(R.id.rela)
    RelativeLayout rela;
    @InjectView(R.id.order_time)
    TextView orderTime;
    @InjectView(R.id.order_price)
    TextView orderPrice;
    @InjectView(R.id.order_allprice)
    TextView orderAllprice;
    @InjectView(R.id.scr)
    ScrollView scr;
    @InjectView(R.id.img_emergency)
    ImageView imgEmergency;
    @InjectView(R.id.bg_line_bottom)
    View bgLineBottom;
    @InjectView(R.id.order_arrive_time)
    TextView orderArriveTime;
    @InjectView(R.id.order_cancel)
    Button orderCancel;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_emergency_pay_item);
        ButterKnife.inject(this);
        idProdListIvLeft.setOnClickListener(this);
        orderCancel.setOnClickListener(this);
        getData();
    }

    //获得数据
    public void getData() {
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
        initView();
    }

    //初始化控件
    public void initView() {
        userName.setText(order.getUser().getName());
        userAddress.setText(order.getAddress().getAddress());
        if (order.getCategory() != null) {

            x.image().bind(imgHousekeeperPhoto, StringUtil.ip + order.getCategory().getIcon());
        }
        orderProfile.setText(order.getCategory().getProfile());
        String time = String.valueOf(order.getTime());
        orderTime.setText(time.substring(0, time.length() - 4));
        orderPrice.setText("￥" + String.valueOf(order.getPrice()));
        orderAllprice.setText("￥" + String.valueOf(order.getAllprice()));
        //到达时间未初始化
        orderArriveTime.setText("20分钟");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_prod_list_iv_left:
                //后退
                finish();
                break;
            case R.id.order_cancel:
                cancelOrder();
                break;

        }
    }

    //取消订单
    public void cancelOrder() {
        //改变订单状态--->state 6(交易关闭)
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        String url = StringUtil.ip + "/UpdateOrderServlet";
        RequestParams requestParams = new RequestParams(url);
        //传送用户.订单id和状态
        MyApplication myApplication = (MyApplication) getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("orderId", order.getOrderId() + "");
        requestParams.addQueryStringParameter("orderState", 6 + "");
        Callback.Cancelable cancelable = x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {

                            Toast.makeText(PayEmergencyItemActivity.this, "操作成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PayEmergencyItemActivity.this, MainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(PayEmergencyItemActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {

                    }

                    @Override
                    public boolean onCache(String result) {
                        return false;
                    }
                }

        );


    }
}
