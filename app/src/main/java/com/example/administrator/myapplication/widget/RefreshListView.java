package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luhai on 2016/10/15.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    View headView;//头部的view
    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView tvRefreshState;
    private TextView tvRefreshTime;
    private int hendHight;
    public static final int INIT=0;//初始状态（头部不显示）
    public static final int PREPAREREFRESHING=1;//准备刷新（头部全部显示）
    public static final int ISREFRESHING=2;//正在刷新
    private float downY;
    private float moveY;
    int firstVisibleItem;
    int  headState;//头部的状态
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    OnRefreshUploadChangeListener onRefreshUploadChangeListener;
    private View tailView;//尾部的view
    private ProgressBar pbFooter;
    private TextView tvFooter;
    boolean flag=true;//有item点击事件
    private boolean isLoading=false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        initHead(context);
        initTail(context);
        initAnimation(context);
        this.setOnScrollListener(this);
    }


    //初始化头部
    public void initHead(Context context){
        //解析布局文件
        headView= LayoutInflater.from(context).inflate(R.layout.refresh_head,null);
        //初始化头部控件
        imageView= (ImageView) headView.findViewById(R.id.iv_refresher);
        progressBar=(ProgressBar) headView.findViewById(R.id.pb_refresher);
        tvRefreshState = (TextView) headView.findViewById(R.id.tv_refreshertext);
        tvRefreshTime = (TextView) headView.findViewById(R.id.tv_refreshtime);
        //获取head的高度
        headView.measure(0,0);
        hendHight=headView.getMeasuredHeight();
        headView.setPadding(0,-hendHight,0,0);
        //加入并设置头部不显示
        addHeaderView(headView);
    }
    //初始化尾部
    public void initTail(Context context){
        tailView=LayoutInflater.from(context).inflate(R.layout.refresh_footer,null);
        pbFooter= (ProgressBar) tailView.findViewById(R.id.pb_footer);
        tvFooter=(TextView) tailView.findViewById(R.id.tv_footer);
        //加到listview中
        addFooterView(tailView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手按下去时
                //记录初始位置的值
                downY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //手滑动
                //如果正在刷新，停止执行
                if (headState==ISREFRESHING){
                    return false;
                }
                moveY=ev.getY();//滑动点Y轴坐标
                //判断第一条记录可见并且是下拉，头部可见
                if (firstVisibleItem==0&&moveY-downY>0){
                    flag=false;//不设置item点击事件
                    float headPadding=-hendHight+(moveY-downY);//拉过之后，头部的padding
                    //下拉过程状态改变，头部全部显示出来==》准备刷新
                    if (headPadding>0&&headState==INIT){
                        headState=PREPAREREFRESHING;//状态变成准备刷新
                        //改变控件赋值
                        changeState();
                    }
                    //下拉过程中，状态改变：头部不全部显示出来==>Init
                    if (headPadding<0&&headState==PREPAREREFRESHING){
                        Log.i("RefreshListView", "ACTION_MOVE4  ");
                        headState=INIT;
                        changeState();//改变控件赋值
                    }
                    headView.setPadding(0,(int)headPadding,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手放开
                //准备状态==》正在刷新
                if (headState==PREPAREREFRESHING){
                    Log.i("RefreshListView", "ACTION_UP1  ");
                    headState=ISREFRESHING;
                    changeState();
                    //更新数据源
                    if (onRefreshUploadChangeListener!=null){
                        Log.i("RefreshListView", "ACTION_UP2  ");
                        onRefreshUploadChangeListener.onRefresh();
                    }
                    //headview的padding变成0
                    headView.setPadding(0,0,0,0);
                }else if (headState==INIT){
                    Log.i("RefreshListView", "ACTION_UP3  ");
                    headView.setPadding(0,-hendHight,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void changeState() {
        switch (headState) {
            case INIT:
                //初始状态
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                //给imageView设置动画
                imageView.startAnimation(downAnimation);//设置箭头朝下转
                tvRefreshState.setText("下拉刷新");
                tvRefreshTime.setVisibility(View.INVISIBLE);
                flag = true;
                Log.i("RefreshListView", "changStateINIT  ");
                break;
            case PREPAREREFRESHING:
                Log.i("RefreshListView", "changStatePREPAREREFRESHING  ");
                //准备状态
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(upAnimation);////设置箭头朝下
                tvRefreshState.setText("释放刷新");
                tvRefreshTime.setVisibility(View.INVISIBLE);
                break;
            case ISREFRESHING:
                Log.i("RefreshListView", "changStateISREFRESHING  ");
                //正在刷新
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                imageView.clearAnimation();//清除imagview的动画
                tvRefreshState.setText("正在刷新");
                tvRefreshTime.setVisibility(View.VISIBLE);
                tvRefreshTime.setText(getTime());//刷新时间
                break;
        }
    }
    private String getTime() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time=format.format(new Date());
        return time;
    }
    public void initAnimation(Context context){
        Log.i("RefreshListView", "initAnimation  ");
        //0-》180：选择中心点在自身的原点
        upAnimation=new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setFillAfter(true);
        upAnimation.setDuration(300);
        downAnimation=new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setFillAfter(true);
        downAnimation.setDuration(300);
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //最后一条记录可见并且当前没有在刷新
        if (getLastVisiblePosition()==getCount()-1&&isLoading==false){
            Log.i("RefreshListView", "onScrollStateChanged2345  ");
            //开始或者结束
            if (scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL||scrollState==OnScrollListener.SCROLL_STATE_IDLE){
                if (downY>moveY){
                    //向下滑动
                    flag=false;
                    //界面改变
                    isLoading=true;//正在加载
                    changeFootState();//改变界面
                    if (onRefreshUploadChangeListener!=null){
                        Log.i("RefreshListView", "onScrollStateChanged  ");
                        onRefreshUploadChangeListener.onPull();//加载数据

                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem=firstVisibleItem;
    }
    //定义接口，下拉刷新，上拉加载
    public interface OnRefreshUploadChangeListener{
        void onRefresh();//下拉刷新
        void onPull();//上拉加载
    }
    public void setOnRefreshUploadChangeListener(OnRefreshUploadChangeListener onRefreshUploadChangeListener){
        this.onRefreshUploadChangeListener=onRefreshUploadChangeListener;
    }
    //刷新完成
    public void completeRefresh(){
        Log.i("RefreshListView", "completeRefresh  ");
        //padding返回去
        headView.setPadding(0,-hendHight,0,0);
        //状态改变：正在刷新
        headState=INIT;
        changeState();//初始化控件
    }
    //加载完成
    public void completeLoad(){
        Log.i("RefreshListView", "completeLoad  ");
        isLoading=false;
        changeFootState();
    }

    private void changeFootState() {
        if (isLoading){
            Log.i("RefreshListView", "changeFootState1  ");
            //正在加载，progressbar显示
            pbFooter.setVisibility(VISIBLE);
            tvFooter.setVisibility(GONE);
        }else {
            flag=true;
            Log.i("RefreshListView", "changeFootState2  ");
            //加载完成，显示文本
            pbFooter.setVisibility(GONE);
            tvFooter.setVisibility(VISIBLE);
        }
    }
}


