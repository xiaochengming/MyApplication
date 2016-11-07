package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.fragment.EmergencyOrderFragment;
import com.example.administrator.myapplication.fragment.EmergencyServiecesFragment;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;

public class PayActivity extends AppCompatActivity {

    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tv_fuwunameleft)
    TextView tvFuwunameleft;
    @InjectView(R.id.tv_fuwuneirong)
    TextView tvFuwuneirong;
    @InjectView(R.id.tv_fuwujine)
    TextView tvFuwujine;
    @InjectView(R.id.layout_dingdanxinxi)
    RelativeLayout layoutDingdanxinxi;
    @InjectView(R.id.tv_zhifu)
    TextView tvZhifu;
    @InjectView(R.id.tv_zhifujinge)
    TextView tvZhifujinge;
    @InjectView(R.id.layout_jine)
    LinearLayout layoutJine;
    @InjectView(R.id.gopay_alipay)
    ImageView gopayAlipay;
    @InjectView(R.id.gopay_select_alipay)
    ImageView gopaySelectAlipay;
    @InjectView(R.id.gopay_rl_alipay)
    RelativeLayout gopayRlAlipay;
    @InjectView(R.id.gopay_weixinpay)
    ImageView gopayWeixinpay;
    @InjectView(R.id.gopay_select_weixin)
    ImageView gopaySelectWeixin;
    @InjectView(R.id.gopay_rl_weixinpay)
    RelativeLayout gopayRlWeixinpay;
    @InjectView(R.id.layout_zhifufangshi)
    LinearLayout layoutZhifufangshi;
    @InjectView(R.id.gopay_pay)
    Button gopayPay;
    private final int REQUEST_EXTERNAL = 2;

    boolean flag = true;
    String orderId;
    @InjectView(R.id.tb_title)
    Toolbar tbTitle;
    Order order;
    @InjectView(R.id.tv_wufujE)
    TextView tvWufujE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
        BP.init(this, "7a1eec4546fa4ab022fefc310d801643");
        initToobar(tbTitle);
        initData();
        initOrderData();
        setSupportActionBar(tbTitle);
        tvTbTitle.setText("订单支付");
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PayActivity", "onClick: 退出");
                finish();
            }
        });

    }

    private void initToobar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.backs);
        toolbar.setTitle("");

    }

    //获取传过来的订单信息
    private void initData() {
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
    }

    private void initOrderData() {
        //初始化订单信息
        tvFuwuneirong.setText(order.getCategory().getName());
        tvWufujE.setText(" ￥：" + order.getAllprice() + "");
        tvZhifujinge.setText(" ￥：" + order.getAllprice() + "");
    }

    @OnClick({R.id.gopay_rl_alipay, R.id.gopay_rl_weixinpay, R.id.gopay_pay, R.id.layout_dingdanxinxi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gopay_rl_alipay:
                gopaySelectAlipay.setImageResource(R.drawable.succeed);
                gopaySelectWeixin.setImageResource(R.drawable.select_all_gray);
                flag = true;
                break;
            case R.id.gopay_rl_weixinpay:
                gopaySelectWeixin.setImageResource(R.drawable.succeed);
                gopaySelectAlipay.setImageResource(R.drawable.select_all_gray);
                flag = false;
                break;
            case R.id.gopay_pay:
                Log.i("PayActivity", "onClick: 支付");
                //true 为支付宝  false 为微信
                //accessFile(flag);
                zhifu(flag);
                break;
            case R.id.layout_dingdanxinxi:
                //进入订单详情
                if (order.getArriveTime() == null) {
                    Intent intent = new Intent(this, PayItemActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(this, PayEmergencyItemActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                }

                break;
        }
    }

    public void accessFile(boolean flag) {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, permission);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL);
        } else {
            //读写SD卡....
            zhifu(flag);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //读写SD卡...
                zhifu(flag);
            } else {
                Toast.makeText(this, "禁止了读写SD卡的权限...", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void zhifu(boolean flag) {
        BP.pay(order.getCategory().getName(), "订单编号：" + order.getOrderId(), 0.02, flag, new PListener() {
            @Override
            public void orderId(String s) {
                orderId = s;
            }

            @Override
            public void succeed() {
                Log.i("PayActivity", "succeed: 支付成功");
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                Log.d("PayActivity", "succeed: " + order.getArriveTime());
                if (order.getArriveTime() == null) {
                    changeOrderState(order.getOrderId());
                } else {
                    changeOrderStateByEmergency(order);

                }

                //queren();
            }

            @Override
            public void fail(int i, String s) {
                String str = "错误";
                if (i == 10777) {
                    str = "网络繁忙，请稍后";
                }
                if (i == 6001) {
                    str = "操作中断";
                }
                if (i == -2) {
                    str = "操作中断";
                }
                Log.i("PayActivity", "fail: " + s);
                Toast.makeText(PayActivity.this, str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unknow() {
                Log.i("PayActivity", "unknow: 网络繁忙");
                Toast.makeText(PayActivity.this, "网络繁忙", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void queren() {
        BP.query(orderId, new QListener() {
            @Override
            public void succeed(String s) {
                if ("SUCCESS".equals(s)) {
                    Log.i("PayActivity", "succeed: 确认支付");
                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    changeOrderState(order.getOrderId());

                } else if ("NOTPAY".equals(s)) {
                    Log.i("PayActivity", "succeed: 支付失败");
                    Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void fail(int i, String s) {
                Log.i("PayActivity", "fail: " + s);
                Toast.makeText(PayActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeOrderState(int orderId) {
        RequestParams params = new RequestParams(StringUtil.ip + "/OrderStateChangeServlet");
        params.addQueryStringParameter("orderId", orderId + "");
        params.addQueryStringParameter("newState", 2 + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
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

    //未调试
    private void changeOrderStateByEmergency(final Order order) {
        RequestParams params = new RequestParams(StringUtil.ip + "/Yan_EmergencyOrderPay");
        Timestamp newTime = new Timestamp(System.currentTimeMillis());
        long endTime = order.getArriveTime().getTime() + newTime.getTime();
        Timestamp en = new Timestamp(endTime);

        order.setEndtime(en);// 获取下单时间
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String orderJson = gson.toJson(order);
        params.addQueryStringParameter("orderJson", orderJson);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("orderId", order.getOrderId() + "");
                editor.commit();
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);

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

}
