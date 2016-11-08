package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.JiageBaseAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.entity.Price;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ServiceInformationActivity extends AppCompatActivity {

    @InjectView(R.id.tv_tbTitle)
    TextView tvTbTitle;
    @InjectView(R.id.tb_title)
    Toolbar tbTitle;
    @InjectView(R.id.lv_yuyue)
    ListView lvYuyue;
    @InjectView(R.id.tv_tishi)
    TextView tvTishi;
    @InjectView(R.id.tv_jiesao)
    TextView tvJiesao;
    @InjectView(R.id.iv_photo)
    ImageView ivPhoto;
    @InjectView(R.id.iv_head)
    ImageView ivHead;

    Category category;
    int Hid;
    @InjectView(R.id.tv_jiage)
    TextView tvJiage;
    @InjectView(R.id.btn_yuyue)
    Button btnYuyue;
    @InjectView(R.id.layout_duojiage)
    RelativeLayout layoutDuojiage;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;


    float jiage;
    @InjectView(R.id.rbtn_1)
    RadioButton rbtn1;
    @InjectView(R.id.rbtn_2)
    RadioButton rbtn2;
    @InjectView(R.id.rbtn_3)
    RadioButton rbtn3;
    @InjectView(R.id.rbtn_4)
    RadioButton rbtn4;
    @InjectView(R.id.rg_xuanze)
    RadioGroup rgXuanze;
    RadioButton[] radioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_information);
        ButterKnife.inject(this);
        radioButtons = new RadioButton[]{rbtn1, rbtn2, rbtn3, rbtn4};
        tbTitle.setTitle("");
        tbTitle.setNavigationIcon(R.mipmap.backs);
        setSupportActionBar(tbTitle);
        //返回
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra("category");
            if (intent.getIntExtra("hid", 0) != 0) {
                Hid = intent.getIntExtra("hid", 0);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Category>() {
            }.getType();
            category = gson.fromJson(str, type);
            Log.i("Service", "onCreate: " + category);
            if (category.getName() != null) {
                tvTbTitle.setText(category.getName());
            }
            if (category.getPhoto() != null) {
                Log.i("Service", "onCreate: " + "getphoto");
                String url = StringUtil.ip + category.getPhoto();
                ImageOptions imageOptions = new ImageOptions.Builder()
                        .setSize(DensityUtil.dip2px(385), DensityUtil.dip2px(200))
                        .setUseMemCache(true).build();

                Log.i("Service", "onCreate: " + url);
                x.image().bind(ivPhoto, url, imageOptions);
            }
            if (category.getPrompt() != null) {
                tvTishi.setText(category.getPrompt());
            }
            if (category.getProfile() != null) {
                tvJiesao.setText(category.getProfile());
            }
            if (category.getPrices() != null) {
                if (category.getPrices().size() > 1) {
                    lvYuyue.setVisibility(View.GONE);
                    rgXuanze.setVisibility(View.VISIBLE);
                    layoutDuojiage.setVisibility(View.VISIBLE);
                    even();
                } else {
                    List<Price> list = category.getPrices();
                    Log.i("Service", "onCreate: " + list.size());
                    JiageBaseAdapter jiage = new JiageBaseAdapter(list, this);
                    lvYuyue.setAdapter(jiage);
                    fixListViewHeight(lvYuyue);
                    lvYuyue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //跳转到预约界面
                            Log.i("Service", "onItemClick: 跳转到预约");
                            if (category.getPrices().get(0).getUnit().equals("/平米")) {
                                Intent intent1 = new Intent(ServiceInformationActivity.this, Order2Activity.class);
                                intent1.putExtra("category", category);
                                intent1.putExtra("price", position);
                                if (Hid != 0) {
                                    intent1.putExtra("hid", Hid);
                                }
                                startActivity(intent1);
                            } else {
                                Intent intent1 = new Intent(ServiceInformationActivity.this, OrderActivity.class);
                                intent1.putExtra("category", category);
                                intent1.putExtra("price", position);
                                if (Hid != 0) {
                                    intent1.putExtra("hid", Hid);
                                }
                                startActivity(intent1);
                            }
                        }
                    });
                }
                //jiage.notifyDataSetChanged();
            }

        }
    }

    public void even() {
        for (int i = 0; i < radioButtons.length; i++) {
            if (i >= category.getPrices().size()) {
                radioButtons[i].setVisibility(View.INVISIBLE);
            } else {
                Log.i("Service", "even: " + category.getPrices().get(i).getSubname());
                radioButtons[i].setText(category.getPrices().get(i).getSubname());
            }
        }
        rgXuanze.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case (R.id.rbtn_1):
                        jiage = category.getPrices().get(0).getPrice();
                        tvJiage.setText("￥" + jiage + category.getPrices().get(0).getUnit());
                        break;
                    case (R.id.rbtn_2):
                        jiage = category.getPrices().get(1).getPrice();
                        tvJiage.setText("￥" + jiage + category.getPrices().get(0).getUnit());
                        break;
                    case (R.id.rbtn_3):
                        jiage = category.getPrices().get(2).getPrice();
                        tvJiage.setText("￥" + jiage + category.getPrices().get(0).getUnit());
                        break;
                    case (R.id.rbtn_4):
                        jiage = category.getPrices().get(3).getPrice();
                        tvJiage.setText("￥" + jiage + category.getPrices().get(0).getUnit());
                        break;
                }
            }
        });
        rbtn1.setChecked(true);
        btnYuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ServiceInformationActivity.this, OrderActivity.class);
                intent1.putExtra("category", category);
                int i = 0;
                for (; i < category.getPrices().size(); i++) {
                    if (category.getPrices().get(i).getPrice() == jiage) {
                        break;
                    }
                }
                intent1.putExtra("price", i);
                if (Hid != 0) {
                    intent1.putExtra("hid", Hid);
                }
                startActivity(intent1);
            }
        });
    }

    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        listView.setLayoutParams(params);
    }

    @OnClick(R.id.iv_head)
    public void onClick() {
        showShare();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare share = new OnekeyShare();
        share.disableSSOWhenAuthorize();
        share.setText(category.getSlogan());
        // text是分享文本，所有平台都需要这个字段
        share.setTitle("家政服务");
        // url仅在微信（包括好友和朋友圈）中使用
        share.setUrl("http://sweetystory.com/");
        share.setTitleUrl("http://sweetystory.com/");
        share.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");

        share.show(this);


    }
}
