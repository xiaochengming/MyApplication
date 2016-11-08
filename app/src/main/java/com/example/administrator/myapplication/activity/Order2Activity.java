package com.example.administrator.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Order2Activity extends AppCompatActivity {
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.bt1)
    Button bt1;
    TextView count;
    TextView price;
    TextView name;
    Button yuYue;

    Category category;
    Address address;
    int pricepostion;
    MyApplication myApplication;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.tv_allprice)
    TextView tvAllprice;
    boolean flag;
    int Hid;
    TextView textView;
    TextView etNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);
        ButterKnife.inject(this);
        Button jia = (Button) findViewById(R.id.jia);
        Button jian = (Button) findViewById(R.id.jian);
        yuYue = (Button) findViewById(R.id.button);
        price = (TextView) findViewById(R.id.tv_price);
        count = (TextView) findViewById(R.id.count);
        textView= (TextView) findViewById(R.id.textView);
        etNumber= (TextView) findViewById(R.id.et_number);

        myApplication = (MyApplication) getApplication();
        Intent intent = getIntent();
        category = intent.getParcelableExtra("category");
        pricepostion = intent.getIntExtra("price", 0);
        Hid=intent.getIntExtra("hid",0);
        Button button = (Button) findViewById(R.id.button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.backs);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newcount = Integer.parseInt(count.getText().toString()) + 1;
                count.setText(newcount + "");
                float sum = newcount * category.getPrices().get(pricepostion).getPrice();
                tvAllprice.setText("总价：￥" + sum);
            }
        });

        jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(count.getText().toString()) > 1) {
                    int newcount2 = Integer.parseInt(count.getText().toString()) - 1;
                    count.setText(newcount2 + "");
                    float sum = newcount2 * category.getPrices().get(pricepostion).getPrice();
                    tvAllprice.setText("总价：￥" + sum);
                }
            }
        });
        initView();
    }
    //初始化界面
    private void initView() {
        Event();
        //设置标题
        title.setText(category.getName());
        price.setText("￥" + category.getPrices().get(pricepostion).getPrice() + category.getPrices().get(pricepostion).getUnit());
        tvAllprice.setText("总价：￥" + category.getPrices().get(pricepostion).getPrice());
        if (category.getPrices().get(pricepostion).getSubname() != null) {
            title.setText(category.getName() + "-" + category.getPrices().get(pricepostion).getSubname());
        } else {
            title.setText(category.getName());
        }
//        switch (category.getPrices().get(pricepostion).getUnit()){
//            case("/小时"):
//                textView.setText("时长:");
//                break;
//            case("/月"):
//                textView.setText("时长:");
//                break;
//            case("/人一天"):
//                textView.setText("人数:");
//                break;

//        }

        //地址选择点击事件
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myApplication.getUser().getUserId()>0){
                    //跳转到预约地址界面
                    Intent intent=new Intent(Order2Activity.this,YuYueAddressActivity.class);
                    startActivityForResult(intent,1001);
                }else {
                    Intent intent=new Intent(Order2Activity.this,LoginActivity.class);
                    startActivityForResult(intent,201);
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==1001){
            if (data!=null&&data.getParcelableExtra("address")!=null){
                address=data.getParcelableExtra("address");
                tv1.setText(address.getAddress());
            }
            return;
        }
        if (resultCode==RESULT_OK&&requestCode==201){
            Event();
        }
    }

    public void Event() {

        //获取默认地址
        String url = StringUtil.ip + "/AddressServlet";

        final RequestParams requestParams = new RequestParams(url);
        requestParams.addBodyParameter("userId", myApplication.getUser().getUserId() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("OrderActivity", "onSuccess: " + result);
                Gson gson = new Gson();
                address = gson.fromJson(result, Address.class);
                if (address == null) {
                    //yuYue.setVisibility(View.INVISIBLE);
                    tv1.setText("请填写服务地址");
                } else {
                    tv1.setText(address.getAddress());
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

    //点击事件 选择服务时间
    public void yuyue(View v) {
        Log.i("OrderActivity", "yuyue:  ");
        String s=etNumber.getText().toString();
        if(s.isEmpty()||s.equals("")){
            Toast.makeText(Order2Activity.this, "请输入面积", Toast.LENGTH_SHORT).show();
            return;
        }else if (Integer.parseInt(s)>500&&Integer.parseInt(s)<0){
            Toast.makeText(Order2Activity.this, "输入错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address == null) {
            Intent intent=new Intent(this,LoginActivity.class);
            startActivityForResult(intent,201);
        } else {
            Intent intent = new Intent(this, TimeActivity.class);
            float jiage = category.getPrices().get(pricepostion).getPrice();
            int number = Integer.parseInt(s);
            float allprice = number * jiage;
            int workertime = Integer.parseInt(s)/10;
            //赋值
            Timestamp time = null;
         /*(int orderId, User user, Address address, Timestamp time,
        int state, float allprice, Timestamp begdate, int workerTime,
        Housekeeper housekeeper, Category category, float price,
        int number, String worker, String subname, Time arriveTime,
                Timestamp endtime)*/
            Order order1 = new Order(-1, myApplication.getUser(), address, new Timestamp(System.currentTimeMillis()),
                    1, allprice, time, workertime, null, category, jiage, number, null, category.getPrices().
                    get(pricepostion).getSubname(), null, null);
            if (Hid!=0){
                order1.setHousekeeper(new Housekeeper(Hid));
            }

            if (category.getType().equals("居家换新") || category.getType().equals("搬家服务")) {
                jiSuanJieSuShiJian(order1, 2);
            } else if (category.getType().equals("保姆月嫂")) {
                jiSuanJieSuShiJian(order1, 3);
            } else {
                intent.putExtra("order", order1);
                startActivity(intent);
            }
        }

    }

    public void jiSuanJieSuShiJian(final Order order, final int type) {
        Log.i("OrderActivity", "jiSuanJieSuShiJian: 执行");
        flag = true;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (flag) {
                    flag=false;
                    Log.i("OrderActivity", "onDateSet: 点击响应");
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    Calendar c2 = Calendar.getInstance();
                    Calendar c3 = Calendar.getInstance();
                    c2.add(Calendar.MONTH, 1);
                    c2.set(Calendar.DAY_OF_MONTH, 1);
                    c2.add(Calendar.DAY_OF_MONTH, -1);//当月最后一天
                    c3.add(Calendar.MONTH, 4);
                    c3.set(Calendar.DAY_OF_MONTH, 1);
                    c3.add(Calendar.DAY_OF_MONTH, -1);//当月最后一天
                    if (calendar.before(c3) && calendar.after(c2)) {
                        Log.i("OrderActivity", "onDateSet: 符合要求c1:" + new Timestamp(calendar.getTimeInMillis()) + "c2:" + new Timestamp(c2.getTimeInMillis()) + "c3:" + new Timestamp(c3.getTimeInMillis()));
                        Timestamp time = new Timestamp(calendar.getTimeInMillis());
                        order.setBegdate(time);
                        //开始时间没有插 结束没有问题
                        //Log.i("OrderActivity", "onDateSet: " + calendar);
                        switch (type) {
                            case 2:
                                //天
                                calendar.add(Calendar.DAY_OF_MONTH, order.getWorkerTime());
                                order.setEndtime(new Timestamp(calendar.getTimeInMillis()));
                                break;
                            case 3:
                                //月
                                calendar.add(Calendar.MONTH, order.getWorkerTime());
                                order.setEndtime(new Timestamp(calendar.getTimeInMillis()));
                                break;
                        }
                        //将订单插入数据库后跳转到结账页面
                        insertOrder(order);
                    } else {
                        Log.i("OrderActivity", "onDateSet: 不符合要求 c1:" + new Timestamp(calendar.getTimeInMillis()) + "c2:" + new Timestamp(c2.getTimeInMillis()) + "c3:" + new Timestamp(c3.getTimeInMillis()));
                        Toast.makeText(Order2Activity.this, "时间选择错误时间为下月开始三个月之内", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    //将订单插入数据库后跳转到结账页面
    private void insertOrder(final Order order) {
        //插入数据库
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String orderStr = gson.toJson(order);
        RequestParams params = new RequestParams(StringUtil.ip + "/InsertOrderServlet");
        params.addQueryStringParameter("order", orderStr);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (Integer.parseInt(result) == -1) {
                    Toast.makeText(Order2Activity.this, "订单存在请到订单页面查看", Toast.LENGTH_SHORT).show();
                } else {
                    order.setOrderId(Integer.parseInt(result));
                    //跳转到结账页面
                    Intent intent = new Intent(Order2Activity.this, PayActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
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
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
