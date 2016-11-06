package com.example.administrator.myapplication.Application;



import android.app.Application;
import android.util.Log;

import com.example.administrator.myapplication.entity.User;

import org.xutils.x;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by luhai on 2016/10/3.
 */
public class MyApplication extends Application {
    boolean flag=false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private User user=new User(0,null,0,null,2,null,null,getDate("0000-00-00"),null);

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Date getDate(String dateSte){
        Date date=new Date(0);
        return  date;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        /**
         * 初始化融云
         */
        RongIM.init(this);

        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i("MyApplication", "onTerminate  ");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i("MyApplication", "onLowMemory  ");
    }
}
