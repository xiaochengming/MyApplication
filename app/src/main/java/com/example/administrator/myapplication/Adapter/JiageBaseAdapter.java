package com.example.administrator.myapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Price;

import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
public class JiageBaseAdapter extends BaseAdapter {
    List<Price> list;
    Context context;

    public JiageBaseAdapter(List<Price> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("JiageBaseAdapter", "getCount: "+list.size());
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("JiageBaseAdapter", "getView: ");
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.price_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_jiaGe);
            TextView btn = (TextView) convertView.findViewById(R.id.btn_yuYue);
            viewHolder = new ViewHolder(tv, btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.i("JiageBaseAdapter", "getView: "+list.get(position).getPrice());
        }
        if (list.get(position).getSubname()==null){
            viewHolder.tv.setText("￥" + list.get(position).getPrice() + list.get(position).getUnit());
        }else{
            viewHolder.tv.setText(list.get(position).getSubname()+"    ￥" + list.get(position).getPrice() + list.get(position).getUnit());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv;
        TextView btn;

        public ViewHolder(TextView tv, TextView btn) {
            this.tv = tv;
            this.btn = btn;
        }
    }
}
