package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class PayItemActivity extends AppCompatActivity implements View.OnClickListener {
    Order order;
    boolean temp = false;
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
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
    @InjectView(R.id.rela)
    RelativeLayout rela;
    @InjectView(R.id.order_time)
    TextView orderTime;
    @InjectView(R.id.order_begdate)
    TextView orderBegdate;
    @InjectView(R.id.order_worker)
    TextView orderWorker;
    @InjectView(R.id.order_price)
    TextView orderPrice;
    @InjectView(R.id.order_number)
    TextView orderNumber;
    @InjectView(R.id.order_allprice)
    TextView orderAllprice;

    @InjectView(R.id.order_total_money)
    TextView orderTotalMoney;
    @InjectView(R.id.order_cancel)
    Button orderCancel;
    @InjectView(R.id.button_right)
    Button buttonRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_pay_item);
        ButterKnife.inject(this);
        getData();
        initView();
        idProdListIvLeft.setOnClickListener(this);
        orderCancel.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
    }

    //获取数据
    public void getData() {
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
    }

    //初始化控件
    public void initView() {
        if (order != null) {
            userName.setText(order.getUser().getName());
            userAddress.setText(order.getAddress().getAddress());
            x.image().bind(imgHousekeeperPhoto, StringUtil.ip + order.getCategory().getIcon());
            edCategoryName.setText(order.getCategory().getName());
            String time = String.valueOf(order.getTime());
            orderTime.setText(time.substring(0, time.length() - 4));
            String begdate = String.valueOf(order.getBegdate());
            orderBegdate.setText(begdate.substring(0, begdate.length() - 2));
            orderWorker.setText(String.valueOf(order.getWorkerTime()));
            orderPrice.setText(String.valueOf(order.getPrice()));
            orderNumber.setText(String.valueOf(order.getNumber()));
            orderAllprice.setText(String.valueOf(order.getAllprice()));
            orderTotalMoney.setText(String.valueOf(order.getAllprice()));
        }
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
            case R.id.button_right:
                //客服
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

                            Toast.makeText(PayItemActivity.this, "操作成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PayItemActivity.this, MainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(PayItemActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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