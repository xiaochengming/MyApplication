package com.example.administrator.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.myapplication.Adapter.SortAdapter;
import com.example.administrator.myapplication.view.CharacterParser;
import com.example.administrator.myapplication.view.CitySortModel;
import com.example.administrator.myapplication.view.PinyinComparator;
import com.example.administrator.myapplication.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CitySelectActivity extends AppCompatActivity {

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private TextView tvNowCity;
    TextView tv_city_name;
    private GpsStatusReceiver receiver ;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        //开启广播
        Intent startIntent = new Intent(CitySelectActivity.this, MyService.class);
        startService(startIntent);
        receiver = new GpsStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.com.action.service");
        registerReceiver(receiver, filter);

        Log.i("CitySelectActivity", "onCreate: ");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNowCity = (TextView) findViewById(R.id.tv_nowCity);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.backs);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        tvNowCity.setText(intent.getStringExtra("city"));
        Log.i("CitySelectActivity", "initViews: city"+intent.getStringExtra("city"));
        characterParser = CharacterParser.getInstance();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //选择city 点击跳转
                if (position > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("city", ((CitySortModel) adapter.getItem(position - 1)).getName());
                    setResult(RESULT_OK,intent);
                    finish();
//                    Toast.makeText(getApplication(),
//                            ((CitySortModel) adapter.getItem(position - 1)).getName(),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }


    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.item_select_city, null);
        TextView tv_catagory = (TextView) headView.findViewById(R.id.tv_catagory);
        tv_city_name = (TextView) headView.findViewById(R.id.tv_city_name);
        tv_catagory.setText("自动定位");
        //tv_city_name.setText("苏州");
        tv_city_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_city_location), null, null, null);
        tv_city_name.setCompoundDrawablePadding(24);
        return headView;
    }

    //对城市名和城市名首字母初始化和排序
    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭服务
        Intent startIntent = new Intent(CitySelectActivity.this, MyService.class);
        stopService(startIntent);
        //取消注册广播
        unregisterReceiver(receiver);
    }

    public class GpsStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //定位当前城市
            String name=intent.getStringExtra("GpsIsAvailable");
            tv_city_name.setText(name);
        }
    }
}
