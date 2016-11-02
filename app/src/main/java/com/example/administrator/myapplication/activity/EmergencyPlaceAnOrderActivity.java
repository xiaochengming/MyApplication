package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Evaluate;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.photoview.log.LoggerDefault;

public class EmergencyPlaceAnOrderActivity extends AppCompatActivity implements View.OnClickListener {


    Category category = null;
    User user;
    List<Address> address;
    List<Evaluate> evaluates;
    Address addressIsefault = null;
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
    Integer orderId;
    Timestamp nowTime;
    Time time;
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
    @InjectView(R.id.id_prod_list_tv)
    TextView idProdListTv;
    @InjectView(R.id.line1)
    LinearLayout line1;
    //获取提供服务的保姆|维修人员
    List<Housekeeper> housekeepers;
    //提供服务的保姆|维修人员的名字
    List<String> hou;
    @InjectView(R.id.spinner)
    Spinner spinner;
    Housekeeper housekeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_order);
        ButterKnife.inject(this);
        //获取数据
        getData();
        //初始控件
        getDataToAddress();

        //获取地址
        getDataToAddress();
        orderGoumai.setOnClickListener(this);
        orderDizhiRightTupian.setOnClickListener(this);


    }

    //获取数据
    public void getData() {
        Intent intent = getIntent();
        String categoryJson = intent.getStringExtra("categoryJson");
        String userJson = intent.getStringExtra("userJson");
        String housekeepersJson = intent.getStringExtra("housekeepersJson");
        Log.d("getData", "getData: " + housekeepersJson);
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        category = gson.fromJson(categoryJson, Category.class);
        user = gson.fromJson(userJson, User.class);
        housekeepers = gson.fromJson(housekeepersJson, new TypeToken<List<Housekeeper>>() {
        }.getType());

    }

    //获取地址
    public void getDataToAddress() {
        String url = StringUtil.ip + "/AddressServletli";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        requestParams.addQueryStringParameter("userId", user.getUserId() + "");

        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
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
                            if (addressIsefault != null) {

                                orderDizhiPhonenum.setText(addressIsefault.getUserName());
                                orderDizhiDetaildizhi.setText(addressIsefault.getAddress());
                            }
                            //初始化服务类型
                            name.setText(category.getName());
                            //初始化价格
                            orderCountTotalMoney.setText(String.valueOf(category.getPrices().get(0).getPrice()));
                            orderTotalMoney.setText(String.valueOf(category.getPrices().get(0).getPrice()));
                            //初始化选择保姆|维修人员
                            hou = new ArrayList<String>();
                            for (int i = 0; i < housekeepers.size(); i++) {
                                String name = housekeepers.get(i).getName();
                                Log.d("hou", "hou: " + name);
                                hou.add(name);

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EmergencyPlaceAnOrderActivity.this,
                                    android.R.layout.simple_spinner_item, hou);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    housekeeper = housekeepers.get(i);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("EmergencyPlay", "onError: " + ex);
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
        if (addressIsefault != null) {
            nowTime = new Timestamp(System.currentTimeMillis());
            time = new Time(System.currentTimeMillis());//创建一个时间对象，获取到当前的时间
            Order order = new Order(user, addressIsefault, nowTime, 1, category.getPrices().get(0).getPrice(),
                    housekeeper, category, category.getPrices().get(0).getPrice(), time);//少了到达时间arrive
            return order;
        }
        return null;
    }

    //插入数据库
    public void toMySqlOder() {
        String url = StringUtil.ip + "/EmergencyPlaceAnOrderServlet";
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
                            //返回订单id
                            orderId = Integer.parseInt(result);

                            Intent intent = new Intent(EmergencyPlaceAnOrderActivity.this, PayActivity.class);
                            Order order = new Order(orderId, user, addressIsefault, nowTime, 1, category.getPrices().get(0).getPrice(),
                                    housekeeper, category, category.getPrices().get(0).getPrice(), time);
                            intent.putExtra("order", order);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("Emerge", "onError: " + ex);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_goumai:
                //下单按钮
                toMySqlOder();
                break;
            case R.id.order_dizhi_right_tupian:
                //地址按钮
                getEvaluates();
                break;
            case R.id.id_prod_list_iv_left:
                //地址按钮
                finish();
                break;

        }
    }

    public void getEvaluates() {
        String url = UrlAddress.url + "Yan_EmergencyEvaluate";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("categoryId", category.getName() + "");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            Log.d("Emergenc", "onSuccess: " + result);
                            Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            evaluates = gson.fromJson(result, new TypeToken<List<Address>>() {
                            }.getType());
                            Iterator<Evaluate> iterator = evaluates.iterator();

                        } else {


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

    //spinner监听
    public void onListenerSpinner() {

    }
}
