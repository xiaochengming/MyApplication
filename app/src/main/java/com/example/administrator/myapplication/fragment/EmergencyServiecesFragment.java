package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.EmergencyPlaceAnOrderActivity;
import com.example.administrator.myapplication.activity.FuWuItemActivity;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Housekeeper;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.CommonAdapter;
import com.example.administrator.myapplication.util.RefreshListView;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.UrlAddress;
import com.example.administrator.myapplication.util.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by king on 2016/10/12.
 */
//应急服务
public class EmergencyServiecesFragment extends Fragment implements RefreshListView.OnRefreshUploadChangeListener {
    int pageNo = 1;// 页号
    int pageSize = 5;// 页大小
    CommonAdapter<Category> categoryAdapter;
    Handler handler = new Handler();
    List<Category> categories = new ArrayList<Category>();//应急服务类型
    RefreshListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_list, null);
        listView = (RefreshListView) view.findViewById(R.id.lv_workerList);
        //获取数据初始化页面
        initData();
        itemListener();
        listView.setOnRefreshUploadChangeListener(this);
        return view;
    }

    public void initData() {

        RequestParams requestParams = new RequestParams(StringUtil.ip + "/AllEmergencyServiceServlet");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        //应急服务类型
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        List<Category> cat = gson.fromJson(result, new TypeToken<List<Category>>() {
                        }.getType());
                        categories.clear();
                        categories.addAll(cat);

                        if (categoryAdapter == null) {
                            categoryAdapter = new CommonAdapter<Category>(getActivity(), categories, R.layout.prod_list_item) {
                                @Override
                                public void convert(ViewHolder holder, Category category, int position) {
                                    //控件赋值

                                    initView(holder, category, position);
                                }

                            };
                            listView.setAdapter(categoryAdapter);
                        } else {
                            categoryAdapter.notifyDataSetChanged();
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
                }

        );


    }

    //控件初始化
    public void initView(ViewHolder holder, Category category, int position) {
        TextView categoryName = holder.getView(R.id.tv_name);
        categoryName.setText(category.getName());
        TextView categoryPrice = holder.getView(R.id.tv_price);
        categoryPrice.setText(category.getPrices().get(0).getPrice() + "");
        TextView prompt = holder.getView(R.id.tv_reminder);
        prompt.setText(category.getPrompt() + "");
        TextView profile = holder.getView(R.id.introduction);
        profile.setText(category.getProfile());
        ImageView imageView = holder.getView(R.id.img_photo);
        if (category != null) {
            x.image().bind(imageView, StringUtil.ip + category.getIcon());
        }


    }

    //item事件
    public void itemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("EmergencyServieces", "onItemClick  ");
                if (listView.isFlag() == false && i != categories.size() + 1) {
                    Log.i("EmergencyServieces", "onItemClick  ");
                    getHousekeeper(categories.get(i - 1), i);

                }

            }
        });
    }

    @Override
    public void onRefresh() {
        //刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //bug
                pageNo = 1;
                initData();
                listView.completeRefresh();//刷新数据后，改变界面
            }
        }, 1000);


    }

    @Override
    public void onPull() {
        pageNo++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //bug
                topPullLoading();
                listView.completeLoad();
            }
        }, 1000);


    }

    public void topPullLoading() {
        String url = StringUtil.ip + "/AllEmergencyServiceServlet";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //把传输过来的json对象转换成UserText对象
                        List<Category> categorieList = gson.fromJson(result, new TypeToken<List<Category>>() {
                        }.getType());

                        if (categorieList.size() == 0) {
                            Toast.makeText(getActivity(), "没有新的数据", Toast.LENGTH_SHORT).show();
                            pageNo--;
                            return;
                        }

                        categories.addAll(categorieList);
                        if (categoryAdapter == null) {
                            categoryAdapter = new CommonAdapter<Category>(getActivity(), categories, R.layout.yan_fuwu_allorder) {
                                @Override
                                public void convert(ViewHolder holder, Category category, int position) {
                                    //控件赋值

                                    initView(holder, category, position);
                                }

                            };
                            listView.setAdapter(categoryAdapter);
                        } else {
                            categoryAdapter.notifyDataSetChanged();
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

                    @Override
                    public boolean onCache(String result) {
                        return false;
                    }
                }

        );
    }

    //获取应急服务对应的保姆
    public void getHousekeeper(Category category, final int i) {
        String url = StringUtil.ip + "/Yan_HousekeeperCategory";
        RequestParams requestParams = new RequestParams(url);
        //发送用户id
        requestParams.addQueryStringParameter("categoryId", category.getCategoryId() + "");
        Log.d("getHousekeeper", "getHousekeeper: " + category.getCategoryId());
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        MyApplication myApplication = (MyApplication) getActivity().getApplication();
                        User user = myApplication.getUser();
                        String categoriesJson = gson.toJson(categories.get(i - 1));
                        String userJson = gson.toJson(user);
                        Intent intent = new Intent(getActivity(), FuWuItemActivity.class);
                        intent.putExtra("housekeeperJson", result);
                        intent.putExtra("categoriesJson", categoriesJson);
                        intent.putExtra("userJson", userJson);
                        startActivity(intent);
                    }


                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(getActivity(), "请检查是否连接网络", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }

                    @Override
                    public boolean onCache(String result) {
                        return false;
                    }
                }

        );
    }

}
