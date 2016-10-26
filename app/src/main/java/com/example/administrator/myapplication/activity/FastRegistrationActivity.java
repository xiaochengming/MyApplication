package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.fragment.RemindFragment;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * 注册
 */

public class FastRegistrationActivity extends AppCompatActivity {

    @InjectView(R.id.fast_toolbar)
    Toolbar fastToolbar;
    @InjectView(R.id.tv_register1)
    TextView tvRegister1;
    @InjectView(R.id.et_registration_phone)
    EditText etRegistrationPhone;
    @InjectView(R.id.but_verification)
    Button button;
    @InjectView(R.id.et_code)
    EditText etCode;
    @InjectView(R.id.tv_receive)
    TextView tvReceive;
    @InjectView(R.id.et_registration_pwd)
    EditText etRegistrationPwd;
    @InjectView(R.id.but_register)
    Button butRegister;
    @InjectView(R.id.tv_register2)
    TextView tvRegister2;
    TimeCount time;
    String number;
    String password;
    String registrationCode;
    RequestQueue requestQueue;
    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_registration);
        //进行BombSMS的初始化
        BmobSMS.initialize(FastRegistrationActivity.this,"51e850757b71c93c966ce53ae22b0794");
        ButterKnife.inject(this);
        myApplication= (MyApplication) getApplication();
        requestQueue= Volley.newRequestQueue(this);
        //设置时间
        time=new TimeCount(60000,1000);
        //设置导航图标
        fastToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        fastToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(fastToolbar);
        //设置toolBar的导航图标点击事件
        fastToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到登录界面
                finish();
            }
        });
    }

    @OnClick({R.id.but_verification, R.id.tv_receive, R.id.but_register})
    public void onClick(View view) {
        number=etRegistrationPhone.getText().toString(); //获取手机号码输入框的值
        password=etRegistrationPwd.getText().toString();//获取密码输入框的值
        registrationCode=etCode.getText().toString();//获取验证码输入框的值
        switch (view.getId()) {
            case R.id.but_verification:
                if (number.length()!=11){
                    Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }else {
                    //获取验证码点击事件（发送请求，短息收到验证码）
                    BmobSMS.requestSMSCode(FastRegistrationActivity.this, "13024525917", "登录与注册功能", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null){
                                //发送成功
                                //按钮显示倒计时60秒
                                Toast.makeText(FastRegistrationActivity.this,"验证码发送成功，请尽快使用",Toast.LENGTH_SHORT).show();
                                time.start();
                            }else {
                                Toast.makeText(FastRegistrationActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;
            case R.id.tv_receive:
                //没有收到验证码点击事件（请求客服发送验证码）
                //开启事务
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                RemindFragment remindFragment=new RemindFragment();
                //把fragment添加到布局文件中
                fragmentTransaction.replace(R.id.fast_frame_layout,remindFragment);
                //提交事务
                fragmentTransaction.commit();
                break;
            case R.id.but_register:
                //注册按钮事件
                if (number.length()==0||registrationCode.length()==0||registrationCode.length()!=6||number.length()!=11){
                    Toast.makeText(this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                }else {
                    BmobSMS.verifySmsCode(FastRegistrationActivity.this, number, registrationCode, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(FastRegistrationActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                //注册成功，数据保存并且跳转到"我的"界面，显示已经登录
                                getDate();
                            }else {
                                Toast.makeText(FastRegistrationActivity.this,"验证码错误，请重新输入",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
    private void getDate(){
        //get方式提交
        Log.i("pwd",password+","+number);
        StringRequest stringRequest = new StringRequest(StringUtil.ip+"/Register?number="+number+"&password="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson=new Gson();
                        User user=gson.fromJson(response,User.class);
                        Log.d("TAG", "FastRegistrationActivity user="+user);
                        if (user!=null){
                            //注册成功，跳转到主界面
                            Intent intent=new Intent();
                            //赋值
                            myApplication.setFlag(true);
                            myApplication.setUser(user);
                            intent.putExtra("user",user);
                            setResult(RESULT_CANCELED,intent);
                            finish();
                            Log.d("TAG", "FastRegistrationActivity id="+user.getUserId()+",FastRegistrationActivity number="+user.getNumber());
                            Toast.makeText(FastRegistrationActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "FastRegistrationActivity"+response);
                        }else {
                            Toast.makeText(FastRegistrationActivity.this,"注册失败，请检查电话号码是否正确", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FastRegistrationActivity.this,"服务器连接失败", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);
    }
    //设置TimeCount继承CountDownTimer（时间计数）
    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //设置点击后按钮背景颜色
            // button.setBackgroundColor(Color.parseColor("#B6B6D8"));
            //设置点击后60秒内按钮不能点击
            button.setClickable(false);
            //设置倒计时间
            button.setText(+millisUntilFinished/1000+"s");
        }
        @Override
        public void onFinish() {
            //倒计时结束后可以点击按钮
            button.setText("获取验证码");
            button.setClickable(true);
        }
    }
}
