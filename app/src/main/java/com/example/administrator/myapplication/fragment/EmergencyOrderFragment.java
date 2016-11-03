package com.example.administrator.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.FuwuOrderEvaluateActivity;
import com.example.administrator.myapplication.activity.EmergencyOrderItemActivity;
import com.example.administrator.myapplication.activity.PayActivity;
import com.example.administrator.myapplication.entity.Order;

import com.example.administrator.myapplication.util.CommonAdapter;
import com.example.administrator.myapplication.util.RefreshListView;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by king on 2016/10/12.
 */
//应急订单

public class EmergencyOrderFragment extends Fragment implements RefreshListView.OnRefreshUploadChangeListener {
    public static final Integer TOITEM = 10;
    public static final Integer TOEVALUATE = 1;
    LinearLayout headSelectView;
    Handler handler = new Handler();
    RefreshListView listView;
    List<Order> orders = new ArrayList<Order>();//存放订单信息
    int pageNo = 1;// 页号
    int pageSize = 5;// 页大小
    CommonAdapter<Order> orderApater;//适配器
    public static final int UNPAY = 1;//待付款
    public static final int UNSERVICE = 2;//待服务
    public static final int UNREMARK = 3;//待评价
    public static final int COMPLETE = 4;//订单完成
    public static final int COMPLAINT = 5;//申请售后
    public static final int CLOSE = 6;//交易关闭
    public static final int REFUND = 7;//退款
    //删除弹出框
    private AlertDialog.Builder builder;
    TextView teState;
    TextView typefaceServiceType;
    ImageView imageView;
    List<Order> orderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_list, null);
        listView = (RefreshListView) view.findViewById(R.id.lv_workerList);

        //控件初始化
        initData();
        //item点击事件
        listView.setOnRefreshUploadChangeListener(this);
        OnItemClickListener(orders);
        //时间倒计时
        Message message = handler1.obtainMessage(1);     // Message
        handler1.sendMessageDelayed(message, 1000);
        return view;
    }

    //获取网路数据
    public void initData() {

        String url = StringUtil.ip + "/AllEmergencyOrderServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Order> orderList = gson.fromJson(result, new TypeToken<List<Order>>() {
                        }.getType());
                        /**
                         * 对订单状态2的判断---》组长还未做endtime代码还未测试
                         */
                        //  changeOrderStateByTwo(orderList);
                        orders.clear();
                        orders.addAll(orderList);
                        if (orderApater == null) {
                            orderApater = new CommonAdapter<Order>(getActivity(), orders, R.layout.yan_emergency_order) {
                                @Override
                                public void convert(ViewHolder holder, Order order, int position) {
                                    //控件赋值

                                    initView(holder, order, position);

                                }

                            };
                            //切换listview底部
                            changeLayout();
                            listView.setAdapter(orderApater);
                        } else {
                            //切换listview底部
                            changeLayout();
                            orderApater.notifyDataSetChanged();

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


    //控件初始化
    public void initView(ViewHolder holder, Order order, int position) {
        typefaceServiceType = holder.getView(R.id.Typeface_ServiceType);
        typefaceServiceType.setText(order.getCategory().getName());
        teState = holder.getView(R.id.State);
        teState.setText(initState(order.getState()));
        TextView teAddress = holder.getView(R.id.tv_address);
        teAddress.setText(order.getAddress().getAddress());
        TextView tePrice = holder.getView(R.id.price);
        tePrice.setText(order.getAllprice() + "");
        Button buttonLeft = holder.getView(R.id.button_left);
        Button buttonRight = holder.getView(R.id.button_right);
        if (order.getCategory() != null) {
            imageView = holder.getView(R.id.img_housekeeper_photo);
            x.image().bind(imageView, StringUtil.ip + order.getCategory().getIcon());

        }
        //按钮控件初始化
        setButtonfromOrderState(buttonLeft, buttonRight, order);
        //按钮点击事件
        onButtonClick(buttonLeft, buttonRight, order, position);
        //倒计时控件
        TextView textView = holder.getView(R.id.time_countc_down);
        if (order.getState() == 3 || order.getState() == 4) {
            //订单已经完成
            textView.setText("00:00:00");
        } else {
            textView.setText(order.getArriveTime() + "");
        }

    }


    //状态初始化
    public String initState(int orderState) {
        switch (orderState) {
            case UNPAY:

                return "待付款";
            case UNSERVICE:
                return "待服务";
            case UNREMARK:
                return "待评价";
            case COMPLETE:
                return "交易完成";
            case REFUND:
                return "退款中";
        }

        return null;
    }

    //按钮控件初始化
    public void setButtonfromOrderState(Button buttonLeft, Button buttonRight, Order order) {
        switch (order.getState()) {
            case UNPAY:
                buttonLeft.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonLeft.setText("删除订单");
                buttonRight.setText("立即支付");
                break;
            case UNSERVICE:
                buttonLeft.setVisibility(View.INVISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonRight.setText("确认订单");
                break;
            case UNREMARK:
                buttonLeft.setVisibility(View.VISIBLE);
                buttonLeft.setText("删除订单");
                buttonRight.setText("立即评价");
                break;
            case COMPLETE:
                buttonLeft.setVisibility(View.INVISIBLE);
                buttonRight.setText("删除订单");
                break;
            case COMPLAINT:
                buttonLeft.setVisibility(View.GONE);
                buttonRight.setVisibility(View.GONE);
                break;
            case REFUND:
                buttonLeft.setVisibility(View.GONE);
                buttonRight.setVisibility(View.GONE);
                teState.setText(initState(REFUND));

                break;
        }

    }

    //按钮点击事件
    public void onButtonClick(Button buttonLeft, Button buttonRight, final Order order, final int position) {

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (order.getState()) {
                    case UNPAY:
                        //删除订单
                        dialog(order, position, CLOSE);
                        break;

                    case UNREMARK:
                        //删除订单
                        dialog(order, position, CLOSE);
                        break;
                }

            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (order.getState()) {
                    case UNPAY:
                        //跳转到支付界面
                        Intent intent = new Intent(getActivity(), PayActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                        break;
                    case UNSERVICE:
                        //在服务完成时候可以确认订单
                        if (ontimeListener(position)) {
                            dialog(order, position, UNREMARK);
                        } else {

                            Toast.makeText(getActivity(), "未服务", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case UNREMARK:
                        //待评价
                        Intent intent2 = new Intent(getActivity(), FuwuOrderEvaluateActivity.class);
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        String or = gson.toJson(order);
                        intent2.putExtra("Order", or);

                        startActivityForResult(intent2, TOEVALUATE);
                        break;
                    case COMPLETE:
                        //删除订单
                        dialog(order, position, CLOSE);
                        break;
                }

            }
        });

    }

    //item点击事件
    public void OnItemClickListener(final List<Order> ordrers) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listView.isFlag() == false && i != ordrers.size() + 1) {
                    Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String orderJson = gson.toJson(ordrers.get(i - 1));
                    Intent intent = new Intent(getActivity(), EmergencyOrderItemActivity.class);
                    intent.putExtra("order", orderJson);
                    startActivityForResult(intent, TOITEM);
                }
            }
        });
    }

    //删除订单弹出框
    public void dialog(final Order order, final int position, final int changeState) {
        builder = new AlertDialog.Builder(getActivity());
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
                                changeState(order, position, CLOSE);
                                break;
                            case UNREMARK:
                                changeState(order, position, UNREMARK);
                                break;
                            case REFUND:
                                changeState(order, position, REFUND);
                                break;
                        }
                    }
                }
        );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.create().show();

    }

    //更新订单状态，更新界面
    public void changeState(final Order order, final int position, final int changeState) {

        RequestParams requestParams = new RequestParams(StringUtil.ip + "/UpdateEmergencyOrder");

        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addBodyParameter("orderId", order.getOrderId() + "");
        requestParams.addBodyParameter("orderState", changeState + "");


        //更新订单，更新界面
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                if (result.equals("success") && position != -1) {
                    switch (changeState) {
                        case CLOSE:
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                            orders.remove(position);
                            break;
                        case UNREMARK:
                            Toast.makeText(getActivity(), "确认成功", Toast.LENGTH_SHORT).show();
                            orders.get(position).setState(UNREMARK);
                            break;
                        case REFUND:
                            Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show();
                            orders.get(position).setState(REFUND);
                            break;
                    }
                    //更新界面
                    changeLayout();
                    orderApater.notifyDataSetChanged();//更新界面


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

    //back完成之后跟新界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOITEM && resultCode == 2) {
            // Log.d("requestCode+resultCode", "我执行了");
            //删除回调
            if (data != null) {
                int orderId = Integer.parseInt(data.getStringExtra("orderId"));
                int orderState = Integer.parseInt(data.getStringExtra("orderState"));

                Iterator iterList = orders.iterator();
                Log.d("requestCode+resultCode", orderState + "");
                while (iterList.hasNext()) {
                    Order backOrder = (Order) iterList.next();
                    if (backOrder.getOrderId() == orderId) {
                        //删除
                        if (orderState == CLOSE) {
                            iterList.remove();
                        } else {
                            backOrder.setState(orderState);
                        }
                    }

                }


            }
            changeLayout();
            orderApater.notifyDataSetChanged();
        }

        if (requestCode == TOEVALUATE && resultCode == 2)

        {

            if (data != null) {

                int orderId = Integer.parseInt(data.getStringExtra("orderId"));
                int orderState = Integer.parseInt(data.getStringExtra("orderState"));
                Iterator iterList = orders.iterator();
                while (iterList.hasNext()) {
                    Order order = (Order) iterList.next();
                    if (order.getOrderId() == orderId) {
                        order.setState(orderState);
                    }

                }
            }
            changeLayout();
            orderApater.notifyDataSetChanged();
        }

    }

    //上拉加载
    public void topPullLoading() {
        String url = StringUtil.ip + "/AllEmergencyOrderServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        Callback.Cancelable cancelable = x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Order> orderList = gson.fromJson(result, new TypeToken<List<Order>>() {
                        }.getType());

                        if (orderList.size() == 0) {
                            Toast.makeText(getActivity(), "没有新的数据", Toast.LENGTH_SHORT).show();
                            pageNo--;
                            return;
                        }
                        orders.addAll(orderList);
                        if (orderApater == null) {
                            orderApater = new CommonAdapter<Order>(getActivity(), orders, R.layout.yan_fuwu_allorder) {
                                @Override
                                public void convert(ViewHolder holder, Order order, int position) {
                                    //控件赋值

                                    initView(holder, order, position);
                                }

                            };
                            //切换listview底部
                            changeLayout();
                            listView.setAdapter(orderApater);

                        } else {
                            //切换listview底部
                            changeLayout();
                            orderApater.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        //刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                initData();
                listView.completeRefresh();//刷新数据后，改变界面
            }
        }, 1000);

    }

    @Override
    public void onPull() {
        pageNo++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                topPullLoading();
                listView.completeLoad();
            }
        }, 1000);

    }


    //判断是否可以“确认下单”||是否可以取消订单
    public boolean ontimeListener(int position) {
        int newTime = 0;
        int endtime = 0;
        //获取当前时间
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time1 = sdf.format(System.currentTimeMillis());
        //获取结束时间
        String time2 = sdf.format(orders.get(position).getEndtime());
        if (Long.parseLong(time1) >= (Long.parseLong(time2) + 500000)) {
            return true;
        }

        return false;
    }

    //是否有订单的布局切换
    public void changeLayout() {
        listView.isShowOrder(orders, getContext());
    }

    /**
     * 判断订单状态是2（待服务时候）若已经服务而用户未确定则改变订单状态
     */

    public List<Order> changeOrderStateByTwo(List<Order> changeOrder) {
        Iterator<Order> iterator = changeOrder.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getState() == 2) {
                //获取当前时间&&订单结束时间
                DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String newTime = sdf.format(System.currentTimeMillis());
                String endTime = sdf.format(order.getEndtime());
                if (Long.parseLong(newTime) >= Long.parseLong(endTime)) {
                    //更新数据库
                    changeState(order, -1, 3);
                    order.setState(3);
                }
            }

        }
        return changeOrder;
    }

    //倒计时
    private Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    for (int index = 0; index < orders.size(); index++) {
                        Order order = orders.get(index);
                        //订单为支付时候不倒计时
                        if (order.getState() == 1) {


                        } else {
                            long time = order.getArriveTime().getTime();

                            if (time > 1000) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                                isNeedCountTime = true;
                                long arrive = order.getArriveTime().getTime() - 1000;
                                // Log.d("EmergencyOrderFragment", "handleMessage: " + time);
                                Time time1 = new Time(arrive);
                                order.setArriveTime(time1);

                            } else {
                                Time time2 = new Time(0);
                                order.setArriveTime(time2);
                            }
                        }

                    }
                    //②：for循环执行的时间
                    if (orderApater != null) {
                        orderApater.notifyDataSetChanged();
                        if (isNeedCountTime) {
                            //TODO 然后用1000-（②-①），就赢延迟的时间
                            handler.sendEmptyMessageDelayed(1, 1000);
                            Message message = handler1.obtainMessage(1);
                            handler1.sendMessageDelayed(message, 1000);
                            break;
                        }
                    } else {
                        Message message = handler1.obtainMessage(1);
                        handler1.sendMessageDelayed(message, 1000);
                    }
            }

        }

    };
}


