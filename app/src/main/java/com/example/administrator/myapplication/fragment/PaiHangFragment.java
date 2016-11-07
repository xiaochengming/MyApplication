package com.example.administrator.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.IntroduceActivity;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.widget.RefreshListView;
import com.google.gson.Gson;
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

/**
 * Created by on 2016/9/19.
 */
public class PaiHangFragment extends Fragment implements RefreshListView.OnRefreshUploadChangeListener{

    String hangName = null;
    int pageNo = 1;
    int pageSize = 7;
    CommonAdapter<Housekeeper> hangAdapter;
    List<Housekeeper> housekeepers = new ArrayList<Housekeeper>();
    List<String> parentItemArr=new ArrayList<>();
    Handler handler = new Handler();
    View tv;
    RadioGroup mRadioGroup;
    HorizontalScrollView mHorizontalScrollView;
    RefreshListView lvHang;
    RadioButton rb;
    View viewWei;
    boolean flag=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (tv != null) {
            ViewGroup v = (ViewGroup) tv.getParent();
            if (v != null) {
                v.removeView(tv);
            }
            mRadioGroup = (RadioGroup)tv.findViewById(R.id.radio_group_ph);
            mHorizontalScrollView = (HorizontalScrollView) tv.findViewById(R.id.horizontalscrollview);
            lvHang= (RefreshListView) tv.findViewById(R.id.lv_rlv_ph);
            init();
            //初始化
//            getData(null);

            lvHang.setOnRefreshUploadChangeListener(this);
            Log.i("PaiHangFragment", "onCreateView lvhang " + lvHang);
            return tv;
        }
        tv = inflater.inflate(R.layout.fragment_hang_pai, null);
        mRadioGroup = (RadioGroup)tv.findViewById(R.id.radio_group_ph);
        mHorizontalScrollView = (HorizontalScrollView) tv.findViewById(R.id.horizontalscrollview);
        lvHang= (RefreshListView) tv.findViewById(R.id.lv_rlv_ph);
        //初始化，hangName=null;
        init();
        //初始化
//        getData(null);
        lvHang.setOnRefreshUploadChangeListener(this);
        Log.i("PaiHangFragment", "onCreateView  ");
        return tv;
    }
    //初始化
    public void init(){
        parentItemArr.add("热度");
        parentItemArr.add("保洁服务");
        parentItemArr.add("家电清洗");
        parentItemArr.add("家具保养");
        parentItemArr.add("保姆");
        parentItemArr.add("月嫂");
        parentItemArr.add("维修服务");
        parentItemArr.add("居家换新");
        parentItemArr.add("深度除螨");
        parentItemArr.add("甲醛检测");
        parentItemArr.add("打蜡服务");

        //parentItemArr为商品类别对象集合
        for (int i =0;i<parentItemArr.size();i++){
            //添加radiobutton及设置参数(方便动态加载radiobutton)
            rb  = new RadioButton(getActivity());
            //根据下标获取商品类别对象
            String itemArr = parentItemArr.get(i);
            rb.setText(itemArr);
            rb.setTextSize(14);
            rb.setPadding(0,15,0,15);
            rb.setGravity(Gravity.CENTER);
            //根据需要设置显示初始标签的个数，这里显示4个
            rb.setLayoutParams(new ViewGroup.LayoutParams((int)((getScreenWidth(getActivity()))/3.0), ViewGroup.LayoutParams.FILL_PARENT));
            rb.setBackgroundResource(R.drawable.radiobutton_bg_selector);
            //**原生radiobutton是有小圆点的，要去掉圆点而且最好按以下设置，设置为null的话在4.x.x版本上依然会出现**
            rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            rb.setTextColor(getActivity().getResources().getColorStateList(R.color.check_txt_color));
            //向radiogroup中添加radiobutton
            mRadioGroup.addView(rb);

            //设置初始check对象（第一个索引从0开始）
            ((RadioButton)mRadioGroup.getChildAt(0)).setChecked(true);
            //初始化
            getData(null);
            //监听check对象
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, final int checkedId) {
                    final int RadiobuttonId = group.getCheckedRadioButtonId();
                    //获取radiobutton对象
                    final RadioButton bt = (RadioButton) group.findViewById(RadiobuttonId);
                    //获取单个对象中的位置
                    final int index = group.indexOfChild(bt);
                    //设置滚动位置，可使点击radiobutton时，将该radiobutton移动至第二位置
                    mHorizontalScrollView.smoothScrollTo(bt.getLeft() - (int) (getScreenWidth(getActivity()) / 3.0), 0);

                    Log.i("MainActivity", "onCheckedChanged  3:"+parentItemArr.get(index));
                    if (parentItemArr.get(index).equals("热度")){
                        hangName=null;
                    }else if (parentItemArr.get(index).equals("保洁服务")){
                        hangName="保洁";
                    }else if (parentItemArr.get(index).equals("家电清洗")){
                        hangName="清洗";
                    }else if (parentItemArr.get(index).equals("家具保养")){
                        hangName="保养";
                    }else if (parentItemArr.get(index).equals("保姆")){
                        hangName="保姆";
                    }else if (parentItemArr.get(index).equals("月嫂")){
                        hangName="月嫂";
                    }else if (parentItemArr.get(index).equals("维修服务")){
                        hangName="维修";
                    }else if (parentItemArr.get(index).equals("居家换新")){
                        hangName="居";
                    }else if (parentItemArr.get(index).equals("深度除螨")){
                        hangName="除螨";
                    }else if (parentItemArr.get(index).equals("甲醛检测")){
                        hangName="甲醛";
                    }else if (parentItemArr.get(index).equals("打蜡服务")){
                        hangName="打蜡";
                    }
                    getData(hangName);
                }
            });
        }
    }
    //获得屏幕宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //连接服务器，获取数据
    public void getData(String hangName) {
        Log.i("PaiHangFragment", "getData hangName= " + hangName);
        String url = StringUtil.ip + "/Ranking";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("hangName", hangName);
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        Log.i("PaiHangFragment", "url:"+url+",pageNo:"+pageNo+",pageSize:"+pageSize);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("PaiHangFragment", "onSuccess  " + result);

                //响应成功
                //json转换成List<HousekeeperCategory>
                Gson gson = new Gson();
                Type type = new TypeToken<List<Housekeeper>>() {
                }.getType();
                List<Housekeeper> updatehousekeepers = gson.fromJson(result, type);
                housekeepers.clear();
                housekeepers.addAll(updatehousekeepers);
                if (housekeepers.size()==0){
                    lvHang.removeTail();
                    //解析布局文件加载listview尾部
                    flag=true;
                    lvHang.setTail(getActivity(),flag);
                    flag=false;
                }else {
                    lvHang.removeTail();
                   // flag=false;
                    lvHang.setTail(getActivity(),flag);
                    Log.i("PaiHangFragment", "onSuccess  :" + housekeepers);
                    //设置listview的adpter
                    if (hangAdapter == null) {
                        Log.i("PaiHangFragment", "onSuccess  hangAdapter");
                        hangAdapter = new CommonAdapter<Housekeeper>(getActivity(), housekeepers, R.layout.lv_item) {
                            @Override
                            public void convert(ViewHolder viewHolder, final Housekeeper housekeeper, int position) {
                                Log.i("PaiHangFragment", "convert housekeeper.getName()=" + housekeeper.getName() + "housekeeper.getServiceTime()=" + housekeeper.getServiceTime() + "housekeeper.getPlaceOfOrigin()=" + housekeeper.getPlaceOfOrigin()
                                        + "housekeeper.getAge()=" + housekeeper.getAge());

                                //姓名赋值
                                TextView tvName = viewHolder.getViewById(R.id.tv_names);
                                tvName.setText(housekeeper.getName());
                                //服务次数赋值
                                TextView tvCount = viewHolder.getViewById(R.id.tv_count);
                                tvCount.setText("服务过" + housekeeper.getServiceTime() + "" + "个家庭");
                                //地址赋值
                                TextView tvAddress = viewHolder.getViewById(R.id.tv_address);
                                tvAddress.setText(housekeeper.getPlaceOfOrigin());
                                //年龄赋值
                                TextView tvAge = viewHolder.getViewById(R.id.tv_ages);
                                tvAge.setText(housekeeper.getAge() + "" + "岁");
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
                                Log.i("PaiHangFragment", "convert  photoUrl:" + photoUrl);
                                ImageView imageView = viewHolder.getViewById(R.id.imageViews);
                                x.image().bind(imageView, photoUrl, imageOptions);
                                //星星数量赋值
                                RatingBar ratingBar = viewHolder.getViewById(R.id.ratingBar);
                                int ratingSize = housekeeper.getServiceplevel();
                                ratingBar.setRating(ratingSize);
                                //按钮点击事件(找控件)
                                TextView tvDetails = viewHolder.getViewById(R.id.but_details);
                                tvDetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //跳转到介绍界面(传值)
                                        Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                                        //housekeeper对象
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("huosekeeper", housekeeper);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        };

                        lvHang.setAdapter(hangAdapter);
                    } else {
                        hangAdapter.notifyDataSetChanged();
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

    //连接服务器，获取数据
    public void getPullData(String hangName) {
        Log.i("PaiHangFragment", "getData hangName= " + hangName);
        Log.i("PaiHangFragment", "getData  ");
        String url = StringUtil.ip + "/Ranking";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("hangName", hangName);
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("PaiHangFragment", "onSuccess  " + result);

                //响应成功
                //json转换成List<HousekeeperCategory>
                Gson gson = new Gson();
                Type type = new TypeToken<List<Housekeeper>>() {
                }.getType();
                List<Housekeeper> updatehousekeepers = gson.fromJson(result, type);
                if (updatehousekeepers.size() == 0) {
                    pageNo--;
                    Toast.makeText(getActivity(), "已经到底了", Toast.LENGTH_SHORT).show();
                    lvHang.completeLoad();//加载数据后更新界面
                    return;
                }
                housekeepers.addAll(updatehousekeepers);
                //设置listview的adpter
                if (hangAdapter == null) {
                    Log.i("PaiHangFragment", "onSuccess  hangAdapter");
                    hangAdapter = new CommonAdapter<Housekeeper>(getActivity(), housekeepers, R.layout.lv_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, final Housekeeper housekeeper, int position) {
                            Log.i("PaiHangFragment", "convert housekeeper.getName()=" + housekeeper.getName() + "housekeeper.getServiceTime()=" + housekeeper.getServiceTime() + "housekeeper.getPlaceOfOrigin()=" + housekeeper.getPlaceOfOrigin()
                                    + "housekeeper.getAge()=" + housekeeper.getAge());

                            //姓名赋值
                            TextView tvName = viewHolder.getViewById(R.id.tv_names);
                            tvName.setText(housekeeper.getName());
                            //服务次数赋值
                            TextView tvCount = viewHolder.getViewById(R.id.tv_count);
                            tvCount.setText(housekeeper.getServiceTime() + "" + "次服务");
                            //地址赋值
                            TextView tvAddress = viewHolder.getViewById(R.id.tv_address);
                            tvAddress.setText(housekeeper.getPlaceOfOrigin());
                            //年龄赋值
                            TextView tvAge = viewHolder.getViewById(R.id.tv_ages);
                            tvAge.setText(housekeeper.getAge() + "" + "岁");
                            //头像赋值
                            String photoUrl = StringUtil.ip + "/" + housekeeper.getHousePhoto();
                            ImageView imageView = viewHolder.getViewById(R.id.imageViews);
                            x.image().bind(imageView, photoUrl);
                            //按钮点击事件(找控件)
                            TextView tvDetails = viewHolder.getViewById(R.id.but_details);
                            tvDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到介绍界面(传值)
                                    Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                                    //housekeeper对象
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("huosekeeper", housekeeper);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    lvHang.setAdapter(hangAdapter);
                } else {
                    hangAdapter.notifyDataSetChanged();
                }
                lvHang.completeLoad();//加载数据后更新界面
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
    //刷新
    @Override
    public void onRefresh() {
        Log.i("PaiHangFragment", "onRefresh  ");
        //更新数据
        pageNo = 1;
        //重新请求服务器
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonRefresh()  ");
                getData(hangName);//停留1秒
                lvHang.completeRefresh();//更新界面
            }
        }, 1000);

    }

    //加载数据
    @Override
    public void onPull() {
        Log.i("PaiHangFragment", "onPull  ");
        pageNo++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonPull()  ");
                getPullData(hangName);//原来基础上加载数据
            }
        }, 1000);
    }

}
