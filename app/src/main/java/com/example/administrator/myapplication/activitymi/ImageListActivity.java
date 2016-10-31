package com.example.administrator.myapplication.activitymi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.util.CommonAdapter;
import com.example.administrator.myapplication.util.FileTraversal;
import com.example.administrator.myapplication.util.ImageUtil;
import com.example.administrator.myapplication.util.ViewHolder;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImageListActivity extends AppCompatActivity {

    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;
    @InjectView(R.id.lv_tupianliebiao)
    ListView lvTupianliebiao;


    List<FileTraversal> list;
    ImageUtil util;
    CommonAdapter<FileTraversal> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
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
        initEven();
    }

    public void initDate() {
        util = new ImageUtil(this);
        list = util.LocalImgFileList();
    }

    public void initEven() {
        if (adapter == null) {
            adapter = new CommonAdapter<FileTraversal>(this, list, R.layout.imagelist_item) {
                @Override
                public void convert(ViewHolder holder, FileTraversal fileTraversal, int position) {
                    ImageView imageView = holder.getView(R.id.iv_xiaotu);
                    TextView filesname = holder.getView(R.id.tv_filesname);
                    TextView imageNum = holder.getView(R.id.tupian_num);
                    if (fileTraversal.getFilecontent().get(0) != null) {
                        x.image().bind(imageView,fileTraversal.getFilecontent().get(0));
                    }
                    filesname.setText(fileTraversal.getFilename());
                    imageNum.setText("("+fileTraversal.getFilecontent().size()+")");
                }
            };
            lvTupianliebiao.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        lvTupianliebiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //跳转到图片列表显示
                Intent intent=new Intent(ImageListActivity.this,ImageActivity.class);
                intent.putStringArrayListExtra("imagelist", (ArrayList<String>) list.get(position).getFilecontent());
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2&&resultCode==RESULT_OK){
           List<String> list=data.getStringArrayListExtra("image");
            Intent intent=new Intent();
           // Log.i("ImageListActivity", "onActivityResult: "+list.size());
            intent.putStringArrayListExtra("image", (ArrayList<String>) list);
            setResult(RESULT_OK,intent);
        }
        finish();
    }
}
