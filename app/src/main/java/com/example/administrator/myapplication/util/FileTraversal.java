package com.example.administrator.myapplication.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class FileTraversal implements Parcelable {
    private String filename;//所属图片的文件名称
    private List<String> filecontent=new ArrayList<String>();

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<String> getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(List<String> filecontent) {
        this.filecontent = filecontent;
    }

    public FileTraversal(String filename, List<String> filecontent) {
        this.filename = filename;
        this.filecontent = filecontent;
    }

    public FileTraversal() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filename);
        dest.writeStringList(this.filecontent);
    }

    protected FileTraversal(Parcel in) {
        this.filename = in.readString();
        this.filecontent = in.createStringArrayList();
    }

    public static final Parcelable.Creator<FileTraversal> CREATOR = new Parcelable.Creator<FileTraversal>() {
        @Override
        public FileTraversal createFromParcel(Parcel source) {
            return new FileTraversal(source);
        }

        @Override
        public FileTraversal[] newArray(int size) {
            return new FileTraversal[size];
        }
    };
}
