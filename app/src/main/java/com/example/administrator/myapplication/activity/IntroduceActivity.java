package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Evaluate;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.entity.ImageTbl;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * 介绍
 * 更改于10、25
 */
public class IntroduceActivity extends AppCompatActivity {

    @InjectView(R.id.introduce_toolbar)
    Toolbar introduceToolbar;
    @InjectView(R.id.iv_introduce)
    ImageView ivIntroduce;
    @InjectView(R.id.tv_in_name)
    TextView tvInName;
    @InjectView(R.id.tv_in_age)
    TextView tvInAge;
    @InjectView(R.id.tv_in_count)
    TextView tvInCount;
    @InjectView(R.id.ratingBar2)
    RatingBar ratingBar2;
    @InjectView(R.id.but_introduce)
    Button butIntroduce;
    @InjectView(R.id.lv_jie_shao)
    ListView lvEvaluate;
    Housekeeper housekeeper;
    int housekeeperId;
    @InjectView(R.id.relative_layout_introduce1)
    RelativeLayout relativeLayoutIntroduce1;
    @InjectView(R.id.text_view_introduct1)
    TextView textViewIntroduct1;
    @InjectView(R.id.relative_layout_introduce2)
    RelativeLayout relativeLayoutIntroduce2;
    @InjectView(R.id.etv_jie_shao)
    ExpandableTextView etvJieShao;
    @InjectView(R.id.lv_jie_shao)
    ListView lvJieShao;
    CommonAdapter<Evaluate> evaluateAdapter;
    int evaluateId;
    ImagesInnerGridViewAdapter imagesInnerGridViewAdapter = null;
    @InjectView(R.id.tv_in_sex)
    TextView tvInSex;
    @InjectView(R.id.tv_in_address)
    TextView tvInAddress;
    @InjectView(R.id.tv_in_phone)
    TextView tvInPhone;
    @InjectView(R.id.tv_you_guan)
    TextView tvYouGuan;
    private final int REQUESTCODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (intent.getParcelableExtra("huosekeeper") != null) {
            housekeeper = (Housekeeper) intent.getParcelableExtra("huosekeeper");
            tvInName.setText(housekeeper.getName());
            tvInAge.setText(housekeeper.getAge() + "" + "岁");
            tvInCount.setText("服务过" + housekeeper.getServiceTime() + "" + "个家庭");
            if (housekeeper.getSex()==0){
                tvInSex.setText("女");
            }else {
                tvInSex.setText("男");
            }
            tvInAddress.setText(housekeeper.getPlaceOfOrigin());//地址
            tvInPhone.setText(housekeeper.getHousePhone());//电话号码
            Log.i("IntroduceActivity", "onCreate tvInPhone: "+housekeeper.getHousePhone());

            //拨打电话
            tvInPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhone();
                }
            });
            ratingBar2.setRating(housekeeper.getServiceplevel());
            //显示介绍详情：
            if (housekeeper.getIntroduce() != null) {
                etvJieShao.setText(housekeeper.getIntroduce());
            } else {
                etvJieShao.setText("现在暂无介绍，正在完善！");
            }
            if (housekeeper.getHousePhoto() != null) {
                //地图赋值
                //头像赋值
                ImageOptions imageOptions = new ImageOptions.Builder()
                        //设置加载过程的图片
                        .setLoadingDrawableId(R.mipmap.ic_launcher)
                        //设置加载失败后的图片
                        .setFailureDrawableId(R.mipmap.ic_launcher)
                        //设置使用圆形图片
                        .setCircular(true)
                        //设置支持gif
                        .setIgnoreGif(true).build();
                String photoUrl = StringUtil.ip + "/" + housekeeper.getHousePhoto();
                Log.i("IntroduceActivity", "onCreate  photoUrl:" + photoUrl);
                x.image().bind(ivIntroduce, photoUrl, imageOptions);
            }
            housekeeperId = housekeeper.getHousekeeperId();
        }
        getDate();
        Log.i("IntroduceActivity", "housekeeperId:  " + housekeeperId);
        //设置导航图标
        introduceToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        introduceToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(introduceToolbar);
        //设置toolBar的导航图标点击事件
        introduceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到"排行"界面
                finish();
            }
        });
    }
    //拨打电话权限
    public void callPhone() {
        String callPhonePer = Manifest.permission.CALL_PHONE;
        String[] permission = new String[]{Manifest.permission.CALL_PHONE};
        //用来适配不同的系统版本
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, callPhonePer);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            //权限没有申请过
            ActivityCompat.requestPermissions(this, permission, REQUESTCODE);
        } else {
            //权限申请通过了
            call();
        }
    }
   //拨打电话
    private void call(){
        Intent intent1=new Intent();
        intent1.setAction(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:"+housekeeper.getHousePhoto()));
        startActivity(intent1);
    }

    //权限申请回调的结果
    //requestCode:请求码;
    //permissions:申请的权限(可能是多个);
    //grantResults:审核的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUESTCODE) {
            //处理拨打电话的权限申请
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    //既可以什么都不做;也可以给用户一些UI提示.
                    Toast.makeText(IntroduceActivity.this, "禁止了拨打电话的权限...", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //访问服务器，获取数据
    public void getDate() {
        String url = StringUtil.ip + "/GetEvaluateServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("housekeeperId", housekeeper.getHousekeeperId() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //访问成功
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<Evaluate>>() {
                }.getType();
                //gson解析成集合
                List<Evaluate> evaluates = gson.fromJson(result, type);
                if (evaluates.size() == 0) {
                    String[] strs = new String[]{"暂无评论。。。"};
                    lvJieShao.setAdapter(new ArrayAdapter<String>(IntroduceActivity.this, R.layout.lv_zanwu_pinglun, R.id.tv_zan_wu, strs));
                } else {
                    //设置listview的adpter
                    if (evaluateAdapter == null) {
                        evaluateAdapter = new CommonAdapter<Evaluate>(IntroduceActivity.this, evaluates, R.layout.ay_ping_lun) {
                            @Override
                            public void convert(ViewHolder viewHolder, Evaluate evaluate, int position) {
                                evaluateId = evaluate.getEvaluate_id();
                                //给控件赋值
                                //用户头像
                                ImageView ivUser = viewHolder.getViewById(R.id.circle_image_view1);
                                ImageOptions imageOptions = new ImageOptions.Builder()
                                        //设置加载过程的图片
                                        .setLoadingDrawableId(R.mipmap.ic_launcher)
                                        //设置加载失败后的图片
                                        .setFailureDrawableId(R.mipmap.ic_launcher)
                                        //设置使用圆形图片
                                        .setCircular(true)
                                        //设置支持gif
                                        .setIgnoreGif(true).build();
                                String photoUrl = StringUtil.ip + "/" + evaluate.getOrder().getUser().getPhoto();
                                x.image().bind(ivUser, photoUrl, imageOptions);
                                //用户姓名赋值
                                TextView tvUser = viewHolder.getViewById(R.id.tv_ping_lun_username);
                                tvUser.setText(evaluate.getOrder().getUser().getName());
                                //评论级数赋值
                                RatingBar ratinBar = viewHolder.getViewById(R.id.ratingBar3);
                                int count = evaluate.getGrade();
                                ratinBar.setRating(count);
                                //评论内容赋值
                                ExpandableTextView etvPingLun = viewHolder.getViewById(R.id.tv_ping_lun_js);
                                etvPingLun.setText(evaluate.getEvaluate(), position);
                                //评论图片赋值
                                GridView mGridView = viewHolder.getViewById(R.id.multi_photo_grid);
                                if (imagesInnerGridViewAdapter == null) {
                                    imagesInnerGridViewAdapter = new ImagesInnerGridViewAdapter(getImageData());
                                    mGridView.setAdapter(imagesInnerGridViewAdapter);
                                } else {
                                    imagesInnerGridViewAdapter.notifyDataSetChanged();
                                }

                                //评论时间赋值
                                TextView tvPingLunTime = viewHolder.getViewById(R.id.tv_ping_lun_time);
                                tvPingLunTime.setText(evaluate.getTime().toString());
                            }
                        };
                        lvJieShao.setAdapter(evaluateAdapter);
                    } else {
                        evaluateAdapter.notifyDataSetChanged();
                    }
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

    //获取网络数据（图片地址）
    private List<String> getImageData() {
        final List<String> datas = new ArrayList<String>();
        RequestParams requestParams = new RequestParams(StringUtil.ip + "/ChaZhaoImageServlet");
        requestParams.addQueryStringParameter("evaluateId", evaluateId + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<ImageTbl>>() {
                }.getType();
                List<ImageTbl> imageTbls = gson.fromJson(result, type);
                Log.i("Main2Activity", "onSuccess imageTbls :" + imageTbls);
                if (imageTbls.size() == 0) {
                    return;
                } else {
                    for (int i = 0; i < imageTbls.size(); i++) {
                        datas.add(StringUtil.ip + imageTbls.get(i).getImageAddress());
                        Log.i("Main2Activity", "onSuccess  datas:" + datas);
                    }
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
        return datas;
    }

    //给gridview设置适配器
    private class ImagesInnerGridViewAdapter extends BaseAdapter {

        private List<String> datas2;

        public ImagesInnerGridViewAdapter(List<String> datas) {
            this.datas2 = datas;
            Log.i("ImagesInner", "ImagesInnerGridViewAdapter  datas:" + datas2);
        }

        @Override
        public int getCount() {
            return datas2.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageView imageView = new ImageView(IntroduceActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            x.image().bind(imageView, datas2.get(position));
            Log.i("ImagesInner", "getView  datas:" + datas2);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(IntroduceActivity.this, SpaceImageDetailActivity.class);
                    intent.putExtra("images", (ArrayList<String>) datas2);
                    intent.putExtra("position", position);
                    int[] location = new int[2];
                    imageView.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", imageView.getWidth());
                    intent.putExtra("height", imageView.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Log.i("ImagesInner", "onClick  :" + datas2.get(position));
                }
            });
            return imageView;
        }

    }

    @OnClick(R.id.but_introduce)
    public void onClick() {
        //跳转到预约界面
        findcategory();
    }

    public void findcategory() {
        RequestParams params = new RequestParams(StringUtil.ip + "/FindcategoryByHousekeeperId");
        params.addQueryStringParameter("id", housekeeperId + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("IntroduceActivity", "onSuccess: " + result);
                Type type = new TypeToken<List<Category>>() {
                }.getType();
                Gson gson = new Gson();
                List<Category> list = gson.fromJson(result, type);

//                if (list.size()==1){
//                    Intent intent= new Intent(IntroduceActivity.this,ServiceInformationActivity.class);
//                    Category category=list.get(0);
//                    Gson gson2=new Gson();
//                    intent.putExtra("category",gson2.toJson(category));
//                    intent.putExtra("hid",housekeeperId);
//                    startActivity(intent);
//                }else { }
                showListDialog(list);

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

    private void showListDialog(final List<Category> list) {

        final String[] fruit = new String[list.size()];
        for (int i = 0; i < fruit.length; i++) {
            fruit[i] = list.get(i).getName();
        }


        new AlertDialog.Builder(this).setTitle("服务类型")
                .setItems(fruit, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(IntroduceActivity.this, ServiceInformationActivity.class);
                        String str = new Gson().toJson(list.get(which));
                        intent.putExtra("category", str);
                        intent.putExtra("hid", housekeeperId);
                        startActivity(intent);
                    }
                }).show();
    }
}
