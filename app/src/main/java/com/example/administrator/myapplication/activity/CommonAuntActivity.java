package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.queryEntity.QueryHouserkeeperSize;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 常用阿姨
 */
public class CommonAuntActivity extends AppCompatActivity {

    @InjectView(R.id.common_aunt_toolbar)
    Toolbar commonAuntToolbar;
    @InjectView(R.id.lv_common_aunt)
    ListView lvCommonAunt;
    MyApplication myApplication;
    CommonAdapter<QueryHouserkeeperSize> queryhouserkeeperAdapter=null;
    ImageView imageView;
    String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("CommonAuntActivity", "onCreate  ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_aunt);
        ButterKnife.inject(this);
        myApplication= (MyApplication) getApplication();
        //初始化
        getDate();
        //设置导航图标
        commonAuntToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        commonAuntToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(commonAuntToolbar);
        //设置toolBar的导航图标点击事件
        commonAuntToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回首页
                finish();
            }
        });
    }
    //连接服务器
    public void getDate(){
        Log.i("CommonAuntActivity", "getDate :"+ StringUtil.ip+"/CommonAuntServlet");
        RequestParams requestParams=new RequestParams(StringUtil.ip+"/CommonAuntServlet");
        requestParams.addQueryStringParameter("userId",myApplication.getUser().getUserId()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("CommonAuntActivity", "onSuccess result:"+result);
                //获得服务器传过来的值
                Gson gson=new Gson();
                Type type=new TypeToken<List<QueryHouserkeeperSize>>(){}.getType();
                final List<QueryHouserkeeperSize> queryHouserkeeperSizes=gson.fromJson(result,type);
                if (queryHouserkeeperSizes.size()==0){
                    ArrayAdapter arrayAdapter=new ArrayAdapter(CommonAuntActivity.this,R.layout.lh_chushi_changyong_ay);
                    lvCommonAunt.setAdapter(arrayAdapter);
                }
                Log.i("CommonAuntActivity", "queryHouserkeeperSizes: "+queryHouserkeeperSizes);
                //给listview设置数据源
                if (queryhouserkeeperAdapter==null){
                    queryhouserkeeperAdapter=new CommonAdapter<QueryHouserkeeperSize>(CommonAuntActivity.this,queryHouserkeeperSizes,R.layout.lh_chang_yong_ay) {
                        @Override
                        public void convert(ViewHolder viewHolder, QueryHouserkeeperSize queryHouserkeeperSize, int position) {
                            Log.i("CommonAuntActivity", "convert  ");
                            //解析控件并赋值
                            //姓名赋值
                            TextView tvName=viewHolder.getViewById(R.id.tv_chang_yong_name);
                            tvName.setText(queryHouserkeeperSize.getHousekeeper().getName());
                            //服务次数赋值
                            TextView tvCount=viewHolder.getViewById(R.id.tv_chang_yong_count);
                            tvCount.setText("服务过"+queryHouserkeeperSize.getHousekeeper().getServiceTime()+""+"个家庭");
                            //星级赋值
                            RatingBar rb=viewHolder.getViewById(R.id.lh_rb);
                            int ratingSize=queryHouserkeeperSize.getHousekeeper().getServiceplevel();
                            rb.setRating(ratingSize);
                            //年龄赋值
                            TextView tvAge=viewHolder.getViewById(R.id.tv_chang_yong_age);
                            tvAge.setText(queryHouserkeeperSize.getHousekeeper().getAge()+""+"岁");
                            //地址赋值
                            TextView tvAddress=viewHolder.getViewById(R.id.tv_chang_yong_address);
                            tvAddress.setText(queryHouserkeeperSize.getHousekeeper().getPlaceOfOrigin());
                            //此用户预约次数赋值
                            TextView tvSize=viewHolder.getViewById(R.id.tv_chang_yong_size);
                            tvSize.setText("预约过"+queryHouserkeeperSize.getCount()+""+"次");
                            //用户头像赋值
                            imageView=viewHolder.getViewById(R.id.iv_chang_yong);
                            //头像赋值
                            ImageOptions imageOptions=new ImageOptions.Builder()
                                    //设置加载过程的图片
                                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                                    //设置加载失败后的图片
                                    .setFailureDrawableId(R.mipmap.ic_launcher)
                                    //设置使用圆形图片
                                    .setCircular(true)
                                    //设置支持gif
                                    .setIgnoreGif(true).build();
                            photoUrl =StringUtil.ip+queryHouserkeeperSize.getHousekeeper().getHousePhoto();
                            Log.i("CommonAuntActivity", "convert : "+photoUrl);
                            x.image().bind(imageView,photoUrl,imageOptions);

                        }
                    };
                    lvCommonAunt.setAdapter(queryhouserkeeperAdapter);
                    //listview的item点击事件
                    lvCommonAunt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //跳转到介绍界面(传值)
                            Intent intent=new Intent(CommonAuntActivity.this,IntroduceActivity.class);
                            //housekeeper对象
                            Bundle bundle=new Bundle();
                            bundle.putParcelable("huosekeeper",queryHouserkeeperSizes.get(position).getHousekeeper());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }else {
                    queryhouserkeeperAdapter.notifyDataSetChanged();
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
        });
    }
}
