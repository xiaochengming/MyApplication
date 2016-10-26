package com.example.administrator.myapplication.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by king on 2016/10/12.
 */
public class ViewHolder {
    private SparseArray<View> mviews;
    private int mpostion;
    private View mConvertview;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int mpostion) {
        this.mpostion = mpostion;
        this.mviews = new SparseArray<View>();
        this.mConvertview = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertview.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mpostion = position;
            return holder;
        }
    }

    public View getConvertview() {
        return mConvertview;
    }

    public <T extends View> T getView(int viewId) {
        View view = mviews.get(viewId);
        if (view == null) {
            view = mConvertview.findViewById(viewId);
            mviews.put(viewId, view);

        }
        return (T) view;

    }




}
