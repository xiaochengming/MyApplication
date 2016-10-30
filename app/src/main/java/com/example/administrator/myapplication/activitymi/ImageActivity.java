package com.example.administrator.myapplication.activitymi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.TianJiaTieziActivit;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ImageActivity extends AppCompatActivity {

    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;
    @InjectView(R.id.gv_tupianliebiao)
    GridView gvTupianliebiao;
    @InjectView(R.id.iv_queding)
    ImageView ivQueding;


    List<String> imageList;
    CommonAdapter<String> adapter;
    List<String> fileList=new ArrayList<>();
    Map<Integer,Boolean> checkmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);
        initDate();
        initEven();
    }

    public void initDate() {
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("imagelist");
        checkmap=new HashMap<Integer, Boolean>();
        for (int i=0;i<imageList.size();i++){
            checkmap.put(i,false);
        }
    }

    public void initEven() {
        tbWorkerlist.setNavigationIcon(R.mipmap.back);
        //返回点击事件
        tbWorkerlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GrildviewSetAdapter();
    }

    public void GrildviewSetAdapter() {
       if (adapter==null){
          adapter=new CommonAdapter<String>(this,imageList,R.layout.imgsitem) {
              @Override
              public void convert(ViewHolder viewHolder, String s, int position) {
                ImageView tupian=viewHolder.getViewById(R.id.imageView1);
                  CheckBox checkBox=viewHolder.getViewById(R.id.checkBox1);
                  x.image().bind(tupian,s);
                  if (checkmap.get(position)){
                      checkBox.setChecked(true);
                  }else {
                      checkBox.setChecked(false);
                  }
                  checkBox.setTag(position);
                  checkBox.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          int x= (int) v.getTag();
                          if (checkmap.get(x)){
                              checkmap.put(x,false);
                          }else {
                              checkmap.put(x,true);
                          }

                      }
                  });
              }
          };
           gvTupianliebiao.setAdapter(adapter);
       }else {
           adapter.notifyDataSetChanged();
       }

    }

    @OnClick(R.id.iv_queding)
    public void onClick() {
        Set<Map.Entry<Integer, Boolean>> set = checkmap.entrySet();
        for (Map.Entry<Integer, Boolean> m:set
             ) {
            if (m.getValue()==true){
                fileList.add(imageList.get(m.getKey()));
            }
        }
        if (fileList.size()==0){
            Toast.makeText(ImageActivity.this, "您未选择图片", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent=new Intent();
            intent.putStringArrayListExtra("image", (ArrayList<String>) fileList);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
