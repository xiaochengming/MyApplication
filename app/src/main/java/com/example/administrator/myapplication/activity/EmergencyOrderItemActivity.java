package com.example.administrator.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EmergencyOrderItemActivity extends AppCompatActivity {
    public static final Integer BACK = 2;
    public static final Integer TOEVALUATE = 1;
    public static final int UNPAY = 1;//待付款
    public static final int UNSERVICE = 2;//待服务
    public static final int UNREMARK = 3;//待评价
    public static final int COMPLETE = 4;//订单完成
    public static final int COMPLAINT = 5;//投诉
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
    @InjectView(R.id.order_arrive_time)
    TextView orderArriveTime;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.scr)
    ScrollView scr;
    @InjectView(R.id.img_emergency)
    ImageView imgEmergency;
    @InjectView(R.id.bg_line_bottom)
    View bgLineBottom;
    @InjectView(R.id.order_left)
    Button orderLeft;
    @InjectView(R.id.order_right)
    Button orderRight;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;
    @InjectView(R.id.te_1)
    TextView textView;
    @InjectView(R.id.order_bottom)
    Button orderBottom;


    //删除弹出框
    private AlertDialog.Builder builder;
    Order order;
    ListView listView;
    List<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_emergency_item);
        ButterKnife.inject(this);
        getData();
        initView();
        //退出
        idProdListIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //获取订单信息
    public void getData() {
        Intent intent = getIntent();
        String orderJson = intent.getStringExtra("order");
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        order = gson.fromJson(orderJson, Order.class);
    }

    //初始化控件
    public void initView() {
        userName.setText(order.getUser().getName());
        userAddress.setText(order.getAddress().getAddress());
        if (order.getCategory() != null) {
            x.image().bind(imgHousekeeperPhoto, StringUtil.ip + order.getCategory().getIcon());
        }
        orderProfile.setText("￥" + order.getCategory().getProfile());
        String time = String.valueOf(order.getTime());
        orderTime.setText(time.substring(0, 19));
        orderPrice.setText("￥" + String.valueOf(order.getPrice()));
        orderAllprice.setText("￥" + String.valueOf(order.getAllprice()));
        //到达时间
        DateFormat sdf = new SimpleDateFormat("HH小时mm分钟ss秒");
        String time1 = sdf.format(order.getArriveTime());
        orderArriveTime.setText(time1);
        initViewButton();
    }

    //button初始化
    public void initViewButton() {
        switch (order.getState()) {
            case UNPAY:
                orderLeft.setVisibility(View.VISIBLE);
                orderRight.setVisibility(View.VISIBLE);
                orderLeft.setText("删除订单");
                orderRight.setText("立即支付");
                break;
            case UNSERVICE:
                textView.setVisibility(View.VISIBLE);
                orderLeft.setVisibility(View.GONE);
                orderRight.setVisibility(View.GONE);

                break;
            case UNREMARK:
                orderLeft.setVisibility(View.VISIBLE);
                orderRight.setVisibility(View.VISIBLE);
                orderLeft.setText("删除订单");
                orderRight.setText("立即评价");
                break;
            case COMPLETE:
                orderLeft.setVisibility(View.GONE);
                orderRight.setVisibility(View.GONE);
                orderBottom.setVisibility(View.VISIBLE);
                orderBottom.setText("删除订单");
                break;
        }

    }


    @OnClick({R.id.order_left, R.id.order_right, R.id.order_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_left:
                switch (order.getState()) {
                    case UNPAY:
                        dialog(order, CLOSE);
                        break;
                    case UNREMARK:
                        dialog(order, CLOSE);
                        break;

                }
                break;
            case R.id.order_right:
                switch (order.getState()) {
                    case UNPAY:
                        //立即支付跳转支付方页面
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                        break;
                    case UNSERVICE:
                        //在服务完成时候可以确认订单
                        if (ontimeListener()) {
                            dialog(order, UNREMARK);
                        } else {
                            Toast.makeText(this, "未服务", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case UNREMARK:
                        //评价
                        Intent intent2 = new Intent(this, FuwuOrderEvaluateActivity.class);

                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        String or = gson.toJson(order);
                        intent2.putExtra("Order", or);

                        startActivityForResult(intent2, TOEVALUATE);
                        break;

                    case REFUND:
                        //退款查看

                        break;
                }
                break;
            case R.id.order_bottom:
                //删除
                dialog(order, CLOSE);
                break;
        }

    }

    //判断是否可以“确认下单”||是否可以取消订单
    public boolean ontimeListener() {
        int newTime = 0;
        int endtime = 0;
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time1 = sdf.format(System.currentTimeMillis());
        newTime = Integer.parseInt(time1.substring(0, time1.length() - 6));
        String time2 = sdf.format(order.getEndtime());
        //到达时间---未做
        endtime = Integer.parseInt(time2.substring(0, time2.length() - 6));
        if (newTime > endtime) {
            return true;
        } else {
            return false;
        }
    }


    //删除订单弹出框
    public void dialog(final Order order, final int changeState) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        switch (changeState) {
            case CLOSE:
                builder.setMessage("是否删除订单");
                break;
            case UNREMARK:
                builder.setMessage("是否确认订单");
                break;

        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (changeState) {
                            case CLOSE:
                                changeState(order, CLOSE);
                                break;
                            case UNREMARK:
                                changeState(order, UNREMARK);
                                break;

                        }
                    }
                }
        );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }

        );
        builder.create().

                show();

    }

    //更新订单状态，更新界面

    public void changeState(final Order order, final int changeState) {

        RequestParams requestParams = new RequestParams(StringUtil.ip + "/UpdateEmergencyOrder");

        MyApplication myApplication = (MyApplication) getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addBodyParameter("orderId", order.getOrderId() + "");
        requestParams.addBodyParameter("orderState", changeState + "");

        //更新订单，更新界面
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                if (result.equals("success")) {
                    switch (changeState) {
                        case CLOSE:
                            Toast.makeText(EmergencyOrderItemActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            order.setState(CLOSE);
                            back();
                            finish();//销毁当前activity (返回键)
                            break;
                        case UNREMARK:
                            Toast.makeText(EmergencyOrderItemActivity.this, "确认成功", Toast.LENGTH_SHORT).show();
                            order.setState(UNREMARK);
                            back();
                            break;

                    }
                    back();

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
        });


    }

    //返回信息
    public void back() {
        Intent intent = new Intent();
        intent.putExtra("orderId", order.getOrderId() + "");
        intent.putExtra("orderState", order.getState() + "");
        setResult(BACK, intent);
        initViewButton();
    }

    //back完成之后跟新界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && requestCode == TOEVALUATE) {
            if (data != null) {
                int orderId = Integer.parseInt(data.getStringExtra("orderId"));
                order.setState(COMPLETE);
                initViewButton();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("orderId", order.getOrderId() + "");
        intent.putExtra("orderState", order.getState() + "");
        setResult(BACK, intent);
        super.onBackPressed();
    }


}
