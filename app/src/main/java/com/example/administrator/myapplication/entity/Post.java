package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 帖子
 *
 * @author Administrator
 */

public class Post implements Parcelable {
    private int postId;
    private User user;
    private String postContent;// 帖子内容
    private Timestamp postTimes;// 发帖时间
    private Dynamic firstDynamic;// 第一个评论
    private int number;// 点赞数
    private int pingLunnum;
    private boolean iszan;// 是否点赞
    private List<String> imageList;//图片地址

    public Post(int postId, User user, String postContent, Timestamp postTimes, Dynamic firstDynamic, int number, int pingLunnum, boolean iszan, List<String> imageList) {
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
        this.firstDynamic = firstDynamic;
        this.number = number;
        this.pingLunnum = pingLunnum;
        this.iszan = iszan;
        this.imageList = imageList;
    }

    public Post(int postId, User user, String postContent, Timestamp postTimes, Dynamic firstDynamic, int number, int pingLunnum, boolean iszan) {
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
        this.firstDynamic = firstDynamic;
        this.number = number;
        this.pingLunnum = pingLunnum;
        this.iszan = iszan;
    }

    public Post(User user, String postContent, Timestamp postTimes) {
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
    }

    public Post(int postId, User user, String postContent, Timestamp postTimes,
                Dynamic firstDynamic, int number, int pingLunnum) {
        super();
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
        this.firstDynamic = firstDynamic;
        this.number = number;
        this.pingLunnum = pingLunnum;
    }

    public Post(int postId, User user, String postContent, Timestamp postTimes,
                Dynamic firstDynamic) {
        super();
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
        this.firstDynamic = firstDynamic;
    }

    public Post(int postId, User user, String postContent, Timestamp postTimes,
                Dynamic firstDynamic, int pingLunnum) {
        super();
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTimes = postTimes;
        this.firstDynamic = firstDynamic;
        this.pingLunnum = pingLunnum;
    }

    public boolean getiszan() {
        return iszan;
    }

    public void setIszan(boolean iszan) {
        this.iszan = iszan;
    }

    public int getPostId() {
        return postId;
    }

    public int getPingLunnum() {
        return pingLunnum;
    }

    public void setPingLunnum(int pingLunnum) {
        this.pingLunnum = pingLunnum;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Timestamp getPostTimes() {
        return postTimes;
    }

    public void setPostTimes(Timestamp postTimes) {
        this.postTimes = postTimes;
    }

    public Dynamic getFirstDynamic() {
        return firstDynamic;
    }

    public void setFirstDynamic(Dynamic firstDynamic) {
        this.firstDynamic = firstDynamic;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean iszan() {
        return iszan;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public Post() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.postId);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.postContent);
        dest.writeSerializable(this.postTimes);
        dest.writeParcelable(this.firstDynamic, flags);
        dest.writeInt(this.number);
        dest.writeInt(this.pingLunnum);
        dest.writeByte(this.iszan ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.imageList);
    }

    protected Post(Parcel in) {
        this.postId = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.postContent = in.readString();
        this.postTimes = (Timestamp) in.readSerializable();
        this.firstDynamic = in.readParcelable(Dynamic.class.getClassLoader());
        this.number = in.readInt();
        this.pingLunnum = in.readInt();
        this.iszan = in.readByte() != 0;
        this.imageList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
