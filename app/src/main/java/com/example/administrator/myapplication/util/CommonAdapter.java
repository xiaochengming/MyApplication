package com.example.administrator.myapplication.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by king on 2016/10/12.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> mDatas;
    protected LayoutInflater inflater;
    protected int layoutId;

    public CommonAdapter(Context context, List<T> mDatas, int layoutId) {
        this.mDatas = mDatas;
        this.context = context;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertview, parent, layoutId, position);
        convert(holder, getItem(position), position);
        return holder.getConvertview();
    }


    public abstract void convert(ViewHolder holder, T t, int position);
}
