package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TimeActivity extends AppCompatActivity {
    @InjectView(R.id.time0)
    RadioButton time0;
    @InjectView(R.id.time1)
    RadioButton time1;
    @InjectView(R.id.time2)
    RadioButton time2;
    @InjectView(R.id.v4)
    View v4;
    @InjectView(R.id.time3)
    Button time3;
    @InjectView(R.id.time4)
    Button time4;
    @InjectView(R.id.time5)
    Button time5;
    @InjectView(R.id.time6)
    Button time6;
    @InjectView(R.id.time7)
    Button time7;
    @InjectView(R.id.time8)
    Button time8;
    @InjectView(R.id.time9)
    Button time9;
    @InjectView(R.id.time10)
    Button time10;
    @InjectView(R.id.time11)
    Button time11;
    @InjectView(R.id.time12)
    Button time12;
    @InjectView(R.id.time13)
    Button time13;
    @InjectView(R.id.time14)
    Button time14;
    @InjectView(R.id.tableLayout)
    TableLayout tableLayout;
    @InjectView(R.id.v3)
    View v3;
    @InjectView(R.id.button2)
    Button button2;
    String date;
    String date1;
    String flag = null;
    @InjectView(R.id.group)
    RadioGroup group;
    Category category;
    Address address;
    MyApplication myApplication;
    Timestamp time;
    Order order;
    int nowbutton;
    Button[] buttons;
    List<Boolean> haslist; //订单是否存在集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        myApplication = (MyApplication) getApplication();
        ButterKnife.inject(this);
        buttons = new Button[]{time3, time4, time5, time6, time7, time8, time9, time10, time11, time12, time13, time14};
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
        category = order.getCategory();
        orderhas();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.backs);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());
        //服务日选择点击事件
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.time0:
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf.format(new Date());
                        date1 = null;
                        nowbutton=0;
                        initview();
                        if (haslist!=null&&haslist.get(0)==true){
                            closeButton();
                        }
                        break;
                    case R.id.time1:
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf1.format(c.getTime());
                        date1 = null;
                        allCheck();
                        nowbutton = 1;
                        if (haslist!=null&&haslist.get(1)==true){
                            closeButton();
                        }
                        break;
                    case R.id.time2:
                        Calendar c1 = Calendar.getInstance();
                        c1.add(Calendar.DAY_OF_MONTH, 2);
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf2.format(c1.getTime());
                        date1 = null;
                        allCheck();
                        nowbutton = 2;
                        if (haslist!=null&&haslist.get(2)==true){
                            closeButton();
                        }
                        break;
                }
            }
        });
        initview();
        yincan();
    }
    public void yincan(){
        for (int i=0;i<buttons.length;i++){
            buttons[i].setVisibility(View.INVISIBLE);
        }
    }
    public void xianshi(){
        for (int i=0;i<buttons.length;i++){
            buttons[i].setVisibility(View.VISIBLE);
        }
    }

    private void initview() {
        //初始化时间选择器 返回一个服务员id
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.i("TimeActivity", "initview: hour" + hour);
        if (hour < 5) {
            // return;
        } else if (hour > 16) {
            time0.setEnabled(false);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(false);
            }
            time1.setChecked(true);
        } else {
            for (int i = 0; i < hour - 5; i++) {
                Log.i("TimeActivity", "initview: i=" + i);
                buttons[i].setEnabled(false);
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            buxian(i);
        }
    }

    private void allCheck() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
            buxian(i);
        }
    }
//设置所有按钮不能点
    public void  closeButton(){
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            buxian(i);
        }
    }

    public void xian(int key) {
        if (key == 0) {
            time3.setBackground(getResources().getDrawable(R.mipmap.time2));

        }
        if (key == 1) {
            time4.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 2) {
            time5.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 3) {
            time6.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 4) {
            time7.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 5) {
            time8.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 6) {
            time9.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 7) {
            time10.setBackground(getResources().getDrawable(R.mipmap.time2));
        }
        if (key == 8) {
            time11.setBackground(getResources().getDrawable(R.mipmap.time2));

        }
        if (key == 9) {
            time12.setBackground(getResources().getDrawable(R.mipmap.time2));

        }
        if (key == 10) {
            time13.setBackground(getResources().getDrawable(R.mipmap.time2));

        }
        if (key == 11) {
            time14.setBackground(getResources().getDrawable(R.mipmap.time2));

        }
    }

    //设置button不显示
    public void buxian(int key) {
        if (key == 0)
            time3.setBackground(getResources().getDrawable(R.color.baise));
        if (key == 1) {
            time4.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 2) {
            time5.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 3) {
            time6.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 4) {
            time7.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 5) {
            time8.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 6) {
            time9.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 7) {
            time10.setBackground(getResources().getDrawable(R.color.baise));
        }
        if (key == 8) {
            time11.setBackground(getResources().getDrawable(R.color.baise));

        }
        if (key == 9) {
            time12.setBackground(getResources().getDrawable(R.color.baise));

        }
        if (key == 10) {
            time13.setBackground(getResources().getDrawable(R.color.baise));

        }
        if (key == 11) {
            time14.setBackground(getResources().getDrawable(R.color.baise));

        }
    }

    //判断
    public void panduan(int key) {
        if (flag != null) {
            buxian(Integer.parseInt(flag));
        }
        xian(key);
        flag = key + "";
    }


    @OnClick({R.id.time3, R.id.time4, R.id.time5, R.id.time6, R.id.time7, R.id.time8, R.id.time9, R.id.time10, R.id.time11, R.id.time12, R.id.time13, R.id.time14})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time3:
                panduan(0);
                date1 = time3.getText().toString();
                break;
            case R.id.time4:
                panduan(1);
                date1 = time4.getText().toString();
                break;
            case R.id.time5:
                panduan(2);
                date1 = time5.getText().toString();
                break;
            case R.id.time6:
                panduan(3);
                date1 = time6.getText().toString();
                break;
            case R.id.time7:
                panduan(4);
                date1 = time7.getText().toString();
                break;
            case R.id.time8:
                panduan(5);
                date1 = time8.getText().toString();
                break;
            case R.id.time9:
                panduan(6);
                date1 = time9.getText().toString();
                break;
            case R.id.time10:
                panduan(7);
                date1 = time10.getText().toString();
                break;
            case R.id.time11:
                panduan(8);
                date1 = time11.getText().toString();
                break;
            case R.id.time12:
                panduan(9);
                date1 = time12.getText().toString();
                Log.i("aa", date1);
                break;
            case R.id.time13:
                panduan(10);
                date1 = time13.getText().toString();
                Log.i("aa", date1);
                break;
            case R.id.time14:
                panduan(11);
                date1 = time14.getText().toString();
                Log.i("aa", date1);
                break;
        }
    }

    //提交订单
    @OnClick(R.id.button2)
    public void onClick() {
        Log.i("TimeActivity", "onClick:date:" + date + "--" + date1);
        if (date1 == null) {
            Toast.makeText(TimeActivity.this, "请选择服务时间", Toast.LENGTH_SHORT).show();
        } else {
            String aa = date.toString() + " " + (date1.toString() + ":00");
            Log.i("aa", "时间" + aa);
            time = Timestamp.valueOf(aa);
            order.setBegdate(time);
            Log.i("aa", "转化的时间 " + time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.HOUR_OF_DAY, order.getWorkerTime());

            order.setEndtime(new Timestamp(calendar.getTimeInMillis()));
//        SimpleDateFormat sim=new SimpleDateFormat("hh:mm:ss");
//        try {
//           Date dateStr = sim.parse(date1+":00");
//            Log.i("aa", "日期"+dateStr);
//            time=Timestamp.valueOf(date+" "+dateStr);
//            Log.i("aa", "时间"+time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

            String url = StringUtil.ip + "/InsertOrderServlet";
            Log.i("aa", "进来了");
            //请求访问服务器
            RequestParams reqestparams = new RequestParams(url);
            //传参数
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            String orderJson = gson.toJson(order);
            Log.i("aa", "orderJson:" + orderJson);
            reqestparams.addBodyParameter("order", orderJson);
            x.http().post(reqestparams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("aa", "成功" + result);

                    if (Integer.parseInt(result) == -1) {
                        Toast.makeText(TimeActivity.this, "您当天已预约了该服务请到订单页面查看", Toast.LENGTH_SHORT).show();

                    } else {
                        order.setOrderId(Integer.parseInt(result));
                        Intent intent = new Intent(TimeActivity.this, PayActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("aa", "失败" + ex);

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

    public void orderhas() {
        List<Order> orders = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        calendar3.add(Calendar.DAY_OF_MONTH, 2);
        orders.add(new Order(new Timestamp(calendar.getTimeInMillis()), order.getUser(), order.getCategory()));
        orders.add(new Order(new Timestamp(calendar2.getTimeInMillis()), order.getUser(), order.getCategory()));
        orders.add(new Order(new Timestamp(calendar3.getTimeInMillis()), order.getUser(), order.getCategory()));
        RequestParams params = new RequestParams(StringUtil.ip + "/HasOrderServlet");
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String string=gson.toJson(orders);
        params.addBodyParameter("orders",string);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TimeActivity", "onSuccess: "+result);
                xianshi();
                Gson gson=new Gson();
                Type type=new TypeToken<List<Boolean>>(){}.getType();
                haslist=gson.fromJson(result,type);
                if (haslist.get(nowbutton)){
                    closeButton();
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
}






