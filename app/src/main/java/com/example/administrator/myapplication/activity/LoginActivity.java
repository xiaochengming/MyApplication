package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.example.administrator.myapplication.R.id.tv_fast;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.log_toolbar)
    Toolbar logToolbar;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.but_login)
    Button butLogin;
    @InjectView(tv_fast)
    TextView tvFast;
    @InjectView(R.id.tv_forget)
    TextView tvForget;
    String number=null;
    String password=null;
    RequestQueue requestQueue;
    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue= Volley.newRequestQueue(this);
        ButterKnife.inject(this);

        JPushInterface.resumePush(this);
        myApplication= (MyApplication) getApplication();
        //设置导航图标
        logToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        logToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(logToolbar);
        //设置toolBar的导航图标点击事件
        logToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到"我的"界面
                finish();
            }
        });
    }

    @OnClick({R.id.but_login, tv_fast, R.id.tv_forget})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.but_login:
                //get方式提交
                number=etPhone.getText().toString();
                password=etPwd.getText().toString();
                Log.i("LoginActivity", "onClick : "+number+",password"+password);
                if (number.length()!=11||number.length()==0){
                    Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                StringRequest stringRequest = new StringRequest(StringUtil.ip+"/LoginServlet?number="+number+"&password="+password,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                                User user=gson.fromJson(response,User.class);
                                Log.i("TAG", "LoginActivity user"+user);
                                if (user!=null){
                                    /*
                                     * 点击登陆按钮：登陆的时候将用户信息注册到极光服务器
                                     * 代码中的2换成 登陆用户的id
                                      */
                                    //设置别名：2（改为登陆用户的id）
                                    JPushInterface.setAlias(LoginActivity.this, user.getUserId()+"", new TagAliasCallback(){

                                        @Override
                                        public void gotResult(int responseCode, String arg1, Set<String> arg2) {
                                            // TODO Auto-generated method stub
                                            //返回码：0 表示注册成功
                                            Log.i("MainAcitivity", "返回码："+responseCode+",别名："+arg1);
                                            //RequestParams requestParams=new RequestParams()
                                        }
                                    });
                                    //登录成功，跳转到主界面
                                    Intent intent=new Intent();
                                    //赋值
                                    myApplication.setFlag(true);
                                    myApplication.setUser(user);
                                    intent.putExtra("user",user);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                    Log.d("TAG", "LoginActivity id="+user.getUserId()+",LoginActivity number="+user.getNumber());
                                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "LoginActivity"+response);
                                }else {
                                    Toast.makeText(LoginActivity.this,"登录失败，请检查用户名与密码输否正确", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"请检查网络是否连接", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                requestQueue.add(stringRequest);
                break;
            case tv_fast:
                //跳转到快速注册界面
                Intent intent=new Intent(LoginActivity.this,FastRegistrationActivity.class);
                startActivityForResult(intent,1166);
                break;
            case R.id.tv_forget:
                //跳转到密码重置界面
                Intent intent1=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivityForResult(intent1,1177);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==1166){
            finish();
        }else if (resultCode==RESULT_OK&&requestCode==1177){
            finish();
        }
    }
}
