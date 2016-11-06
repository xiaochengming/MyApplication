package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activitymi.ShowImageActivity;
import com.example.administrator.myapplication.entity.Dynamic;
import com.example.administrator.myapplication.entity.Post;
import com.example.administrator.myapplication.entityMi.Zan;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.widget.MyGridView;
import com.example.administrator.myapplication.widget.MyRefreshListView;
import com.example.administrator.myapplication.widget.NoScrollListview;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DongtaiActivity extends AppCompatActivity {

    @InjectView(R.id.touxiang)
    ImageView touxiang;
    @InjectView(R.id.luntan_listitem_textView_name)
    TextView luntanListitemTextViewName;
    @InjectView(R.id.luntan_listitem_textView_time)
    TextView luntanListitemTextViewTime;
    @InjectView(R.id.luntan_listitem_textView_content)
    TextView luntanListitemTextViewContent;
    @InjectView(R.id.layout_tiezhi)
    LinearLayout layoutTiezhi;
    @InjectView(R.id.luntan_listitem_layout)
    LinearLayout luntanListitemLayout;
    @InjectView(R.id.lv_tiezi_pinglun)
    MyRefreshListView lvTieziPinglun;
    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;
    @InjectView(R.id.luntan_listitem_textView_pinglun)
    TextView luntanListitemTextViewPinglun;
    @InjectView(R.id.luntan_listitem_textView_cishu)
    TextView pinglunCishu;
    @InjectView(R.id.luntan_listitem_textView_dianzan)
    ImageView ivdianzan;
    @InjectView(R.id.luntan_listitem_textView_geshu)
    TextView dianzanliang;
    @InjectView(R.id.layout_tiezhibuju)
    LinearLayout layoutTiezhibuju;
    @InjectView(R.id.et_fasong_pinglun)
    EditText etFasongPinglun;
    @InjectView(R.id.show_luntan_text_enterpinglun)
    TextView showLuntanTextEnterpinglun;
    @InjectView(R.id.dibubuju)
    LinearLayout dibubuju;


    TextView tvFloor;
    MyGridView image;

    Post post;
    List<Dynamic> list = new ArrayList<Dynamic>();
    List<Dynamic> showlist = new ArrayList<Dynamic>();
    CommonAdapter<Dynamic> adapter;
    MyApplication myApplication;
    InputMethodManager imm;
    int louceng;
    int page;
    int pageSize = 10;
    boolean flag = false;//下拉刷新的标志
    int xuanzhong;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtai);
        ButterKnife.inject(this);
        queue = Volley.newRequestQueue(this);
        tvFloor = (TextView) findViewById(R.id.tv_footer);
        image = (MyGridView) findViewById(R.id.gv_tiezhixiangqing);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etFasongPinglun.getWindowToken(), 0);
        myApplication = (MyApplication) getApplication();
        initTiezi();//初始化帖子数据
        getPinglun();//获取帖子评论 并设置listview
        inittoobar();//初始化toolbar
        initEven();//初始化事件

        tvFloor.setText("暂无更多评论");

    }


    public void initEven() {
        //上拉加载下拉刷新
        lvTieziPinglun.setOnRefreshUploadChangeListener(new MyRefreshListView.OnRefreshUploadChangeListener() {
            @Override
            public void onRefresh() {
                //刷新

                getPinglun();
                flag = true;
            }

            @Override
            public void onPull() {
                //加载
                lvTieziPinglun.completeLoad();
                Toast.makeText(DongtaiActivity.this, "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });
        //评论帖子
        layoutTiezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dibubuju.setVisibility(View.VISIBLE);
                if (myApplication.getUser().getUserId() == 0) {
                    Intent intent = new Intent(DongtaiActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 203);
                    return;
                }
                if (flag) {
                    flag = false;
                    return;
                }
                louceng = 0;
                imm.showSoftInput(etFasongPinglun, InputMethodManager.SHOW_FORCED);
            }
        });

        lvTieziPinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // dibubuju.setVisibility(View.VISIBLE);
                Log.i("DongtaiActivity", "onItemClick: " + position);
            }
        });
    }

    public void inittoobar() {
        tbWorkerlist.setNavigationIcon(R.mipmap.back);
        tbWorkerlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化帖子数据
    public void initTiezi() {
        Intent intent = getIntent();
        if (intent.getParcelableExtra("post") != null) {

            post = intent.getParcelableExtra("post");

            if (post.getUser() != null && post.getUser().getPhoto() != null) {
                ImageOptions imageOptions = new ImageOptions.Builder().
                        setLoadingDrawableId(R.mipmap.ic_launcher).
                        setRadius(DensityUtil.dip2px(30.0f)).build();
                x.image().bind(touxiang, StringUtil.ip + "/" + post.getUser().getPhoto(), imageOptions);
                luntanListitemTextViewName.setText(post.getUser().getName());
            }
            luntanListitemTextViewContent.setText(post.getPostContent());
            luntanListitemTextViewTime.setText(post.getPostTimes() + "");
            grilvewXianshitupian(image);
            gengxintiezhi();
        }
    }

    public void grilvewXianshitupian(GridView gridView) {
        final List<String> images = post.getImageList();
//        if (images.size() != 0) {
        gridView.setAdapter(new CommonAdapter<String>(this, images, R.layout.imageitem) {
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
                        Intent intent = new Intent(DongtaiActivity.this, ShowImageActivity.class);
                        intent.putStringArrayListExtra("image", (ArrayList<String>) images);
                        intent.putExtra("postion", x);
                        startActivity(intent);
                    }
                });
            }
        });
//        }
    }

    public void gengxintiezhi() {
        pinglunCishu.setText(post.getPingLunnum() + "");
        if (post.getiszan()) {
            ivdianzan.setImageResource(R.mipmap.good_checked);
        } else {
            ivdianzan.setImageResource(R.mipmap.good);
        }
        dianzanliang.setText(post.getNumber() + "");
        imm.hideSoftInputFromWindow(etFasongPinglun.getWindowToken(), 0);
    }

    //获取评论数据
    public void getPinglun() {
        RequestParams params = new RequestParams(StringUtil.ip + "/MiSelectPinglun");
        params.addQueryStringParameter("postId", post.getPostId() + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<Dynamic>>() {
                }.getType();
                List<Dynamic> listTemp = gson.fromJson(result, type);

                list.clear();
                list.addAll(listTemp);
                lvTieziPinglun.completeRefresh();
                listViewshezhishipeiqi();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("DongtaiActivity", "onError: " + ex);
                lvTieziPinglun.completeRefresh();
                Toast.makeText(DongtaiActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    ////listview 添加页数据
//    public  void add(){
//        if (0<page*pageSize&&page*pageSize<showlist.size()){
//            showlist.clear();
//            for(int i=0;i<page*)
//        }
//    }
    //listview s设置适配器
    public void listViewshezhishipeiqi() {
        if (adapter == null) {
            lvTieziPinglun.setAdapter(new CommonAdapter<Dynamic>(DongtaiActivity.this, list, R.layout.pinglun_item) {
                @Override
                public void convert(ViewHolder viewHolder, Dynamic dynamic, final int position) {
                    ImageView touxiang = viewHolder.getViewById(R.id.listview_pinglun_item_imageview_icon);
                    TextView name = viewHolder.getViewById(R.id.listview_pinglun_item_textView_name);
                    TextView lou = viewHolder.getViewById(R.id.listview_pinglun_item_textView_lou);
                    TextView pinglun = viewHolder.getViewById(R.id.listview_pinglun_item_textView_location);
                    TextView shijian = viewHolder.getViewById(R.id.listview_pinglun_item_textView_time);
                    TextView huifu = viewHolder.getViewById(R.id.listview_pinglun_item_textView_zan);
                    if (dynamic.getUser() != null && dynamic.getUser().getPhoto() != null) {
                        ImageOptions imageOptions = new ImageOptions.Builder().
                                setLoadingDrawableId(R.mipmap.ic_launcher).
                                setRadius(DensityUtil.dip2px(30.0f)).build();
                        x.image().bind(touxiang, StringUtil.ip + "/" + dynamic.getUser().getPhoto(), imageOptions);
                        name.setText(dynamic.getUser().getName());
                    }
                    huifu.setTag(position);
                    int x = position + 1;
                    int b = findParent(dynamic.getParent());
                    if (b != 0) {
                        lou.setText("第" + x + "楼   回复" + b + "楼");
                    } else {
                        lou.setText("第" + x + "楼");
                    }
                    pinglun.setText(dynamic.getDynamicContent());
                    shijian.setText(dynamic.getDynamicTime() + "");
                    huifu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (flag){
//                                flag=false;
//                                return;x
//                            }
                            if (myApplication.getUser().getUserId() == 0) {
                                Intent intent = new Intent(DongtaiActivity.this, LoginActivity.class);
                                startActivityForResult(intent, 203);
                                return;
                            }
                            int x = (int) v.getTag();


                            imm.showSoftInput(etFasongPinglun, InputMethodManager.SHOW_FORCED);
                            louceng = x + 1;
                        }
                    });
                }
            });
        } else {
            adapter.notifyDataSetChanged();
            lvTieziPinglun.setSelection(xuanzhong);
        }
    }


    @OnClick({R.id.luntan_listitem_textView_dianzan, R.id.show_luntan_text_enterpinglun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.luntan_listitem_textView_dianzan:
                if (myApplication.getUser().getUserId() == 0) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 203);
                    return;
                }
                //点赞
                boolean flag = false;
                if (post.getiszan()) {

                } else {
                    flag = true;
                }
                dianzan(view, flag, post);
                break;
            case R.id.show_luntan_text_enterpinglun:
                //底部发表评论
                if (myApplication.getUser().getUserId() == 0) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 203);
                    return;
                }
                Log.i("DongtaiActivity", "onClick: 评论");
                String string = etFasongPinglun.getText().toString();
                if (string.isEmpty() || string.equals("")) {
                    Toast.makeText(DongtaiActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                Dynamic dynamic = null;
                if (louceng == 0) {
                    //回复帖子
                    dynamic = new Dynamic(0, myApplication.getUser(), post.getPostId(), string, new Timestamp(System.currentTimeMillis()), 0, 0);
                    push(post.getUser().getUserId());
                } else {
                    int x = list.get(louceng - 1).getDynamicId();
                    dynamic = new Dynamic(0, myApplication.getUser(), post.getPostId(), string, new Timestamp(System.currentTimeMillis()), x, 0);
                    push(x);
                }
                //隐藏输入框
                imm.hideSoftInputFromWindow(etFasongPinglun.getWindowToken(), 0);
                //etFasongPinglun.clearComposingText();//清空文本
                etFasongPinglun.setText("");
                // dibubuju.setVisibility(View.GONE);
                //刷新界面和插入数据库
                insertPinglun(dynamic);
                louceng = 0;
                break;
        }
    }

    public void push(int userId) {
        /*
		 * 访问servlet，在服务端进行推送；
		 * 将要推送的用户id传给服务端
		 */
        //ip:192.168.0.101换成自己的,alias=2:换成推送用户的id
        String urlString = StringUtil.ip + "/pushproject/PushServlet?alias=" + userId;
        //创建请求
        StringRequest request = new StringRequest(urlString, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                Log.i("MainAcitivity", "返回消息：" + response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("MainAcitivity", "失败");
            }
        });

        //请求添加到请求队列
        queue.add(request);
    }

    //刷新界面和插入数据库
    public void insertPinglun(Dynamic dynamic) {

        //
        RequestParams params = new RequestParams(StringUtil.ip + "/InsertDynamicServlet");
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String string = gson.toJson(dynamic);
        params.addQueryStringParameter("dyamic", string);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("DongtaiActivity", "onSuccess: ");
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                Type type = new TypeToken<List<Dynamic>>() {
                }.getType();
                List<Dynamic> newlist = gson.fromJson(result, type);
                Log.i("DongtaiActivity", "onSuccess: newlist" + newlist.size());
                //新加入的评论id
                //int a = list.get(list.size() - 1).getDynamicId();
                list.clear();

                post.setPingLunnum(post.getPingLunnum() + 1);
                gengxintiezhi();
                list.addAll(newlist);
                list.remove(list.size() - 1);
                Log.i("DongtaiActivity", "onSuccess:list" + list.size());

                //  int a = 0;
//                int x = 0;
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).getDynamicId() == a) {
//                        x = i;
//                        break;
//                    }
//                }
//                xuanzhong = x;
                listViewshezhishipeiqi();
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
        gengxintiezhi();
//        if (flag) {
//                    ((ImageView) v).setImageResource(R.mipmap.good_checked);
//                    dianzanliang
//
//                } else {
//                    ((ImageView) v).setImageResource(R.mipmap.good);
//
//                }
        //修改数据库
        RequestParams params = new RequestParams(StringUtil.ip + "/DianzanServlet");
        Gson gson = new Gson();
        String str = gson.toJson(zan);
        params.addQueryStringParameter("zan", str);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

    //对list排序
//    public List<Dynamic> sortList(List<Dynamic> list) {
//
//        List<Dynamic> jieguolist = new ArrayList<Dynamic>();
//        for (Dynamic dynamic : list) {
//            if (dynamic.getParent() == 0) {
//
//                jieguolist.add(dynamic);
//                sortSubList(list, dynamic, jieguolist);
//
//            }
//        }
//        return jieguolist;
//    }
//
//    public void sortSubList(List<Dynamic> list, Dynamic dynamic,
//                            List<Dynamic> jieguolist) {
//
//        if (dynamic.getHasNext() == 0) {
//            return;
//        }
//        for (Dynamic dynamic2 : list) {
//            if (dynamic2.getParent() == dynamic.getDynamicId()) {
//                jieguolist.add(dynamic2);
//                sortSubList(list, dynamic2, jieguolist);
//            }
//        }
//
//    }

    //查找父动态楼层
    public int findParent(int dynamicId) {
        if (dynamicId == 0) {
            return 0;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDynamicId() == dynamicId) {
                return i + 1;
            }
        }
        return 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 203 && resultCode == RESULT_OK) {
            huoqutiezhixiangqing();
        }
    }

    public void huoqutiezhixiangqing() {
        RequestParams params = new RequestParams(StringUtil.ip + "/SelectPostByIdServlet");
        params.addQueryStringParameter("postid", post.getPostId() + "");
        params.addQueryStringParameter("userid", myApplication.getUser().getUserId() + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                post = gson.fromJson(result, Post.class);
                gengxintiezhi();
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
