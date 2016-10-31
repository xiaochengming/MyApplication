package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.fragment.ExitLoginFragment;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @InjectView(R.id.set_toolbar)
    Toolbar setToolbar;
    @InjectView(R.id.tv_1)
    TextView tv1;
    @InjectView(R.id.tv_agreement)
    TextView tvAgreement;
    @InjectView(R.id.iv_agreement)
    ImageView ivAgreement;
    @InjectView(R.id.iv_into_agreement)
    ImageView ivIntoAgreement;
    @InjectView(R.id.tv_2)
    TextView tv2;
    @InjectView(R.id.tv_problem)
    TextView tvProblem;
    @InjectView(R.id.iv_problem)
    ImageView ivProblem;
    @InjectView(R.id.iv_into_problem)
    ImageView ivIntoProblem;
    @InjectView(R.id.tv_3)
    TextView tv3;
    @InjectView(R.id.tv_customer)
    TextView tvCustomer;
    @InjectView(R.id.iv_customer)
    ImageView ivCustomer;
    @InjectView(R.id.iv_into_coutomer)
    ImageView ivIntoCoutomer;
    @InjectView(R.id.tv_4)
    TextView tv4;
    @InjectView(R.id.tv_new_version)
    TextView tvNewVersion;
    @InjectView(R.id.iv_new_version)
    ImageView ivNewVersion;
    @InjectView(R.id.iv_into_new_version)
    ImageView ivIntoNewVersion;
    @InjectView(R.id.tv_5)
    TextView tv5;
    @InjectView(R.id.tv_about)
    TextView tvAbout;
    @InjectView(R.id.iv_about)
    ImageView ivAbout;
    @InjectView(R.id.iv_into_about)
    ImageView ivIntoAbout;
    @InjectView(R.id.tv_6)
    TextView tv6;
    @InjectView(R.id.tv_exit_login)
    TextView tvExitLogin;
    Integer setId=null;
    MyApplication myApplication;
    ExitLoginFragment exitLoginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
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
//                    Intent intent=new Intent(SettingActivity.this,MainActivity.class);
//                    setResult(RESULT_OK,intent);
                    myApplication.setFlag(false);
                    //  myApplication.setUser(null);
                    myApplication.setUser(new User(0,null,0,null,2,null,null,getDate("0000-00-00"),null));
                    finish();
                    tvExitLogin.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    public Date getDate(String dateSte) {
        Date date = new Date(0);
        return date;
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            return dateFormat.parse(dateSte);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
    @OnClick({R.id.iv_into_agreement, R.id.iv_into_problem, R.id.iv_into_coutomer, R.id.iv_into_new_version, R.id.iv_into_about})
    public void onClick (View view){
        switch (view.getId()) {
            case R.id.iv_into_agreement:
                break;
            case R.id.iv_into_problem:
                break;
            case R.id.iv_into_coutomer:
                break;
            case R.id.iv_into_new_version:
                break;
            case R.id.iv_into_about:
                break;
            case R.id.tv_exit_login:
                break;
        }
    }
}
