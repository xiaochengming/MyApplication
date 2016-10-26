package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.TitleBar;
import com.example.administrator.myapplication.util.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EmergencyPlaceAnOrderActivity extends AppCompatActivity implements View.OnClickListener {


    Category category = null;
    User user;
    List<Address> address;
    Address addressIsefault = null;
    @InjectView(R.id.titlebar)
    TitleBar titlebar;
    @InjectView(R.id.bg_line)
    View bgLine;
    @InjectView(R.id.bg_line_bottom)
    View bgLineBottom;
    @InjectView(R.id.order_goumai)
    Button orderGoumai;
    @InjectView(R.id.order_total_money)
    TextView orderTotalMoney;
    @InjectView(R.id.order_bottom)
    RelativeLayout orderBottom;
    @InjectView(R.id.order_info_state_img)
    ImageView orderInfoStateImg;
    @InjectView(R.id.order_info_state)
    RelativeLayout orderInfoState;
    @InjectView(R.id.order_dizhi_left_tupian)
    ImageView orderDizhiLeftTupian;
    @InjectView(R.id.order_dizhi_username)
    TextView orderDizhiUsername;
    @InjectView(R.id.order_dizhi_phonenum)
    TextView orderDizhiPhonenum;
    @InjectView(R.id.order_dizhi_detaildizhi)
    TextView orderDizhiDetaildizhi;
    @InjectView(R.id.order_dizhi_detailinfo)
    RelativeLayout orderDizhiDetailinfo;
    @InjectView(R.id.order_dizhi_right_tupian)
    ImageView orderDizhiRightTupian;
    @InjectView(R.id.order_dizhi)
    RelativeLayout orderDizhi;
    @InjectView(R.id.order_prod_yunfei)
    TextView orderProdYunfei;
    @InjectView(R.id.order_prod_yunfei_money)
    TextView orderProdYunfeiMoney;
    @InjectView(R.id.order_prod_yunfeitotal)
    RelativeLayout orderProdYunfeitotal;
    @InjectView(R.id.order_count_total_money)
    TextView orderCountTotalMoney;
    @InjectView(R.id.order_count_heji)
    TextView orderCountHeji;
    @InjectView(R.id.order_count_money)
    RelativeLayout orderCountMoney;
    @InjectView(R.id.order_prod_fapiao_left)
    TextView orderProdFapiaoLeft;
    @InjectView(R.id.order_prod_fapiao_right)
    TextView orderProdFapiaoRight;
    @InjectView(R.id.order_prod_fapiao)
    RelativeLayout orderProdFapiao;
    @InjectView(R.id.order_ly_scrllview)
    LinearLayout orderLyScrllview;
    @InjectView(R.id.id_order_scollview)
    ScrollView idOrderScollview;
    boolean temp = false;
    @InjectView(R.id.text_name)
    TextView textName;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.emergency_order_name)
    RelativeLayout emergencyOrderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_order);
        ButterKnife.inject(this);
        //获取数据
        getData();
        //获取地址
        getDataToAddress();
        //初始控件
        initView();
        orderGoumai.setOnClickListener(this);
        orderDizhiRightTupian.setOnClickListener(this);
    }

    //获取数据
    public void getData() {
        Intent intent = getIntent();
        String categoryJson = intent.getStringExtra("categoryJson");
        String userJson = intent.getStringExtra("userJson");

        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        category = gson.fromJson(categoryJson, Category.class);
        user = gson.fromJson(userJson, User.class);
    }

    //获取地址
    public void getDataToAddress() {
        String url = UrlAddress.url + "AddressServletli";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        requestParams.addQueryStringParameter("userId", user.getUserId() + "");
        Callback.Cancelable cancelable = x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {

                        if (result != null) {
                            temp = true;
                            Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            address = gson.fromJson(result, new TypeToken<List<Address>>() {
                            }.getType());
                            Iterator<Address> iterator = address.iterator();
                            while (iterator.hasNext()) {
                                Address address = iterator.next();
                                if (address.getIsdefault() == 1) {
                                    addressIsefault = address;

                                }

                            }
                        } else {
                            temp = false;

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


    //生成订单
    public Order newOrder() {

        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        Order order = new Order(user, addressIsefault, nowTime, 1, category.getPrices().get(0).getPrice(),
                category, category.getPrices().get(0).getPrice(), null);//少了到达时间arrive
        return order;
    }

    //插入数据库
    public void toMySql() {
        String url = UrlAddress.url + "";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String orderJson = gson.toJson(newOrder());

        requestParams.addQueryStringParameter("orderJson", orderJson);
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            int orderId = Integer.getInteger(result);

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

    public void initView() {


        //初始化服务类型
        name.setText(category.getName());
        //初始化价格
        orderCountTotalMoney.setText(category.getPrices().get(0).getPrice() + "");
        orderTotalMoney.setText(category.getPrices().get(0).getPrice() + "");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_goumai:
                //下单按钮


                break;
            case R.id.order_dizhi_right_tupian:
                //地址按钮

                break;
        }
    }
}
