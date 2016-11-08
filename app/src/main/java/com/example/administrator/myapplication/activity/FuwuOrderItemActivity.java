package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.entity.Price;
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
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FuwuOrderItemActivity extends AppCompatActivity {
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
    @InjectView(R.id.button_right)
    Button buttonRight;
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
    @InjectView(R.id.text1)
    TextView text1;
    @InjectView(R.id.order_number)
    TextView orderNumber;
    @InjectView(R.id.order_allprice)
    TextView orderAllprice;
    @InjectView(R.id.scr)
    ScrollView scr;
    @InjectView(R.id.bg_line_bottom)
    View bgLineBottom;
    @InjectView(R.id.order_left)
    Button orderLeft;
    @InjectView(R.id.order_right)
    Button orderRight;
    @InjectView(R.id.order_bottom)
    Button orderBottom;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;
    @InjectView(R.id.rela_order_number)
    RelativeLayout relaOrderNumber;
    //删除弹出框
    private AlertDialog.Builder builder;
    Order order;
    ListView listView;
    List<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_fuwu_allorder_item);
        ButterKnife.inject(this);
        getData();
        //控件初始化
        initView();
    }

    //获取订单信息
    public void getData() {
        Intent intent = getIntent();
        String orderJson = intent.getStringExtra("order");
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        order = gson.fromJson(orderJson, Order.class);
    }

    public void initView() {
        userName.setText(order.getUser().getName());
        userAddress.setText(order.getAddress().getAddress());
        //服务类型的图片
        if (order.getCategory() != null) {
            x.image().bind(imgHousekeeperPhoto, StringUtil.ip + order.getCategory().getIcon());
        }
        edCategoryName.setText(order.getCategory().getName());
        //下单时间
        String time = String.valueOf(order.getTime());
        orderTime.setText(time.substring(0, 19));
        //开始时间
        String begdate = String.valueOf(order.getBegdate());
        orderBegdate.setText(begdate.substring(0, 19));
        //工作时间
        switch (order.getCategory().getType()) {
            case "保姆月嫂":
                orderWorker.setText(String.valueOf(order.getWorkerTime()) + "月");
                break;
            case "搬家服务":
                orderWorker.setText(String.valueOf(order.getWorkerTime()) + "天");
                break;
            case "居家换新":
                orderWorker.setText(String.valueOf(order.getWorkerTime()) + "天");
                break;
            default:
                orderWorker.setText(String.valueOf(order.getWorkerTime()) + "小时");
        }
        Iterator<Price> iterator = order.getCategory().getPrices().iterator();
        while (iterator.hasNext()) {
            Price price = iterator.next();
            if (price.getPrice() == order.getPrice()) {
                unit = price.getUnit();
            }
        }
        unit.substring(1, unit.length());
        //单价
        orderPrice.setText("￥" + order.getPrice() + unit);
        //数量

        //数量处理
        if (unit.equals("/小时") || unit.equals("/人一天") || unit.equals("/月")) {

            relaOrderNumber.setVisibility(View.GONE);
        } else {
            relaOrderNumber.setVisibility(View.VISIBLE);
            orderNumber.setText(String.valueOf(order.getNumber() + unit.substring(1, unit.length())));
        }

        //总价
        orderAllprice.setText("￥" + order.getAllprice());
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

                orderLeft.setVisibility(View.VISIBLE);
                orderRight.setVisibility(View.VISIBLE);
                orderLeft.setText("取消订单");
                orderRight.setText("确认订单");
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
            case REFUND:
                orderLeft.setVisibility(View.VISIBLE);
                orderLeft.setVisibility(View.GONE);
                orderRight.setVisibility(View.GONE);
                break;
        }

    }


    @OnClick({R.id.order_right, R.id.order_left, R.id.order_bottom, R.id.button_right, R.id.id_prod_list_iv_left})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_right:
                //开启电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1008611"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                FuwuOrderItemActivity.this.startActivity(intent);
                break;
            case R.id.order_left:
                switch (order.getState()) {
                    case UNPAY:
                        dialog(order, CLOSE);
                        break;
                    case UNSERVICE:
                        //退款
                        if (ontimeListener() == false) {
                            dialog(order, REFUND);
                        } else {
                            Toast.makeText(this, "已服务请联系客服", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case UNREMARK:
                        //待评价
                        dialog(order, CLOSE);
                        break;
                    case COMPLETE:
                        //订单完成
                        dialog(order, CLOSE);
                        break;

                }
                break;
            case R.id.order_right:
                switch (order.getState()) {
                    case UNPAY:
                        //立即支付跳转支付方页面
                        Intent intent1 = new Intent(this, PayActivity.class);
                        intent1.putExtra("order", order);
                        startActivity(intent1);
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
                dialog(order, CLOSE);
                break;
            case R.id.id_prod_list_iv_left:
                //后退
                finish();
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
            case REFUND:
                builder.setMessage("是否取消订单");
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
                            case REFUND:
                                changeState(order, REFUND);
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
                            Toast.makeText(FuwuOrderItemActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            order.setState(CLOSE);
                            back();
                            finish();//销毁当前activity (返回键)
                            break;
                        case UNREMARK:
                            Toast.makeText(FuwuOrderItemActivity.this, "确认成功", Toast.LENGTH_SHORT).show();
                            order.setState(UNREMARK);
                            back();
                            break;
                        case REFUND:
                            Toast.makeText(FuwuOrderItemActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                            order.setState(REFUND);
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
