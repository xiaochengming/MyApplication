package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.DataCleanManager;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SettingActivity extends AppCompatActivity {

    @InjectView(R.id.set_toolbar)
    Toolbar setToolbar;
    @InjectView(R.id.tv_exit_login)
    TextView tvExitLogin;
    MyApplication myApplication;
    @InjectView(R.id.relative_layout_set1)
    RelativeLayout relativeLayoutSet1;
    @InjectView(R.id.relative_layout_set2)
    RelativeLayout relativeLayoutSet2;
    @InjectView(R.id.relative_layout_set3)
    RelativeLayout relativeLayoutSet3;
    @InjectView(R.id.relative_layout_set4)
    RelativeLayout relativeLayoutSet4;
    @InjectView(R.id.relative_layout_set5)
    RelativeLayout relativeLayoutSet5;
    String strHuanCun;
    @InjectView(R.id.tv_huan_cun)
    TextView tvHuanCun;
    @InjectView(R.id.tv_banben_hao)
    TextView tvBanbenHao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        try {
            strHuanCun = DataCleanManager.getTotalCacheSize(getApplicationContext());
            Log.i("SettingActivity", "onCreate strHuanCun :" + strHuanCun);
            if (strHuanCun != null && !strHuanCun.equals("0K")) {
                tvHuanCun.setText(strHuanCun);
            } else {
                tvHuanCun.setText("暂无缓存");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getVersionName()!=null){
            tvBanbenHao.setText(getVersionName());
        }

        myApplication = (MyApplication) getApplication();
        //设置导航图标
        setToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        setToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(setToolbar);
        Log.i("TAG", "onCreate  myApplication.getUser()=" + myApplication.getUser());
        //设置toolBar的导航图标点击事件
        setToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到主界面
                finish();
            }
        });
        //如果传回的Id存在，说明用户已经登录，显示“退出登录”
        if (myApplication.isFlag() == true) {
            //显示退出登录
            tvExitLogin.setVisibility(View.VISIBLE);
            tvExitLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("SettingActivity", "onClick  ");
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    myApplication.setFlag(false);
                    myApplication.setUser(new User(0, null, 0, null, 2, null, null, getDate("0000-00-00"), null));
                    startActivity(intent);
                    System.exit(0);
                    tvExitLogin.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public Date getDate(String dateSte) {
        Date date = new Date(0);
        return date;
    }

    @OnClick({R.id.relative_layout_set1, R.id.relative_layout_set2, R.id.relative_layout_set3, R.id.relative_layout_set4, R.id.relative_layout_set5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_set1:
                //用户协议
                break;
            case R.id.relative_layout_set2:
                //清除缓存
                DataCleanManager.clearAllCache(getApplicationContext());
                Toast.makeText(SettingActivity.this,"清除成功",Toast.LENGTH_SHORT).show();
                tvHuanCun.setText("暂无缓存");
                Log.i("SettingActivity", "onClick  strHuanCun2:" + strHuanCun);
                break;
            case R.id.relative_layout_set3:
                getChatKey();
                break;
            case R.id.relative_layout_set4:
                //检查新版本
                Toast.makeText(SettingActivity.this,"正在获取最新版本...",Toast.LENGTH_SHORT).show();
                if (getVersionName()!=null){
                    tvBanbenHao.setText(getVersionName());
                }
                break;
            case R.id.relative_layout_set5:
                //关于我们
                break;
        }
    }

    private String getVersionName(){
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    //获取聊天密钥
    public void getChatKey() {
        String url = StringUtil.ip + "/Yan_getChatKeyServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getApplication();
        final User user = myApplication.getUser();
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
                                RongIM.getInstance().startConversationList(SettingActivity.this);

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
                                RongIM.getInstance().startPrivateChat(SettingActivity.this, "3", "客服");
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
