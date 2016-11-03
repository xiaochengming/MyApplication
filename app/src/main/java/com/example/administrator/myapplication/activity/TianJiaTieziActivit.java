package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activitymi.ImageListActivity;
import com.example.administrator.myapplication.entity.Post;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @InjectView(R.id.iv_tianjiatupian)
    ImageView ivTianjiatupian;
    @InjectView(R.id.gv_tupian)
    GridView gvTupian;


    List<String> fileList = new ArrayList<>();
    CommonAdapter<String> adapter;

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
        initDate();
    }

    public void initDate() {
//        Intent intent = getIntent();
//        fileList = intent.getStringArrayListExtra("image");
//        if (fileList() != null) {
//            grilviewSetAdapter();
//        }
    }

    public void grilviewSetAdapter() {
        if (adapter == null) {
            adapter = new CommonAdapter<String>(this, fileList, R.layout.imageitem) {
                @Override
                public void convert(ViewHolder viewHolder, String s, int position) {
                    ImageView imageview = viewHolder.getViewById(R.id.iv_show);
                    x.image().bind(imageview, s);
                }
            };
            gvTupian.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.iv_tianjiatupian, R.id.btn_fabiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tianjiatupian:
                //添加图片
                Intent intent = new Intent(this, ImageListActivity.class);
                Log.i("TianJiaTieziActivit", "onClick: 跳转到添加图片");
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_fabiao:
                if (luntanStateEdittextContent.getText().toString().isEmpty() || luntanStateEdittextContent.getText().toString().equals("")) {
                    Toast.makeText(TianJiaTieziActivit.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    fabiao();
                }
                break;
        }
    }

    public void fabiao() {

        MyApplication myApplication = (MyApplication) getApplication();
        //  Post post =new Post(myApplication.getUser(),)
        //int postId, User user, String postContent, Timestamp postTimes
        final Post post = new Post(myApplication.getUser(), luntanStateEdittextContent.getText().toString(), new Timestamp(System.currentTimeMillis()));
        RequestParams requestParams = new RequestParams(StringUtil.ip + "/InsertPostServlet");
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String str = gson.toJson(post);
        requestParams.setMultipart(true);
        requestParams.addBodyParameter("post", str);
        if (fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(fileList.get(i));
                File file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName()+i+".jpg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                requestParams.addBodyParameter("file" + i, file);
            }
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TianJiaTieziActivit", "onError: " + ex);
                Toast.makeText(TianJiaTieziActivit.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        Intent intent = new Intent();
        intent.putExtra("post", post);
        setResult(RESULT_OK, intent);
        finish();
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TianJiaTieziActivit", "onActivityResult: ");
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<String> newlist = new ArrayList<>();
            newlist = data.getStringArrayListExtra("image");
            fileList.clear();
            fileList.addAll(newlist);
            Log.i("TianJiaTieziActivit", "onActivityResult: " + fileList.size());
            if (fileList != null) {
                grilviewSetAdapter();
            }
        }
    }
}
