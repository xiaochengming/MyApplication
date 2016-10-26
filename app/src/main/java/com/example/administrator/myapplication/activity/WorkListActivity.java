package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.myapplication.Adapter.MiMybaseAdapter;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Category;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkListActivity extends AppCompatActivity {

    @InjectView(R.id.lv_workerTtpe)
    ListView lvWorkerTtpe;
    @InjectView(R.id.lv_worker_list_right)
    ListView lvWorker;

    @InjectView(R.id.tb_workerlist)
    Toolbar tbWorkerlist;


    List<Category> lis;
    String[] workertype;
    String typew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);
        ButterKnife.inject(this);
        tbWorkerlist.setTitle("");
        tbWorkerlist.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(tbWorkerlist);

        tbWorkerlist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        workertype = getResources().getStringArray(R.array.workerType);//simple_list_item_1
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.work_list, workertype);
        lvWorkerTtpe.setAdapter(arrayAdapter);

        Intent intent = getIntent();

        typew = intent.getStringExtra("postion");
        Log.i("WorkListActivity", "onCreate: " + typew);
        String str = StringUtil.ip + "/CategoryServlet";
        RequestParams params = new RequestParams(str);
        Log.i("ming", "onCreate: " + 11);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ming", "onSuccess: " + result);
                Gson g = new Gson();
                Type type = new TypeToken<List<Category>>() {
                }.getType();
                lis = g.fromJson(result, type);
                workertype = getResources().getStringArray(R.array.workerType);
                Log.i("ming", "onSuccess: " + lis.size());
                MiMybaseAdapter miMybaseAdapter = new MiMybaseAdapter(lis, WorkListActivity.this);
                lvWorker.setAdapter(miMybaseAdapter);

                if (typew.equals(null)) {
                    initLeftItemState(0);//改变第一个item颜色
                } else {
                    if (lis != null) {
                        for (int i = 0; i < lis.size(); i++) {
                            Log.i("ming", "onItemClick: " + typew);
                            if (lis.get(i).getType().equals(typew)) {
                                Log.i("ming", "onItemClick: " + typew + "--" + i);
                                lvWorker.setSelection(i);
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < workertype.length; i++) {
                        if (typew.equals(workertype[i])) {
                            initLeftItemState(i);
                        }
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("ming", "onError: " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("ming", "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.i("ming", "onFinished: ");
            }
        });

        lvWorkerTtpe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lis != null) {
                    for (int i = 0; i < lis.size(); i++) {
                        Log.i("ming", "onItemClick: " + workertype[position]);
                        if (lis.get(i).getType().equals(workertype[position])) {
                            Log.i("ming", "onItemClick: " + position + "--" + i);
                            lvWorker.setSelection(i);

                            break;
                        }
                    }
                }
                initLeftItemState(position);
            }
        });
        //详细服务lv点击事件
        lvWorker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(WorkListActivity.this, ServiceInformationActivity.class);
                Category category = lis.get(position);
                Gson gson = new Gson();
                String str = gson.toJson(category);
                intent.putExtra("category", str);
                startActivity(intent);
            }
        });
        lvWorker.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;
            String oldState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            //scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
            //scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
            //scrollState = SCROLL_STATE_IDLE(0) 停止滚动
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    return;
                }
                if (!(lis.get(firstVisibleItem).getType().equals(oldState))) {
                    oldState = lis.get(firstVisibleItem).getType();
                    for (int i = 0; i < workertype.length; i++) {
                        if (workertype[i].equals(lis.get(firstVisibleItem).getType())) {
                            initLeftItemState(i);
                            Log.i("WorkListActivity", "onItemClick: " + lis.get(firstVisibleItem).getType());
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initLeftItemState(int postion) {
        for (int i = lvWorkerTtpe.getFirstVisiblePosition(); i <= lvWorkerTtpe.getLastVisiblePosition(); i++) {
            lvWorkerTtpe.getChildAt(i).setEnabled(true);
        }
        lvWorkerTtpe.getChildAt(postion).setEnabled(false);
    }
}
