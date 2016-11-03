package com.example.administrator.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.DongtaiActivity;
import com.example.administrator.myapplication.entity.Dynamic;
import com.example.administrator.myapplication.entity.Post;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.widget.NoScrollListview;
import com.example.administrator.myapplication.widget.RefreshListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/10/24.
 */
public class MiHuiFuFragment extends Fragment {
    @InjectView(R.id.lv_huifu)
    RefreshListView lvHuifu;
    @InjectView(R.id.et_fasong_pinglun)
    EditText etFasongPinglun;
    @InjectView(R.id.show_luntan_text_enterpinglun)
    TextView showLuntanTextEnterpinglun;
    @InjectView(R.id.dibubuju)
    LinearLayout dibubuju;

    CommonAdapter<Map<Post, List<Dynamic>>> adapter;
    int page = 1;
    int pageSize = 10;
    boolean flag = false;
    MyApplication myApplication;
    List<Map<Post, List<Dynamic>>> list = new ArrayList<>();
    List<Post> postList;
    List<List<Dynamic>> lists;
    InputMethodManager imm;
    Dynamic parentDyanmic;
    Post beihuifupost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_huifu, null);
        ButterKnife.inject(this, v);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etFasongPinglun.getWindowToken(), 0);
        initDate();
        initEven();
        return v;
    }



    public void initEven() {
        lvHuifu.setOnRefreshUploadChangeListener(new RefreshListView.OnRefreshUploadChangeListener() {
            @Override
            public void onRefresh() {
                //上啦刷新
                page = 1;
                flag = true;
                initDate();
            }

            @Override
            public void onPull() {
                //下拉加载
                page++;
                getDate();
            }
        });
        showLuntanTextEnterpinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beihuifupost == null || parentDyanmic == null) {
                    Toast.makeText(getActivity(), "你没有选择回复对象", Toast.LENGTH_SHORT).show();
                } else {
                    String string = etFasongPinglun.getText().toString();
                    if (string.isEmpty() || string.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Dynamic dynamic = new Dynamic(0, myApplication.getUser(), beihuifupost.getPostId(), string, new Timestamp(System.currentTimeMillis()), parentDyanmic.getDynamicId(), 0);
                        insertPinglun(dynamic);
                        etFasongPinglun.setText("");
                        imm.hideSoftInputFromWindow(etFasongPinglun.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    //刷新界面和插入数据库
    public void insertPinglun(Dynamic dynamic) {

        //
        RequestParams params = new RequestParams(StringUtil.ip + "/InsertDynamicshuaxinServlet");
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String string = gson.toJson(dynamic);
        params.addQueryStringParameter("dyamic", string);
        params.addQueryStringParameter("userId", dynamic.getUser().getUserId() + "");
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("pagesize", pageSize + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("DongtaiActivity", "onSuccess: ");
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<Map<Post, List<Dynamic>>>>() {
                }.getType();
                List<Map<Post, List<Dynamic>>> newList = new ArrayList<Map<Post, List<Dynamic>>>();
                newList = gson.fromJson(result, type);
                list.clear();
                list.addAll(newList);
                listViewSheZhishiperqi();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("DongtaiActivity", "onError: " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void initDate() {
        myApplication = (MyApplication) getActivity().getApplication();
        RequestParams params = new RequestParams(StringUtil.ip + "/SelectHuifu");
        params.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<Map<Post, List<Dynamic>>>>() {
                }.getType();
                List<Map<Post, List<Dynamic>>> newList = new ArrayList<Map<Post, List<Dynamic>>>();
                newList = gson.fromJson(result, type);
                list.clear();
                list.addAll(newList);
                listViewSheZhishiperqi();
                if (flag) {
                    lvHuifu.completeRefresh();
                    flag = false;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("MiHuiFuFragment", "onError: " + ex);
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                if (flag) {
                    lvHuifu.completeRefresh();
                    flag = false;
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void listViewSheZhishiperqi() {
        if (adapter == null) {
            adapter = new CommonAdapter<Map<Post, List<Dynamic>>>(getActivity(), list, R.layout.huifu_item) {
                @Override
                public void convert(ViewHolder viewHolder, Map<Post, List<Dynamic>> postDynamicMap, int position) {
                    ImageView touxiang = viewHolder.getViewById(R.id.listview_pinglun_item_imageview_icon);
                    TextView huifuzhename = viewHolder.getViewById(R.id.listview_pinglun_item_textView_name);
                    TextView huifuzhetime = viewHolder.getViewById(R.id.tv_time);
                    TextView huifuneilong = viewHolder.getViewById(R.id.listview_pinglun_item_textView_location);
                    TextView postname = viewHolder.getViewById(R.id.tv_louzhuName);
                    TextView postcontent = viewHolder.getViewById(R.id.tv_louzhuTiezhiContent);
                    TextView huifu = viewHolder.getViewById(R.id.listview_pinglun_item_textView_zan);
                    NoScrollListview listview = viewHolder.getViewById(R.id.lv_pinglun);
                    LinearLayout tiezineilong = viewHolder.getViewById(R.id.layout_tiezineilong);


                    Set<Map.Entry<Post, List<Dynamic>>> set = postDynamicMap.entrySet();
                    Post post = null;
                    List<Dynamic> list = null;
                    for (Map.Entry<Post, List<Dynamic>> map : set
                            ) {
                        post = map.getKey();
                        list = map.getValue();
                    }
                    final Dynamic dynamic = list.get(list.size() - 1);
                    huifu.setTag(R.id.tv_time, dynamic);
                    huifu.setTag(R.id.tv_louzhuName, post);
                    User user = list.get(list.size() - 1).getUser();
                    ImageOptions imageOptions = new ImageOptions.Builder().
                            setLoadingDrawableId(R.mipmap.ic_launcher).
                            setRadius(DensityUtil.dip2px(30.0f)).build();
                    x.image().bind(touxiang, StringUtil.ip + "/" + user.getPhoto());
                    huifuzhename.setText(user.getName());
                    huifuzhetime.setText(dynamic.getDynamicTime().toString());
                    huifuneilong.setText(dynamic.getDynamicContent());
                    postname.setText(post.getUser().getName());
                    postcontent.setText(post.getPostContent());
                    //帖子内容
                    tiezineilong.setTag(post);
                    tiezineilong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Post p = (Post) v.getTag();
                            Intent intent = new Intent(getActivity(), DongtaiActivity.class);
                            intent.putExtra("post", p);
                            startActivity(intent);
                        }
                    });

                    //回复点击事件
                    huifu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parentDyanmic = (Dynamic) v.getTag(R.id.tv_time);
                            beihuifupost = (Post) v.getTag(R.id.tv_louzhuName);
                            imm.showSoftInput(etFasongPinglun, InputMethodManager.SHOW_FORCED);
                        }
                    });
                    final List<Dynamic> finalList = list;
                    listview.setAdapter(new CommonAdapter<Dynamic>(getActivity(), finalList, R.layout.huifuxiangqingitem) {
                        @Override
                        public void convert(ViewHolder viewHolder, Dynamic dynamic, int position) {
                            TextView huifuname = viewHolder.getViewById(R.id.tv_huifuName);
                            TextView huifuzi = viewHolder.getViewById(R.id.tv_huifuzi);
                            TextView beihuifu = viewHolder.getViewById(R.id.tv_beihuifuName);
                            TextView huifucontext = viewHolder.getViewById(R.id.tv_beihuifuContent);
                            if (position == 0) {
                                huifuzi.setVisibility(View.GONE);
                                beihuifu.setVisibility(View.GONE);
                                huifuname.setText(dynamic.getUser().getName());
                                huifucontext.setText(dynamic.getDynamicContent());
                            } else {
                                huifuname.setText(dynamic.getUser().getName());
                                beihuifu.setText(findparentName(dynamic, finalList));
                                huifucontext.setText(dynamic.getDynamicContent());
                            }
                        }
                    });
                }
            };
            lvHuifu.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    public String findparentName(Dynamic dynamic, List<Dynamic> list) {
        for (Dynamic d : list
                ) {
            if (d.getDynamicId() == dynamic.getParent()) {
                return d.getUser().getName();
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void getDate() {
        myApplication = (MyApplication) getActivity().getApplication();
        RequestParams params = new RequestParams(StringUtil.ip + "/SelectHuifu");
        params.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<Map<Post, List<Dynamic>>>>() {
                }.getType();
                List<Map<Post, List<Dynamic>>> newList = new ArrayList<Map<Post, List<Dynamic>>>();
                newList = gson.fromJson(result, type);
                if (newList.size() == 0) {
                    Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    page--;
                    lvHuifu.completeLoad();
                    return;
                }
                list.clear();
                list.addAll(newList);
                listViewSheZhishiperqi();
                lvHuifu.completeLoad();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("MiHuiFuFragment", "onError: " + ex);
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                lvHuifu.completeLoad();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != getActivity().getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        return getActivity().onTouchEvent(event);
    }
    /*Post post=null;
    Dynamic beihufuDynamic=null;
    Dynamic huFudynamic=null;
    for (Map.Entry<Post, Dynamic> map:set
    ) {
        if ( (map.getKey()).getPostId()!=0){
            post=map.getKey();
            beihufuDynamic=map.getValue();
        }else {
            huFudynamic=map.getValue();
        }
    }
    if (huFudynamic.getUser() != null && huFudynamic.getUser().getPhoto() != null) {
        ImageOptions imageOptions = new ImageOptions.Builder().
                setLoadingDrawableId(R.mipmap.ic_launcher).
                setRadius(DensityUtil.dip2px(30.0f)).build();
        x.image().bind(touxiang, StringUtil.ip + "/" + huFudynamic.getUser().getPhoto(), imageOptions);
        huifuzhename.setText(huFudynamic.getUser().getName());
    }
    huifuzhetime.setText(huFudynamic.getDynamicTime().toString());
    huifuneilong.setText(huFudynamic.getDynamicContent());
    postname.setText(post.getUser().getName());
    postcontent.setText(post.getPostContent());
    beihuifuname.setText(beihufuDynamic.getUser().getName());
    beihuifucontext.setText(beihufuDynamic.getDynamicContent());*/
}
