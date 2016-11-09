package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Evaluate;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.CommonAdapter;
import com.example.administrator.myapplication.util.MyListNoScroll;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by king on 2016/10/19.
 */
public class FuWuItemActivity extends AppCompatActivity implements View.OnClickListener {
    int pageNo = 1;// 页号
    int pageSize = 5;// 页大小

    Category category;
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
    @InjectView(R.id.prod_info_tv_des_name)
    TextView prodInfoTvDesName;
    @InjectView(R.id.prod_info_tv_price)
    TextView prodInfoTvPrice;
    @InjectView(R.id.prod_info_cart)
    Button prodInfoCart;
    @InjectView(R.id.prod_info_nowbuy)
    Button prodInfoNowbuy;
    @InjectView(R.id.housekeeper_num)
    TextView housekeeperNum;


    ListView listView;
    String categoryName;
    List<Evaluate> evaluates = new ArrayList<>();
    CommonAdapter evaluateAdapter;
    TextView textView;
    List<Housekeeper> housekeepers = new ArrayList<>();
    @InjectView(R.id.id_prod_list_tv)
    TextView idProdListTv;
    @InjectView(R.id.line1)
    LinearLayout line1;
    @InjectView(R.id.img_photo)
    ImageView imgPhoto;
    @InjectView(R.id.prod_info_linearly)
    LinearLayout prodInfoLinearly;
    @InjectView(R.id.line2)
    LinearLayout line2;
    @InjectView(R.id.price)
    TextView price;
    @InjectView(R.id.num)
    TextView num;
    @InjectView(R.id.rela1)
    RelativeLayout rela1;
    @InjectView(R.id.prod_info_tv_prod_record)
    TextView prodInfoTvProdRecord;
    @InjectView(R.id.rela2)
    RelativeLayout rela2;
    @InjectView(R.id.lv_user_remark)
    MyListNoScroll lvUserRemark;
    @InjectView(R.id.text)
    TextView text;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_emergency_service_item);
        ButterKnife.inject(this);
        listView = (ListView) findViewById(R.id.lv_user_remark);
        textView = (TextView) listView.findViewById(R.id.prod_info_tv_prod_comment);
        textView.setOnClickListener(this);
        prodInfoCart.setOnClickListener(this);
        prodInfoNowbuy.setOnClickListener(this);
        idProdListIvLeft.setOnClickListener(this);

        //获取数据
        getData();
        //初始化控件
        initView();
        //初始化评价

        initDataEvaluate();
    }

    //获取categories信息
    public void getData() {
        Intent intent = getIntent();
        String categoriesJson = intent.getStringExtra("categoriesJson");
        String userJson = intent.getStringExtra("userJson");
        String housekeeperJson = intent.getStringExtra("housekeeperJson");

        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        category = gson.fromJson(categoriesJson, Category.class);
        housekeepers = gson.fromJson(housekeeperJson, new TypeToken<List<Housekeeper>>() {
        }.getType());
    }

    public void initView() {
        x.image().bind(imgPhoto, StringUtil.ip + category.getPhoto());
        prodInfoTvDesName.setText(category.getName());
        prodInfoTvPrice.setText(category.getPrices().get(0).getPrice() + "");
        housekeeperNum.setText(housekeepers.size() + "");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.prod_info_cart:
                //转到客服
                MyApplication myApplication = (MyApplication) getApplication();
                User user1 = myApplication.getUser();
                if (user1.getUserId() != 0) {
                    getChatKey(user1);
                } else {
                    Toast.makeText(FuWuItemActivity.this, "未登入", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.prod_info_nowbuy:
                //转到下单页面
                MyApplication myApplication1 = (MyApplication) getApplication();
                User user2 = myApplication1.getUser();
                if (user2.getUserId() != 0) {
                    //跳转到下单页面
                    Intent intent = new Intent(FuWuItemActivity.this, EmergencyPlaceAnOrderActivity.class);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String categoryJson = gson.toJson(category);
                    String userJson = gson.toJson(user2);
                    String housekeepersJson = gson.toJson(housekeepers);
                    intent.putExtra("categoryJson", categoryJson);

                    intent.putExtra("userJson", userJson);
                    intent.putExtra("housekeepersJson", housekeepersJson);

                    startActivity(intent);


                } else {
                    Toast.makeText(this, "未登入", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_prod_list_iv_left:
                finish();
                break;
            case R.id.prod_info_tv_prod_comment:
                pageNo++;
                initDataEvaluate();
                break;

        }

    }

    //初始化评价获取网路数据
    public void initDataEvaluate() {
        String url = StringUtil.ip + "/Yan_EmergencyEvaluate";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        requestParams.addQueryStringParameter("categoryName", category.getName() + "");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Evaluate> evaluateList = gson.fromJson(result, new TypeToken<List<Evaluate>>() {
                        }.getType());
                        if (evaluateList.isEmpty()) {
                            Toast.makeText(FuWuItemActivity.this, "没有更多评论了", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (pageNo == 1) {
                            evaluates.clear();
                        }
                        evaluates.addAll(evaluateList);

                        if (evaluates != null) {

                        }
                        if (evaluateAdapter == null) {
                            evaluateAdapter = new CommonAdapter<Evaluate>(FuWuItemActivity.this, evaluates, R.layout.yan_emergency_evaluate) {
                                @Override
                                public void convert(ViewHolder holder, Evaluate evaluate, int position) {
                                    //控件赋值
                                    initView(holder, evaluate);

                                }

                            };

                            listView.setAdapter(evaluateAdapter);

                        } else {

                            evaluateAdapter.notifyDataSetChanged();
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

    //初始话控件
    public void initView(ViewHolder holder, Evaluate evaluate) {
        TextView userPhone = holder.getView(R.id.guzhu_phone);
        String phone = evaluate.getOrder().getUser().getNumber();
        String uPhone = phone.substring(0, 4) + "*****" + phone.substring(phone.length() - 3, phone.length() - 1);
        userPhone.setText(uPhone);
        TextView evaluateTime = holder.getView(R.id.guzhu_time);
        evaluateTime.setText(evaluate.getTime() + "");
        //星级
        RatingBar ratingBar = holder.getView(R.id.guzhu_rating);
        Log.d("FuWuItemActivity", "initView: " + evaluate.getGrade());
        ratingBar.setNumStars(evaluate.getGrade());
        TextView userContent = holder.getView(R.id.evaluate_string);
        userContent.setText(evaluate.getEvaluate());
    }

    //获取聊天密钥
    public void getChatKey(final User user) {
        String url = StringUtil.ip + "/Yan_getChatKeyServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        requestParams.addQueryStringParameter("userId1", user.getUserId() + "");
        requestParams.addQueryStringParameter("username", user.getName());

        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        String userKey = result.split("token")[1].split("userId")[0].split("\"")[2];
                        Log.d("Main2Activity", "onSuccess: " + userKey);
                        //得到Token
                        String Token = userKey;
                        /**
                         * IMKit SDK调用第二步
                         *
                         * 建立与服务器的连接
                         *
                         */
                        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                //Connect Token 失效的状态处理，需要重新获取 Token
                            }

                            @Override
                            public void onSuccess(String userId) {
                                Log.d("MainActivity", "onSuccess: " + userId);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.d("MainActivity", "onError: " + errorCode);

                            }
                        });
                        //实现客服端
                        if (user.getUserId() == 3) {


//启动会话列表界面
                            if (RongIM.getInstance() != null)
                                RongIM.getInstance().startConversationList(FuWuItemActivity.this);

//启动聚合会话列表界面
/*
                         if (RongIM.getInstance() != null)
                                RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.GROUP);
*/
                        } else {
                            if (RongIM.getInstance() != null)
                            //访问的用户
                            /**
                             * 启动单聊
                             * context - 应用上下文。
                             * targetUserId - 要与之聊天的用户 Id。
                             * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                             */
                                RongIM.getInstance().startPrivateChat(FuWuItemActivity.this, "3", "客服");
                        }

                    }


                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("Main2Activity", "onError: " + ex);
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


}
