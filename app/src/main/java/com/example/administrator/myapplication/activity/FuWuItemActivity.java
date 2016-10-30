package com.example.administrator.myapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ViewFlipper;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Evaluate;
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
import io.rong.imlib.model.Conversation;

/**
 * Created by king on 2016/10/19.
 */
public class FuWuItemActivity extends AppCompatActivity implements View.OnClickListener {


    Category category;
    User user;
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
    @InjectView(R.id.id_prod_list_tv)
    TextView idProdListTv;
    @InjectView(R.id.line1)
    LinearLayout line1;
    @InjectView(R.id.flipper_photo)
    ViewFlipper flipperPhoto;
    @InjectView(R.id.prod_info_linearly)
    LinearLayout prodInfoLinearly;
    @InjectView(R.id.prod_info_tv_des)
    TextView prodInfoTvDes;
    @InjectView(R.id.prod_info_tv_des_name)
    TextView prodInfoTvDesName;
    @InjectView(R.id.line2)
    LinearLayout line2;
    @InjectView(R.id.prod_info_tv_price)
    TextView prodInfoTvPrice;
    @InjectView(R.id.prod_info_tv_pnum)
    TextView prodInfoTvPnum;
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
    @InjectView(R.id.prod_info_cart)
    Button prodInfoCart;
    @InjectView(R.id.prod_info_nowbuy)
    Button prodInfoNowbuy;
    @InjectView(R.id.prod_info_bottom)
    RelativeLayout prodInfoBottom;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    ListView listView;
    String categoryName;
    List<Evaluate> evaluates = new ArrayList<>();
    CommonAdapter evaluateAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yan_fuwu_item);
        ButterKnife.inject(this);
        listView = (ListView) findViewById(R.id.lv_user_remark);
        prodInfoCart.setOnClickListener(this);
        prodInfoNowbuy.setOnClickListener(this);

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

        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        category = gson.fromJson(categoriesJson, Category.class);
        user = gson.fromJson(userJson, User.class);

    }

    public void initView() {

        prodInfoTvDesName.setText(category.getName());
        prodInfoTvPrice.setText(category.getPrices().get(0).getPrice() + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prod_info_cart:
                //转到客服
                if (user.getUserId() != 0) {
                    getChatKey();
                } else {
                    Toast.makeText(FuWuItemActivity.this, "未登入", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.prod_info_nowbuy:
                //转到下单页面
                if (user.getUserId() != 0) {
                    Intent intent = new Intent(this, EmergencyPlaceAnOrderActivity.class);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String categoryJson = gson.toJson(category);
                    String userJson = gson.toJson(user);
                    intent.putExtra("categoryJson", categoryJson);
                    intent.putExtra("userJson", userJson);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "未登入", Toast.LENGTH_SHORT).show();
                }

                break;


        }

    }

    //初始化评价获取网路数据
    public void initDataEvaluate() {
        String url = StringUtil.ip + "/Yan_EmergencyEvaluate";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id

        requestParams.addQueryStringParameter("categoryName", category.getName() + "");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("result", "onSuccess: " + result);
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Evaluate> evaluateList = gson.fromJson(result, new TypeToken<List<Evaluate>>() {
                        }.getType());
                        evaluates.clear();
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
                        Log.d("Throwable", "onError: " + ex);
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
        RatingBar ratingBar = holder.getView(R.id.guzhu_rating);
        ratingBar.setNumStars(evaluate.getGrade());
        TextView userContent = holder.getView(R.id.evaluate_string);
        userContent.setText(evaluate.getEvaluate());
    }

    //获取聊天密钥
    public void getChatKey() {
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
                                RongIM.getInstance().startSubConversationList(FuWuItemActivity.this, Conversation.ConversationType.GROUP);
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
