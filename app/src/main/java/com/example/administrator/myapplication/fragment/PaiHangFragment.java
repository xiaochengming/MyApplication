package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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


    @InjectView(R.id.tv_degree_of_heat)
    TextView tvDegreeOfHeat;
    @InjectView(R.id.tv_cleaning_service)
    TextView tvCleaningService;
    @InjectView(R.id.tv_appliance_cleaning)
    TextView tvApplianceCleaning;
    @InjectView(R.id.tv_furniture_maintenance)
    TextView tvFurnitureMaintenance;
    @InjectView(R.id.tv_nanny)
    TextView tvNanny;
    @InjectView(R.id.tv_maternity_matron)
    TextView tvMaternityMatron;
    @InjectView(R.id.tv_more)
    TextView tvMore;
    @InjectView(R.id.lv_hang)
    RefreshListView lvHang;
    String hangName=null;
    int pageNo =1;
    int pageSize =7;
    CommonAdapter<Housekeeper> hangAdapter;
    List<Housekeeper> housekeepers=new ArrayList<Housekeeper>();
    List<String> popContents=new ArrayList<String>();
    Handler handler=new Handler();
    View tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (tv!=null){
            ViewGroup v= (ViewGroup) tv.getParent();
            if (v!=null){
                v.removeView(tv);
            } getData(hangName);
            Log.i("PaiHangFragment", "onCreateView lvhang "+lvHang);

            ButterKnife.inject(this, tv);
            lvHang.setOnRefreshUploadChangeListener(this);
            popContents.add("维修服务");
            popContents.add("居家换新");
            popContents.add("深度除螨");
            popContents.add("甲醛检测");
            popContents.add("打蜡服务");
            return tv;
        }

        tv = inflater.inflate(R.layout.fragment_hang_pai, null);
        ButterKnife.inject(this, tv);
        //初始化，hangName=null;
        getData(hangName);
        lvHang.setOnRefreshUploadChangeListener(this);
        popContents.add("维修服务");
        popContents.add("居家换新");
        popContents.add("深度除螨");
        popContents.add("甲醛检测");
        popContents.add("打蜡服务");
        return tv;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //连接服务器，获取数据
    public void getData(String hangName) {
        Log.i("PaiHangFragment", "getData hangName= "+hangName);
        Log.i("PaiHangFragment", "getData  ");
        String url = StringUtil.ip+"/Ranking";
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
                Type type = new TypeToken<List<Housekeeper>>() {}.getType();
                List<Housekeeper> updatehousekeepers=gson.fromJson(result, type);
                housekeepers.clear();
                housekeepers.addAll(updatehousekeepers);
                Log.i("PaiHangFragment", "onSuccess  :"+housekeepers);
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
                            tvCount.setText("服务过"+housekeeper.getServiceTime() + "" + "个家庭");
                            //地址赋值
                            TextView tvAddress = viewHolder.getViewById(R.id.tv_address);
                            tvAddress.setText(housekeeper.getPlaceOfOrigin());
                            //年龄赋值
                            TextView tvAge = viewHolder.getViewById(R.id.tv_ages);
                            tvAge.setText(housekeeper.getAge() + "" + "岁");
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
                            String photoUrl = StringUtil.ip+"/"+ housekeeper.getHousePhoto();
                            ImageView imageView = viewHolder.getViewById(R.id.imageViews);
                            x.image().bind(imageView, photoUrl,imageOptions);
                            //星星数量赋值
                            RatingBar ratingBar=viewHolder.getViewById(R.id.ratingBar);
                            int ratingSize=housekeeper.getServiceplevel();
                            ratingBar.setRating(ratingSize);
                            //按钮点击事件(找控件)
                            TextView tvDetails=viewHolder.getViewById(R.id.but_details);
                            tvDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到介绍界面(传值)
                                    Intent intent=new Intent(getActivity(),IntroduceActivity.class);
                                    //housekeeper对象
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("huosekeeper",housekeeper);
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
        Log.i("PaiHangFragment", "getData hangName= "+hangName);
        Log.i("PaiHangFragment", "getData  ");
        String url = StringUtil.ip+"/Ranking";
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
                Type type = new TypeToken<List<Housekeeper>>() {}.getType();
                List<Housekeeper> updatehousekeepers=gson.fromJson(result, type);
                if (updatehousekeepers.size()==0){
                    pageNo--;
                    Toast.makeText(getActivity(),"已经到底了",Toast.LENGTH_SHORT).show();
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
                            String photoUrl = StringUtil.ip+"/"+ housekeeper.getHousePhoto();
                            ImageView imageView = viewHolder.getViewById(R.id.imageViews);
                            x.image().bind(imageView, photoUrl);
                            //按钮点击事件(找控件)
                            TextView tvDetails=viewHolder.getViewById(R.id.but_details);
                            tvDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到介绍界面(传值)
                                    Intent intent=new Intent(getActivity(),IntroduceActivity.class);
                                    //housekeeper对象
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("huosekeeper",housekeeper);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }
    //创建popupWindow：v（点击的按钮）
    public void initPopupWindow(View v){
        Log.i("PaiHangFragment", "initPopupWindow popContents11: "+popContents);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.lv_more,null);
        final PopupWindow popupWindow=new PopupWindow(view,200,ViewGroup.LayoutParams.WRAP_CONTENT);
        //listview设置数据源
        ListView lv= (ListView) view.findViewById(R.id.lv_more_fenlei);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),R.layout.lv_item_more,R.id.textView_pop,popContents);
        lv.setAdapter(arrayAdapter);
        //popupwiondow外面点击，popupwindow消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //显示在v的下面
        popupWindow.showAsDropDown(v);
        //listview的item点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PaiHangFragment", "initPopupWindow popContents22: "+popContents);
                //关闭popupWindow
                popupWindow.dismiss();
                //排序
                if (position==0){
                    hangName="维修";
                }else if (position==1){
                    hangName="居";
                }else if (position==2){
                    hangName="除螨";
                }else if (position==3){
                    hangName="甲醛";
                }else if (position==4){
                    hangName="打蜡";
                }
                //重新获取数据源，按价格排序
                getData(hangName);
            }
        });
    }
    @OnClick({R.id.tv_degree_of_heat, R.id.tv_cleaning_service, R.id.tv_appliance_cleaning, R.id.tv_furniture_maintenance, R.id.tv_nanny, R.id.tv_maternity_matron,R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_degree_of_heat:
                //热度
                hangName=null;
                getData(hangName);
                break;
            case R.id.tv_cleaning_service:
                //保洁服务
                hangName="保洁";
                Log.i("PaiHangFragment", "onClick  tvCleaningService hangName="+hangName);
                getData(hangName);
                break;
            case R.id.tv_appliance_cleaning:
                //家电清洗
                hangName="清洗";
                Log.i("PaiHangFragment", "onClick  tvCleaningService hangName="+hangName);
                getData(hangName);
                break;
            case R.id.tv_furniture_maintenance:
                //家具保养
                hangName="保养";
                Log.i("PaiHangFragment", "onClick  tvCleaningService hangName="+hangName);
                getData(hangName);
                break;
            case R.id.tv_nanny:
                //保姆
                hangName="保姆";
                Log.i("PaiHangFragment", "onClick  tvCleaningService hangName="+hangName);
                getData(hangName);
                break;
            case R.id.tv_maternity_matron:
                //月嫂
                hangName="月嫂";
                Log.i("PaiHangFragment", "onClick  tvCleaningService hangName="+hangName);
                getData(hangName);
                break;
            case R.id.tv_more:
                //更多
                initPopupWindow(view);
                Log.i("PaiHangFragment", "onClick  more");
                break;
        }
    }

    @Override
    public void onRefresh() {
        //更新数据
        pageNo=1;
        //重新请求服务器
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonRefresh()  ");
                getData(hangName);//停留1秒
                lvHang.completeRefresh();//更新界面
            }
        },1000);

    }

    @Override
    public void onPull() {
//加载数据
        pageNo++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonPull()  ");
                getPullData(hangName);//原来基础上加载数据
            }
        },1000);
    }
}
