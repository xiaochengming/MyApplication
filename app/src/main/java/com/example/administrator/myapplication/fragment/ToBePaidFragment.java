package com.example.administrator.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.FuwuOrderItemActivity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by king on 2016/10/2.
 */
//待支付
public class ToBePaidFragment extends Fragment implements RefreshListView.OnRefreshUploadChangeListener {
    public static final Integer TOITEM = 10;
    Handler handler = new Handler();
    public static final Integer TOEVALUATE = 1;
    RefreshListView listView;
    List<Order> orders = new ArrayList<Order>();//存放订单信息
    int pageNo = 1;// 页号
    int pageSize = 5;// 页大小
    CommonAdapter<Order> orderApater;//适配器
    public static final int UNPAY = 1;//待付款
    public static final int UNSERVICE = 2;//待服务
    public static final int CLOSE = 6;//交易关闭
    //删除弹出框
    private AlertDialog.Builder builder;
    TextView teState;
    LinearLayout headSelectView;

    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_list, null);
        listView = (RefreshListView) view.findViewById(R.id.lv_workerList);
        //控件初始化
        initData();
        //item点击事件
        OnItemClickListener(orders);
        listView.setOnRefreshUploadChangeListener(this);
        return view;
    }

    //获取网路数据
    public void initData() {

        String url = StringUtil.ip + "/AllOrderServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        //  Log.d("用户id", +myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("orderState", UNPAY + "");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageNo", pageSize + "");


        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Order> orderList = gson.fromJson(result, new TypeToken<List<Order>>() {
                        }.getType());
                        orders.clear();
                        //移除交易关闭状态的订单
                        Iterator iterList = orderList.iterator();
                        while (iterList.hasNext()) {
                            Order order = (Order) iterList.next();
                            if (order.getState() == 6) {
                                iterList.remove();
                            }
                        }
                        orders.addAll(orderList);

                        if (orderApater == null) {
                            orderApater = new CommonAdapter<Order>(getActivity(),
                                    orders, R.layout.yan_fuwu_allorder) {
                                @Override
                                public void convert(ViewHolder holder, Order order, int position) {
                                    //控件赋值

                                    initView(holder, order, position);

                                }

                            };
                            changeLayout();
                            listView.setAdapter(orderApater);

                        } else {
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
        TextView typefaceServiceType = holder.getView(R.id.Typeface_ServiceType);
        typefaceServiceType.setText(order.getCategory().getName());
        teState = holder.getView(R.id.State);
        teState.setText(initState(order.getState()));
        TextView teAddress = holder.getView(R.id.tv_address);
        teAddress.setText(order.getAddress().getAddress());
        TextView time = holder.getView(R.id.order_textview_5);
        String placeAnOrderTime = String.valueOf(order.getTime());
        time.setText("下单时间: " + placeAnOrderTime.substring(0, placeAnOrderTime.length() - 2));
        TextView tePrice = holder.getView(R.id.price);
        tePrice.setText(order.getAllprice() + "");
        Button buttonLeft = holder.getView(R.id.button_left);
        Button buttonRight = holder.getView(R.id.button_right);
        //按钮控件初始化
        setButtonfromOrderState(buttonLeft, buttonRight, order);
        //按钮点击事件
        onButtonClick(buttonLeft, buttonRight, order, position);
    }

    //状态初始化
    public String initState(int orderState) {
        switch (orderState) {
            case UNPAY:
                return "待付款";
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
                buttonLeft.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonLeft.setText("取消订单");
                buttonRight.setText("确认订单");
                break;
        }

    }

    //按钮点击事件
    public void onButtonClick(final Button buttonLeft, Button buttonRight, final Order order, final int position) {

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (order.getState()) {
                    case UNPAY:
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
                        if (isCanPay(order)) {
                            Intent intent = new Intent(getActivity(), PayActivity.class);
                            intent.putExtra("order", order);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "订单已过期，请重新下单", Toast.LENGTH_SHORT).show();
                        }
                        break;

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

        }

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (changeState) {
                            case CLOSE:
                                changeState(order, position, CLOSE);
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
    public void changeState(Order order, final int position, final int changeState) {

        RequestParams requestParams = new RequestParams(StringUtil.ip + "/UpdateOrderServlet");

        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addBodyParameter("orderId", order.getOrderId() + "");
        requestParams.addBodyParameter("orderState", changeState + "");

        //更新订单，更新界面
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (changeState != UNPAY) {
                    orders.remove(position);
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
            //删除回调
            if (data != null) {
                int orderId = Integer.parseInt(data.getStringExtra("orderId"));
                int orderState = Integer.parseInt(data.getStringExtra("orderState"));

                Iterator iterList = orders.iterator();
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
                // Log.d("111", "我执行了");
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

    //item点击事件
    public void OnItemClickListener(final List<Order> ordrers) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listView.isFlag() == false && i != ordrers.size() + 1) {
                    Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String orderJson = gson.toJson(ordrers.get(i - 1));
                    Intent intent = new Intent(getActivity(), FuwuOrderItemActivity.class);
                    intent.putExtra("order", orderJson);
                    startActivityForResult(intent, TOITEM);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            initData();
        }
    }

    //上拉加载
    public void topPullLoading() {
        String url = StringUtil.ip + "/AllOrderServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        requestParams.addQueryStringParameter("orderState", UNPAY + "");
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

    //是否有订单的布局切换
    public void changeLayout() {
        listView.isShowOrder(orders, getContext());


    }

    //判断订单是否可以支付--》
    public boolean isCanPay(Order order) {
        //获取当前时间
        Date dt = new Date();
        Long newTime = dt.getTime();//这就是距离1970年1月1日0点0分0秒的毫秒数
        //获取开始服务时间
        long begdate = order.getBegdate().getTime();
        if (newTime > begdate) {
            return false;

        } else {
            return true;
        }
    }

}

