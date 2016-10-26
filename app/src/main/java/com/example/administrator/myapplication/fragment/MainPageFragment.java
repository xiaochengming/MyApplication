package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.ServiceInformationActivity;
import com.example.administrator.myapplication.activity.WorkListActivity;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by  on 2016/9/19.
 */
public class MainPageFragment extends Fragment {


    @InjectView(R.id.vp_firstPage)
    ViewPager vpFirstPage;
    @InjectView(R.id.iv_top1)
    ImageView ivTop1;
    @InjectView(R.id.iv_top2)
    ImageView ivTop2;
    @InjectView(R.id.iv_top3)
    ImageView ivTop3;
    ImageView[] dot = null;
    @InjectView(R.id.layout_top)
    LinearLayout layoutTop;
    @InjectView(R.id.btn_list1)
    Button btnList1;
    @InjectView(R.id.btn_list2)
    Button btnList2;
    @InjectView(R.id.btn_list3)
    Button btnList3;
    @InjectView(R.id.btn_list4)
    Button btnList4;
    @InjectView(R.id.btn_list21)
    Button btnList21;
    @InjectView(R.id.btn_list22)
    Button btnList22;
    @InjectView(R.id.btn_list23)
    Button btnList23;
    @InjectView(R.id.btn_list24)
    Button btnList24;
    @InjectView(R.id.rLayout_1)
    RelativeLayout rLayout1;
    @InjectView(R.id.rLayout_2)
    RelativeLayout rLayout2;
    @InjectView(R.id.rLayout_3)
    RelativeLayout rLayout3;
    private final int AUTO_MSG = 1;
    private final int HANDLE_MSG = AUTO_MSG + 1;
    private static final int PHOTO_CHANGE_TIME = 3000;//定时变量
    private int index = 0;
    private boolean flag;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (vpFirstPage == null) {
                return;
            }
            switch (msg.what) {
                case AUTO_MSG:
                    vpFirstPage.setCurrentItem(index++);//收到消息后设置当前要显示的图片
                    mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
                    break;
                case HANDLE_MSG:
                    mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("MyFragment", "onCreateView: MainPageFragment");

        View v = inflater.inflate(R.layout.activity_home_page, null);
        ButterKnife.inject(this, v);


        LayoutInflater inflater2 = LayoutInflater.from(getActivity());
        View imageView1 = inflater2.inflate(R.layout.home_page_1, null);
        View imageView2 = inflater2.inflate(R.layout.home_page_2, null);
        View imageView3 = inflater2.inflate(R.layout.home_page_3, null);
        List<View> list = new ArrayList<>();
        list.add(imageView1);
        list.add(imageView2);
        list.add(imageView3);
        MyViewPageAdepter mybaseAdapter = new MyViewPageAdepter(list);
        vpFirstPage.setAdapter(mybaseAdapter);

        //延时发送消息实现自动轮播
        mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
        vpFirstPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.i("MainPageFragment", "onPageScrolled: " +position);
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                for (int i = 0; i < dot.length; i++) {
                    dot[i].setEnabled(true);
                }
                int i = position % (dot.length);
                if (i < 0) {
                    i = i + dot.length;
                }
                dot[i].setEnabled(false);
            }

            @Override
            //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    Log.i("MainPageFragment", "onPageScrollStateChanged: 1");
                    mHandler.removeMessages(AUTO_MSG);
                    flag = true;
                }
                if (state == 2) {
                    Log.i("MainPageFragment", "onPageScrollStateChanged: 2");
                    if (flag) {
                        mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
                        flag=false;
                    }
                }
            }
        });
        initDot();
        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    public void initDot() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout layout = layoutTop;
        dot = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            dot[i] = (ImageView) layout.getChildAt(i);
            dot[i].setEnabled(true);
        }
        dot[0].setEnabled(false);
    }

    @OnClick({R.id.btn_list1, R.id.btn_list2, R.id.btn_list3, R.id.btn_list4, R.id.btn_list21, R.id.btn_list22, R.id.btn_list23, R.id.btn_list24, R.id.rLayout_1, R.id.rLayout_2, R.id.rLayout_3})
    public void onClick(View view) {
        Log.i("MainPageFragment", "onClick: 点击");
        switch (view.getId()) {
            case R.id.btn_list1:
                chuangzhi("保洁服务");
                break;
            case R.id.btn_list2:
                chuangzhi("家电清洗");
                break;
            case R.id.btn_list3:
                chuangzhi("家居保养");
                break;
            case R.id.btn_list4:
                chuangzhi("维修服务");
                break;
            case R.id.btn_list21:
                chuangzhi("保姆月嫂");
                break;
            case R.id.btn_list22:
                chuangzhi("搬家服务");
                break;
            case R.id.btn_list23:
                chuangzhi("居家换新");
                break;
            case R.id.btn_list24:
                chuangzhi("保洁服务");
                break;
            case R.id.rLayout_1:
                Log.i("MainPageFragment", "onClick:1 ");
                getNet(String.valueOf(1));
                break;
            case R.id.rLayout_2:
                Log.i("MainPageFragment", "onClick:2 ");
                getNet(String.valueOf(2));
                break;
            case R.id.rLayout_3:
                Log.i("MainPageFragment", "onClick: 3");
                getNet(String.valueOf(3));
                break;
        }
    }

    private void chuangzhi(String string) {
        Intent intent = new Intent(getActivity(), WorkListActivity.class);
        intent.putExtra("postion", string);
        startActivity(intent);
    }

    public void getNet(String id) {
        String str = StringUtil.ip + "/CategoryByidServlet";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("id", id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("MainPageFragment", "onSuccess: ");

                Intent intent = new Intent(getActivity(), ServiceInformationActivity.class);
                intent.putExtra("category", result);
                startActivity(intent);
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


    public class MyViewPageAdepter extends PagerAdapter {
        List<View> imagelist;

        public MyViewPageAdepter(List<View> list) {
            imagelist = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % imagelist.size();
            if (position < 0) {
                position = position + imagelist.size();
            }
            View view = imagelist.get(position);
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            return view;
//            return super.instantiateItem(container, position);
        }
    }

}
