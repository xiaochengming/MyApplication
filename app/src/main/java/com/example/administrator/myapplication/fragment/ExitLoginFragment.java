package com.example.administrator.myapplication.fragment;

import android.content.Intent;
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
import com.example.administrator.myapplication.activity.LoginActivity;
import com.example.administrator.myapplication.activity.MainActivity;
import com.example.administrator.myapplication.entity.User;

import java.util.Date;

/**
 * Created by luhai on 2016/9/30.
 */
public class ExitLoginFragment extends Fragment{
    View v;
    MyApplication myApplication;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_exit_login,null);
        myApplication= (MyApplication) getActivity().getApplication();
        TextView tvExit= (TextView) v.findViewById(R.id.tv_exit_login);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录界面
                Intent intent=new Intent(getActivity(),MainActivity.class);
                myApplication.setFlag(false);
                myApplication.setUser(new User(0,null,0,null,2,null,null,getDate("0000-00-00"),null));
                startActivity(intent);
                Log.i("ExitLoginFragment", "onClick  121");
            }
        });
        return v;
    }
    public Date getDate(String dateSte){
        Date date=new Date(0);
        return  date;
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            return dateFormat.parse(dateSte);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
