package com.example.administrator.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/10/28.
 */
public class ImageUtil {
    private Context context;

    public ImageUtil(Context context) {
        this.context = context;
    }
    /**
     * 获取全部图片地址
     * @return
     */
    public ArrayList<String> listAlldir(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        ArrayList<String> list = new ArrayList<String>();
        String[] proj ={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
        while(cursor.moveToNext()){
            String path =cursor.getString(0);
            list.add(new File(path).getAbsolutePath());
        }
        return list;
    }
    //获取所有图片文件夹和文件夹下的图片地址列表
    public List<FileTraversal> LocalImgFileList(){
        List<FileTraversal> data=new ArrayList<FileTraversal>();
        String filename="";
        List<String> allimglist=listAlldir();
        List<String> retulist=new ArrayList<String>();
        if (allimglist!=null) {
            Set set = new TreeSet();
            String[] str;
            for (int i = 0; i < allimglist.size(); i++) {
                retulist.add(getfileinfo(allimglist.get(i)));
            }
            for (int i = 0; i < retulist.size(); i++) {
                set.add(retulist.get(i));
            }
            str= (String[]) set.toArray(new String[0]);
            for (int i = 0; i < str.length; i++) {
                filename=str[i];
                FileTraversal ftl= new FileTraversal();
                ftl.setFilename(filename);
                data.add(ftl);
            }

            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < allimglist.size(); j++) {
                    if (data.get(i).getFilename().equals(getfileinfo(allimglist.get(j)))) {
                        data.get(i).getFilecontent().add(allimglist.get(j));
                    }
                }
            }
        }
        return data;
    }
    //获取图片文件夹名
    public String getfileinfo(String data){
        String filename[]= data.split("/");
        if (filename!=null) {
            return filename[filename.length-2];
        }
        return null;
    }
}
