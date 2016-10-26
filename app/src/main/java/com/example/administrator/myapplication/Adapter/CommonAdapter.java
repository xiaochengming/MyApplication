package com.example.administrator.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by luhai on 2016/10/3.
 */
public abstract class CommonAdapter<T> extends BaseAdapter{
    Context context;
    List<T> lists;
    int layoutId;
    public CommonAdapter(Context context, List<T> lists,int layoutId){

        this.context=context;
        this.lists=lists;
        this.layoutId=layoutId;

    }
    @Override
    public int getCount() {
        return (lists!=null)?lists.size():0;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=ViewHolder.get(context,layoutId,convertView,parent);
        convert(viewHolder,lists.get(position),position);
        return viewHolder.getConvertView();
    }
    //取出控件，赋值
    public abstract void  convert(ViewHolder viewHolder,T t,int position);
}
