package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Post;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TianJiaTieziActivit extends AppCompatActivity {

    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;
    @InjectView(R.id.luntan_state_edittext_content)
    EditText luntanStateEdittextContent;
    @InjectView(R.id.btn_fabiao)
    Button btnFabiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tian_jia_tiezi);
        ButterKnife.inject(this);
        tbWorkerlist.setNavigationIcon(R.mipmap.back);
        //返回点击事件
        tbWorkerlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick(R.id.btn_fabiao)
    //发表
    public void onClick() {
        if (luntanStateEdittextContent.getText().toString()!=null){
            MyApplication myApplication= (MyApplication) getApplication();
          //  Post post =new Post(myApplication.getUser(),)
         //int postId, User user, String postContent, Timestamp postTimes
            final Post post=new Post(myApplication.getUser(),luntanStateEdittextContent.getText().toString(),new Timestamp(System.currentTimeMillis()));
            RequestParams requestParams=new RequestParams(StringUtil.ip+"/InsertPostServlet");
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String str=gson.toJson(post);
            requestParams.addQueryStringParameter("post",str);
            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Intent intent=new Intent();
                    intent.putExtra("post",post);
                    setResult(RESULT_OK);
                    finish();
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
}
