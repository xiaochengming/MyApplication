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

/**
 * Created by luhai on 2016/10/19.
 */
public class EmilFragment extends Fragment {
    MyApplication myApplication;
    String userEmil=null;
    View v;
    TextView viewEmil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v!=null){
            ViewGroup vg= (ViewGroup) v.getParent();
            if (vg!=null){
                vg.removeView(v);
            }
            myApplication= (MyApplication) getActivity().getApplication();
            userEmil=myApplication.getUser().getEmail();
            viewEmil= (TextView) v.findViewById(R.id.view_emil);
            if (userEmil!=null&&userEmil.length()>0){
                viewEmil.setText(myApplication.getUser().getEmail());
            }else if (userEmil==null&&userEmil.length()==0){
                viewEmil.setText("还没编辑邮箱");
            }
            return v;
        }

        v=inflater.inflate(R.layout.fragment_emil,null);
        myApplication= (MyApplication) getActivity().getApplication();
        userEmil=myApplication.getUser().getEmail();
        viewEmil= (TextView) v.findViewById(R.id.view_emil);
        if (userEmil!=null&&userEmil.length()>0){
            viewEmil.setText(myApplication.getUser().getEmail());
        }else if (userEmil==null&&userEmil.length()==0){
            viewEmil.setText("还没编辑邮箱");
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (userEmil!=null&&userEmil.length()>0){
            viewEmil.setText(myApplication.getUser().getEmail());
        }else if (userEmil==null&&userEmil.length()==0){
            viewEmil.setText("还没编辑邮箱");
        }
    }
}
