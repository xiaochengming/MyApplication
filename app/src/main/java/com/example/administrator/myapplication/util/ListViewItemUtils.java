package com.example.administrator.myapplication.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by sony on 2016/11/8.
 */
public class ListViewItemUtils {private ListViewItemUtils(){}
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        Log.i("ListViewItemUtils", "setListViewHeightBasedOnChildren5: ");
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int itemHeight=0;

                
        for (int i = 0; i < listAdapter.getCount(); i++) {
            
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            itemHeight=listItem.getMeasuredHeight();
        }
        Log.i("ListViewItemUtils", "setListViewHeightBasedOnChildren: "+listAdapter.getCount()+"itemheight:"+itemHeight);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() ))+itemHeight*3;

        listView.setLayoutParams(params);
    }
}