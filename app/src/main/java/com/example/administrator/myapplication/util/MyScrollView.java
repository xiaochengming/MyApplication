package com.example.administrator.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by king on 2016/10/22.
 */

public class MyScrollView extends ScrollView {
    private float xStart;
    private float yStart;
    private float xDistance;
    private float yDistance;
    private float xEnd;
    private float yEnd;


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                xDistance = yDistance = 0f;

                xStart = ev.getX();

                yStart = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                xEnd = ev.getX();

                yEnd = ev.getY();

                break;
            case MotionEvent.ACTION_UP:
                xEnd = ev.getX();
                yEnd = ev.getY();
                break;
        }

        xDistance = Math.abs(xEnd - xStart);

        yDistance = Math.abs(yEnd - yStart);
        //点击事件的分发

        //若要拦截，则return true，否则return false（不拦截）


        return false;
    }
}
