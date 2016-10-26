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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.UrlAddress;
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

public class ItemActivity extends AppCompatActivity {
    public static final Integer BACK = 2;
    public static final Integer TOEVALUATE = 1;
    public static final int UNPAY = 1;//待付款
    public static final int UNSERVICE = 2;//待服务
    public static final int UNREMARK = 3;//待评价
    public static final int COMPLETE = 4;//订单完成
    public static final int COMPLAINT = 5;//投诉
    public static final int CLOSE = 6;//交易关闭
    public static final int REFUND = 7;//退款
    //删除弹出框
    private AlertDialog.Builder builder;
    Order order;
    ListView listView;
    List<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    @InjectView(R.id.image_id_2)
    ImageView imageId2;
    @InjectView(R.id.consignee)
    TextView consignee;
    @InjectView(R.id.order_textview_3)
    TextView orderTextview3;
    @InjectView(R.id.view1)
    View view1;
    @InjectView(R.id.img_housekeeper_photo)
    ImageView imgHousekeeperPhoto;
    @InjectView(R.id.ed_category_name)
    TextView edCategoryName;
    @InjectView(R.id.button_left)
    Button buttonLeft;
    @InjectView(R.id.button_right)
    Button buttonRight;
    @InjectView(R.id.relat_id)
    RelativeLayout relatId;
    @InjectView(R.id.list_order_information)
    ListView listOrderInformation;
    @InjectView(R.id.bu_left)
    Button buLeft;
    @InjectView(R.id.bu_right)
    Button buRight;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_item);
        ButterKnife.inject(this);
        listView = (ListView) findViewById(R.id.list_order_information);
        getData();
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
        //listview的初始化
        String[] orderInformation = {"下单时间", "开始时间", "工作时间", "单价", "数量", "总价", "实付款"};
        String[] orders = {order.getTime().toString(), order.getBegdate().toString(),
                order.getWorker(), "￥" + order.getPrice(), order.getNumber() + "",
                order.getAllprice() + "", order.getAllprice() + ""};
        for (int i = 0; i < orderInformation.length; i++) {
            HashMap<String, String> orderMap = new HashMap<String, String>();
            orderMap.put("orderInformation", orderInformation[i]);
            orderMap.put("orders", orders[i]);
            orderList.add(orderMap);
        }

        listView.setAdapter(new SimpleAdapter(this, orderList, R.layout.emergency_item_list_layout, new String[]{"orderInformation", "orders"},
                new int[]{R.id.tv_left, R.id.tv_right}));
        //其他控件初始化
        if (order.getHousekeeper() != null) {
            x.image().bind(imgHousekeeperPhoto, order.getHousekeeper().getHousePhoto());
        }

        edCategoryName.setText(order.getCategory().getName());
        initViewButton();
    }

    //button初始化
    public void initViewButton() {
        switch (order.getState()) {
            case UNPAY:
                buttonLeft.setVisibility(View.GONE);
                buLeft.setVisibility(View.VISIBLE);
                buLeft.setText("删除订单");
                buRight.setText("立即支付");
                break;
            case UNSERVICE:
                buttonLeft.setVisibility(View.GONE);
                buLeft.setVisibility(View.VISIBLE);
                buRight.setVisibility(View.VISIBLE);
                buLeft.setText("取消订单");
                buRight.setText("确认订单");
                break;
            case UNREMARK:
                buttonLeft.setVisibility(View.VISIBLE);
                buLeft.setVisibility(View.VISIBLE);
                buLeft.setText("删除订单");
                buRight.setText("立即评价");
                break;
            case COMPLETE:
                buttonLeft.setVisibility(View.VISIBLE);
                buRight.setVisibility(View.GONE);
                buLeft.setText("删除订单");
                break;
            case REFUND:
                buttonLeft.setVisibility(View.VISIBLE);
                buLeft.setVisibility(View.GONE);
                buRight.setVisibility(View.GONE);
                break;
        }

    }


    @OnClick({R.id.button_left, R.id.button_right, R.id.bu_left, R.id.bu_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_left:
                //跳转到客服

                break;
            case R.id.button_right:
                //开启电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1008611"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                ItemActivity.this.startActivity(intent);
                break;
            case R.id.bu_left:
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
            case R.id.bu_right:
                switch (order.getState()) {
                    case UNPAY:
                        //立即支付跳转支付方页面
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
                        Intent intent2 = new Intent(this, EvaluateActivity.class);

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

        RequestParams requestParams = new RequestParams(UrlAddress.url + "UpdateEmergencyOrder");

        MyApplication myApplication = (MyApplication) getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addBodyParameter("orderId", order.getOrderId() + "");
        requestParams.addBodyParameter("orderState", changeState + "");

        //更新订单，更新界面
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //  Log.i("OrderAllFragment", "onSuccess: " + result);
                if (result.equals("success")) {
                    switch (changeState) {
                        case CLOSE:
                            Toast.makeText(ItemActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            order.setState(CLOSE);
                            back();
                            finish();//销毁当前activity (返回键)
                            break;
                        case UNREMARK:
                            Toast.makeText(ItemActivity.this, "确认成功", Toast.LENGTH_SHORT).show();
                            order.setState(UNREMARK);
                            back();
                            break;
                        case REFUND:
                            Toast.makeText(ItemActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
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
        Log.i("333", "onSuccess: " + order.toString());
        intent.putExtra("orderId", order.getOrderId() + "");
        intent.putExtra("orderState", order.getState() + "");
        setResult(BACK, intent);
        super.onBackPressed();
    }


}
