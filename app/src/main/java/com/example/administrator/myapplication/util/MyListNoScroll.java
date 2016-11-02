package com.example.administrator.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;


import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by king on 2016/10/30.
 */
public class MyListNoScroll extends ListView {
    View view;
    TextView textView;

    public MyListNoScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
   initViewFoot( context);
    }

    public MyListNoScroll(Context context) {
        this(context,null);

    }

    public MyListNoScroll(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    //设置listview的尾部
    public void initViewFoot(Context context) {

        view = LayoutInflater.from(context).inflate(R.layout.yan_listview_foot, null);

        textView = (TextView) view.findViewById(R.id.prod_info_tv_prod_comment);
        //加到listview中
        addFooterView(view);
    }
}
