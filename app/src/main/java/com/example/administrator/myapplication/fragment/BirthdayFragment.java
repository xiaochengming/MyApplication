package com.example.administrator.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    String userBirthday=null;
    View v;
    TextView tvBirthday;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("BirthdayFragment", "onAttach  ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BirthdayFragment", "onCreate  ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("BirthdayFragment", "onActivityCreated  ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("BirthdayFragment", "onStart  ");
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
            if (userBirthday.equals("")){
                tvBirthday.setText("还没编辑生日");
            }else {
                tvBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
            }

        }else {
            tvBirthday.setText("还没编辑生日");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("BirthdayFragment", "onResume  ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("BirthdayFragment", "onPause  ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("BirthdayFragment", "onStop  ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("BirthdayFragment", "onDestroyView  ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("BirthdayFragment", "onDestroy  ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("BirthdayFragment", "onDetach  ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("BirthdayFragment", "onCreateView  ");
        if (v!=null){
            ViewGroup vg= (ViewGroup) v.getParent();
            if (vg!=null){
                vg.removeView(v);
            }
            myApplication= (MyApplication) getActivity().getApplication();
            tvBirthday= (TextView) v.findViewById(R.id.view_birthday);
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
                if (userBirthday.equals("")){
                    tvBirthday.setText("还没编辑生日");
                }else {
                    tvBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
                }
            }else {
                tvBirthday.setText("还没编辑生日");
            }

            return v;
        }

        v=inflater.inflate(R.layout.fragment_birthday,null);
        myApplication= (MyApplication) getActivity().getApplication();
        tvBirthday= (TextView) v.findViewById(R.id.view_birthday);
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
            if (userBirthday.equals("")){
                tvBirthday.setText("还没编辑生日");
            }else {
                tvBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
            }
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
