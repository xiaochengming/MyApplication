package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * 社区
 * 
 * @author Administrator
 *
 */
public class Dynamic implements Parcelable {
	private int dynamicId;
	private User user;
	private int postId;
	private String dynamicContent;// 动态内容
	private Timestamp dynamicTime;// 动态发表的时间
	private int parent;// 父评论的id
	private int hasNext;// 是否有下一个评论
	private String image; // 图片地址
	private int number;// 点赞数

	public Dynamic(int dynamicId, User user, int postId, String dynamicContent,
			Timestamp dynamicTime, int parent, int hasNext) {
		super();
		this.dynamicId = dynamicId;
		this.user = user;
		this.postId = postId;
		this.dynamicContent = dynamicContent;
		this.dynamicTime = dynamicTime;
		this.parent = parent;
		this.hasNext = hasNext;
	}

	public Dynamic(int dynamicId, User user, int postId, String dynamicContent,
			Timestamp dynamicTime, int parent, int hasNext, String image,
			int number) {
		super();
		this.dynamicId = dynamicId;
		this.user = user;
		this.postId = postId;
		this.dynamicContent = dynamicContent;
		this.dynamicTime = dynamicTime;
		this.parent = parent;
		this.hasNext = hasNext;
		this.image = image;
		this.number = number;
	}

	@Override
	public String toString() {
		return "Dynamic [dynamicId=" + dynamicId + ", user=" + user
				+ ", postId=" + postId + ", dynamicContent=" + dynamicContent
				+ ", dynamicTime=" + dynamicTime + ", parent=" + parent
				+ ", hasNext=" + hasNext + ", image=" + image + ", number="
				+ number + "]";
	}

	public int getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(int dynamicId) {
		this.dynamicId = dynamicId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getDynamicContent() {
		return dynamicContent;
	}

	public void setDynamicContent(String dynamicContent) {
		this.dynamicContent = dynamicContent;
	}

	public Timestamp getDynamicTime() {
		return dynamicTime;
	}

	public void setDynamicTime(Timestamp dynamicTime) {
		this.dynamicTime = dynamicTime;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getHasNext() {
		return hasNext;
	}

	public void setHasNext(int hasNext) {
		this.hasNext = hasNext;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Dynamic() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.dynamicId);
		dest.writeParcelable(this.user, flags);
		dest.writeInt(this.postId);
		dest.writeString(this.dynamicContent);
		dest.writeSerializable(this.dynamicTime);
		dest.writeInt(this.parent);
		dest.writeInt(this.hasNext);
		dest.writeString(this.image);
		dest.writeInt(this.number);
	}

	protected Dynamic(Parcel in) {
		this.dynamicId = in.readInt();
		this.user = in.readParcelable(User.class.getClassLoader());
		this.postId = in.readInt();
		this.dynamicContent = in.readString();
		this.dynamicTime = (Timestamp) in.readSerializable();
		this.parent = in.readInt();
		this.hasNext = in.readInt();
		this.image = in.readString();
		this.number = in.readInt();
	}

	public static final Creator<Dynamic> CREATOR = new Creator<Dynamic>() {
		@Override
		public Dynamic createFromParcel(Parcel source) {
			return new Dynamic(source);
		}

		@Override
		public Dynamic[] newArray(int size) {
			return new Dynamic[size];
		}
	};
}
