package com.example.administrator.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by king on 2016/10/31.
 */
public class TimeCountDownTextView extends TextView implements Runnable{
    private long  mhour, mmin, msecond;//小时，分钟，秒
    private boolean run = false; //是否启动了

    public TimeCountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setTimes(long[] times) {

        mhour = times[0];
        mmin = times[1];
        msecond = times[2];

    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;

            }

        }

    }

    public boolean isRun() {
        return run;
    }

    public void beginRun() {
        this.run = true;
        run();
    }

    public void stopRun() {
        this.run = false;
    }


    @Override
    public void run() {
        //标示已经启动
        if (run) {
            ComputeTime();

            String strTime =  mhour + "时:" + mmin + "分:" + msecond + "秒";
            this.setText(strTime);

            postDelayed(this, 1000);
        } else {
            removeCallbacks(this);
        }
    }
}
