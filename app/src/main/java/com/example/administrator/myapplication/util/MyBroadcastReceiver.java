package com.example.administrator.myapplication.util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.HuifuActivity;
import com.example.administrator.myapplication.activity.PersonalInformationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by luhai on 2016/11/4.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ponpay";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");

            //打开自定义的Activity
            Intent intent2 = new Intent(context, HuifuActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
// send msg to MainActivity

    /**
     * 可能经常用到的一点，获取附加的自定义的字段
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        // if (MainActivity.isForeground) {//检查当前软件是否在前台
        // 利用JPushInterface.EXTRA_MESSAGE 机械能推送消息的获取
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);


        // 可能经常用到的一点，获取附加的自定义的字段、
        // 这个字符串就是Json的格式，用于自己的服务器给特定的客户端传递一些特定的属性和配置，
        // 例如显示一些数字、特定的事件，或者是访问特定的网址的时候，使用extras
        // 例如显示订单信息、特定的商品列表，特定的咨询网址
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);


        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentText(message).setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Message");
        builder.setDefaults(Notification.DEFAULT_SOUND);


        Log.i("Jpush", extras + "~~");

        int drawResId = R.drawable.ic_launcher;
        int num = 0;
        String title = "hello";
        int iconType = 0;

        /**
         * 自定义信息： 获取
         * */
        if (extras != null) {
            try {
                JSONObject object = new JSONObject(extras);
                num = object.optInt("num");
                title = object.optString("title", "hello");
                iconType = object.optInt("iconType");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        builder.setContentText(title);
    }
}
