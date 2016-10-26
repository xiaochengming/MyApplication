package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luhai on 2016/10/19.
 */
public class BirthdayFragment extends Fragment {
    MyApplication myApplication;
    String userBirthday;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v!=null){
            ViewGroup vg= (ViewGroup) v.getParent();
            if (vg!=null){
                vg.removeView(v);
            }
            myApplication= (MyApplication) getActivity().getApplication();
            TextView tvBirthday= (TextView) v.findViewById(R.id.view_birthday);
            if (myApplication.getUser().getBirthday()==null){
                userBirthday=null;
            } else {
                if (dateToString(myApplication.getUser().getBirthday()).equals("1970-01-01")){
                    userBirthday=null;
                }else {
                    userBirthday=dateToString(myApplication.getUser().getBirthday());
                }
            }
            if (userBirthday!=null){
                tvBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
            }else {
                tvBirthday.setText("还没编辑生日");
            }

            return v;
        }

        v=inflater.inflate(R.layout.fragment_birthday,null);
        myApplication= (MyApplication) getActivity().getApplication();
        TextView tvBirthday= (TextView) v.findViewById(R.id.view_birthday);
        if (myApplication.getUser().getBirthday()==null){
            userBirthday=null;
        } else {
            if (dateToString(myApplication.getUser().getBirthday()).equals("1970-01-01")){
                userBirthday=null;
            }else {
                userBirthday=dateToString(myApplication.getUser().getBirthday());
            }
        }
        if (userBirthday!=null){
            tvBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
        }else {
            tvBirthday.setText("还没编辑生日");
        }

        return v;
    }
    //date转换成String
    public String dateToString(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
