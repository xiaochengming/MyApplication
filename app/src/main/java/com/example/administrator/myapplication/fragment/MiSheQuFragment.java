package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.DongtaiActivity;
import com.example.administrator.myapplication.activity.LoginActivity;
import com.example.administrator.myapplication.activity.TianJiaTieziActivit;
import com.example.administrator.myapplication.activitymi.ShowImageActivity;
import com.example.administrator.myapplication.entity.Post;
import com.example.administrator.myapplication.entityMi.Zan;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.widget.MyGridView;
import com.example.administrator.myapplication.widget.RefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
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
 * Created by Administrator on 2016/10/22.
 */
public class MiSheQuFragment extends Fragment {

    @InjectView(R.id.iv_tianjia)
    ImageView ivTianjia;
    @InjectView(R.id.listView)
    RefreshListView listView;


    public int pageNum = 1;
    private int pagesize = 10;

    public List<Post> list = new ArrayList<Post>();

    public CommonAdapter<Post> adapter;
    CommonAdapter<String> tupianadapter;
    boolean flag = false;//标志刷新是否是加载还是下拉 下拉为true
    MyApplication myApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("MiSheQuFragment", "onCreateView: ");
        View v = inflater.inflate(R.layout.fragment_shequ, null);
        ButterKnife.inject(this, v);
        myApplication = (MyApplication) getActivity().getApplication();
        Log.i("MiSheQuFragment", "onCreateView: " + myApplication.getUser());
        initData();
        initEven();
        return v;
    }

    //初始化事件
    public void initEven() {
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MiSheQuFragment", "onItemClick: ");
            }
        });*/
        listView.setOnRefreshUploadChangeListener(new RefreshListView.OnRefreshUploadChangeListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                pageNum = 1;
                flag = true;
                initData();
            }

            @Override
            public void onPull() {
                //上啦加载
                getdata();


            }
        });
    }

    //初始化数据
    public void initData() {
        RequestParams requestParams = new RequestParams(StringUtil.ip + "/SelectPostservlet");
        requestParams.addQueryStringParameter("pageNum", pageNum + "");
        requestParams.addQueryStringParameter("pageSize", pagesize + "");
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        Log.i("MiSheQuFragment", "initData: userId" + myApplication.getUser().getUserId());
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (flag) {
                    listView.completeRefresh();
                    flag = false;
                }
                Log.i("MiSheQuFragment", "onSuccess: " + result);
                Type type = new TypeToken<List<Post>>() {
                }.getType();
                Gson gson = new Gson();
                if (result != null) {
                    List<Post> listTemp = new ArrayList<Post>();
                    listTemp = gson.fromJson(result, type);
                    list.clear();
                    list.addAll(listTemp);
                }
                if (adapter == null) {
                    adapter = (new CommonAdapter<Post>(getActivity(), list, R.layout.tiezi_list_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, final Post post, final int position) {
                            ImageView touxiang = viewHolder.getViewById(R.id.touxiang);
                            TextView name = viewHolder.getViewById(R.id.luntan_listitem_textView_name);
                            TextView time = viewHolder.getViewById(R.id.luntan_listitem_textView_time);
                            TextView content = viewHolder.getViewById(R.id.luntan_listitem_textView_content);
                            ImageView dianzan = viewHolder.getViewById(R.id.luntan_listitem_textView_dianzan);
                            TextView pinglun = viewHolder.getViewById(R.id.luntan_listitem_textView_pinglun);
                            TextView pinglunnum = viewHolder.getViewById(R.id.luntan_listitem_textView_cishu);
                            TextView zannum = viewHolder.getViewById(R.id.luntan_listitem_textView_geshu);
                            LinearLayout layoutTiezi = viewHolder.getViewById(R.id.layout_tiezhi);
                            MyGridView image = viewHolder.getViewById(R.id.gv_tiezhi);
                            if (post.getUser() != null && post.getUser().getPhoto() != null) {
                                ImageOptions imageOptions = new ImageOptions.Builder().
                                        setLoadingDrawableId(R.mipmap.ic_launcher).
                                        setRadius(DensityUtil.dip2px(30.0f)).build();
                                x.image().bind(touxiang, StringUtil.ip + "/" + post.getUser().getPhoto(), imageOptions);
                                name.setText(post.getUser().getName());
                            }
                            content.setText(post.getPostContent());
                            time.setText(post.getPostTimes() + "");
                            layoutTiezi.setTag(post);
                            image.setTag(position);
                            pinglunnum.setText(post.getPingLunnum() + "");
                            //评论次数
                            pinglunnum.setText(post.getPingLunnum() + "");
                            // 赞个数
                            zannum.setText(post.getNumber() + "");
                            if (post.getiszan()) {
                                dianzan.setImageResource(R.mipmap.good_checked);
                            } else {
                                dianzan.setImageResource(R.mipmap.good);
                            }

                            //贴子点击事件，跳转到帖子详情
                            layoutTiezi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("MiSheQuFragment", "onClick:布局点击 ");

                                    //跳到贴子详情

                                    Intent intent = new Intent(getActivity(), DongtaiActivity.class);
                                    Log.i("MiSheQuFragment", "onClick: 是否赞" + list.get(position).getiszan());
                                    intent.putExtra("post", list.get(position));
                                    startActivity(intent);
                                }
                            });

                            //点赞
                            dianzan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("MiSheQuFragment", "onClick: 点赞");
                                    if (myApplication.getUser().getUserId() == 0) {
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        getActivity().startActivityForResult(intent, 201);
                                        return;
                                    }
                                    boolean flag = false;
                                    if (list.get(position).getiszan()) {

                                    } else {
                                        flag = true;
                                    }
                                    dianzan(v, flag, post);
                                }
                            });

                            //评论点击
                            pinglun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("MiSheQuFragment", "onClick: 点击评论");
                                    //跳到贴子详情

                                    Intent intent = new Intent(getActivity(), DongtaiActivity.class);
                                    Log.i("MiSheQuFragment", "onClick: 是否赞" + list.get(position).getiszan());
                                    intent.putExtra("post", list.get(position));
                                    startActivity(intent);
                                }
                            });
                            // initListItemEven(layoutTiezi,post);

                            //评论图片
//                            if (list.get(position).getImageList().size() != 0) {
                            grilvewXianshitupian(image);
//                            }else {
//                                grilvewXianshitupian(null);
//                            }
                        }
                    });
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("MiSheQuFragment", "onError: " + ex);
                if (flag) {
                    listView.completeRefresh();
                    flag = false;
                }
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
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

    public void grilvewXianshitupian(GridView gridView) {
        final List<String> images = list.get((Integer) gridView.getTag()).getImageList();
//        if (images.size() != 0) {
        gridView.setAdapter(new CommonAdapter<String>(getActivity(), images, R.layout.imageitem) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int position) {
                ImageView imageView = viewHolder.getViewById(R.id.iv_show);
                x.image().bind(imageView, StringUtil.ip + s);
                Log.i("MiSheQuFragment", "convert: " + StringUtil.ip + s);
                imageView.setTag(position);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x = (int) v.getTag();
                        Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                        intent.putStringArrayListExtra("image", (ArrayList<String>) images);
                        intent.putExtra("postion", x);
                        startActivity(intent);
                    }
                });
            }
        });
//        }
    }

    //点赞后改变图标
    public void dianzan(final View v, final boolean flag, final Post post) {
        int a = 0;
        if (flag) {
            post.setNumber(post.getNumber() + 1);
            a = 1;
        } else {
            post.setNumber(post.getNumber() - 1);
            a = 0;
        }
        post.setIszan(flag);
        Zan zan = new Zan(post.getPostId(), myApplication.getUser().getUserId(), a);
        //更新界面
        adapter.notifyDataSetChanged();
        //修改数据库
        RequestParams params = new RequestParams(StringUtil.ip + "/DianzanServlet");
        Gson gson = new Gson();
        String str = gson.toJson(zan);
        params.addQueryStringParameter("zan", str);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


//                if (flag) {
//                    ((ImageView) v).setImageResource(R.mipmap.good_checked);
//
//                } else {
//                    ((ImageView) v).setImageResource(R.mipmap.good);
//
//                }
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

    @OnClick(R.id.iv_tianjia)
    public void onClick() {
        Intent intent = new Intent(getActivity(), TianJiaTieziActivit.class);
        getActivity().startActivityForResult(intent, 100);
    }

    //上啦加载
    public void getdata() {

        RequestParams requestParams = new RequestParams(StringUtil.ip + "/SelectPostservlet");
        pageNum++;
        requestParams.addQueryStringParameter("pageNum", pageNum + "");
        requestParams.addQueryStringParameter("pageSize", pagesize + "");
        requestParams.addQueryStringParameter("userId", myApplication.getUser().getUserId() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("MiSheQuFragment", "onSuccess: getdata" + result);
                listView.completeLoad();
                Type type = new TypeToken<List<Post>>() {
                }.getType();
                Gson gson = new Gson();
                listView.completeLoad();
                List<Post> listTemp = gson.fromJson(result, type);
                if (listTemp.size() == 0) {
                    pageNum--;
                    Toast.makeText(getActivity(), "没有新数据", Toast.LENGTH_SHORT).show();
                } else {
                    int old = list.size();
                    list.addAll(listTemp);

                    if (adapter == null) {

                        //未改可能有bug有复制上面代码即可
                        adapter = (new CommonAdapter<Post>(getActivity(), list, R.layout.tiezi_list_item) {
                            @Override
                            public void convert(ViewHolder viewHolder, final Post post, int position) {
                                Log.i("MiSheQuFragment", "convert: " + position);
                                Log.i("MiSheQuFragment", "convert: " + post);
                                ImageView touxiang = viewHolder.getViewById(R.id.touxiang);
                                TextView name = viewHolder.getViewById(R.id.luntan_listitem_textView_name);
                                TextView time = viewHolder.getViewById(R.id.luntan_listitem_textView_time);
                                TextView content = viewHolder.getViewById(R.id.luntan_listitem_textView_content);
                                ImageView dianzan = viewHolder.getViewById(R.id.luntan_listitem_textView_dianzan);
                                TextView pinglun = viewHolder.getViewById(R.id.luntan_listitem_textView_pinglun);
                                TextView pinglunnum = viewHolder.getViewById(R.id.luntan_listitem_textView_cishu);
                                TextView zannum = viewHolder.getViewById(R.id.luntan_listitem_textView_geshu);
                                LinearLayout layoutTiezi = viewHolder.getViewById(R.id.layout_tiezhi);

                                if (post.getUser() != null && post.getUser().getPhoto() != null) {
                                    ImageOptions imageOptions = new ImageOptions.Builder().
                                            setLoadingDrawableId(R.mipmap.ic_launcher).
                                            setRadius(DensityUtil.dip2px(30.0f)).build();
                                    x.image().bind(touxiang, StringUtil.ip + "/" + post.getUser().getPhoto(), imageOptions);
                                    name.setText(post.getUser().getName());
                                }
                                content.setText(post.getPostContent());
                                time.setText(post.getPostTimes() + "");
                                layoutTiezi.setTag(post);
                                //评论次数
                                pinglunnum.setText(post.getPingLunnum() + "");
                                // 赞个数
                                zannum.setText(post.getNumber());
                                if (post.getiszan()) {
                                    dianzan.setImageResource(R.mipmap.good_checked);
                                } else {
                                    dianzan.setImageResource(R.mipmap.good);
                                }
                                //贴子点击事件，跳转到帖子详情
                                layoutTiezi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.i("MiSheQuFragment", "onClick:布局点击 ");
                                        Post post1 = (Post) v.getTag();
                                        //跳到贴子详情

                                        Intent intent = new Intent(getActivity(), DongtaiActivity.class);
                                        intent.putExtra("post", post1);
                                        startActivity(intent);
                                    }
                                });
                                //评论点击
                                pinglun.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.i("MiSheQuFragment", "onClick: 评论点击");
                                    }
                                });

                                //zan

                                // initListItemEven(layoutTiezi,post);

                                //评论图片

                            }
                        });
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        listView.setSelection(old - 1);
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("MiSheQuFragment", "onError: " + ex);
                listView.completeLoad();
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
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
