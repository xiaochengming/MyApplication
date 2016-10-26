package com.example.administrator.myapplication.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Housekeeper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/20.
 */
public class MybaseAdapter extends BaseAdapter {
    Context context;
    List<Housekeeper> houseKeepers=new ArrayList<Housekeeper>();
    //构造方法
    public MybaseAdapter(Context context, List<Housekeeper> houseKeepers){
        this.context=context;
        this.houseKeepers=houseKeepers;
    }

    @Override
    public int getCount() {
        return houseKeepers.size();
    }

    @Override
    public Object getItem(int position) {
        return houseKeepers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        ViewHoder viewHoder;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.lv_item,null);
            viewHoder=new ViewHoder();
            //找到view的控件
//            viewHoder.tvName= (TextView) convertView.findViewById(R.id.tv_name);
//            viewHoder.tvCount= (TextView) convertView.findViewById(R.id.tv_count);
//            viewHoder.tvAddress= (TextView) convertView.findViewById(R.id.tv_address);
//            viewHoder.tvAge= (TextView) convertView.findViewById(R.id.tv_age);
//            viewHoder.butDetails= (Button) convertView.findViewById(R.id.but_details);
//            viewHoder.ratingBar= (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(viewHoder);
        }else {
            viewHoder= (ViewHoder) convertView.getTag();
        }
        viewHoder.butDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情页面
            }
        });
        //给view控件赋值
//        viewHoder.tvName.setText(houseKeepers.get(position).getName());
//        viewHoder.tvCount.setText(houseKeepers.get(position).getCount());
//        viewHoder.tvAddress.setText(houseKeepers.get(position).getAddress());
//        viewHoder.tvAge.setText(houseKeepers.get(position).getAge());
        return convertView;
        //设置按钮点击事件

    }
    class ViewHoder{
        ImageView imageView;
        TextView tvName,tvCount,tvAddress,tvAge;
        Button butDetails;
        RatingBar ratingBar;
    }
}
