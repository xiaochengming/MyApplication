package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Housekeeper implements Parcelable {
	private int housekeeperId;
	private int sex;
	private int age;
	private String name;
	private int serviceplevel;
	private int serviceTime;//服务次数
	private String placeOfOrigin;//籍贯
	private String housePhoto;//照片
	private String introduce;//介绍
	private String housePhone;//保姆电话

	public String getHousePhone() {
		return housePhone;
	}

	public void setHousePhone(String housePhone) {
		this.housePhone = housePhone;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getHousePhoto() {
		return housePhoto;
	}

	public void setHousePhoto(String housePhoto) {
		this.housePhoto = housePhoto;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Housekeeper() {
	}

	public Housekeeper(int sex,String name, int age, int serviceplevel, int serviceTime, String placeOfOrigin,
			String housePhoto) {
		super();
		this.sex = sex;
		this.age = age;
		this.name = name;
		this.serviceplevel = serviceplevel;
		this.serviceTime = serviceTime;
		this.placeOfOrigin = placeOfOrigin;
		this.housePhoto = housePhoto;
	}

	public Housekeeper(int housekeeperId) {
		this.housekeeperId = housekeeperId;
	}

	public Housekeeper(int housekeeperId, int sex, String name, int age, int serviceplevel, int serviceTime, String placeOfOrigin,
					   String housePhoto,String housePhone) {
		super();
		this.housekeeperId=housekeeperId;
		this.sex = sex;
		this.age = age;
		this.name = name;
		this.serviceplevel = serviceplevel;
		this.serviceTime = serviceTime;
		this.placeOfOrigin = placeOfOrigin;
		this.housePhoto = housePhoto;
		this.housePhone = housePhone;
	}

	public Housekeeper(int sex, int age, String name, int serviceplevel) {
		this.sex = sex;
		this.age = age;
		this.name = name;
		this.serviceplevel = serviceplevel;
	}

	public Housekeeper(int housekeeperId, int sex, int age, String name,
			int serviceplevel) {
		this.housekeeperId = housekeeperId;
		this.sex = sex;
		this.age = age;
		this.name = name;
		this.serviceplevel = serviceplevel;
	}


	public int getHousekeeperId() {
		return housekeeperId;
	}

	public void setHousekeeperId(int housekeeperId) {
		this.housekeeperId = housekeeperId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getServiceplevel() {
		return serviceplevel;
	}

	public void setServiceplevel(int serviceplevel) {
		this.serviceplevel = serviceplevel;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.housekeeperId);
		dest.writeInt(this.sex);
		dest.writeInt(this.age);
		dest.writeString(this.name);
		dest.writeInt(this.serviceplevel);
		dest.writeInt(this.serviceTime);
		dest.writeString(this.placeOfOrigin);
		dest.writeString(this.housePhoto);
		dest.writeString(this.introduce);
		dest.writeString(this.housePhone);
	}

	protected Housekeeper(Parcel in) {
		this.housekeeperId = in.readInt();
		this.sex = in.readInt();
		this.age = in.readInt();
		this.name = in.readString();
		this.serviceplevel = in.readInt();
		this.serviceTime = in.readInt();
		this.placeOfOrigin = in.readString();
		this.housePhoto = in.readString();
		this.introduce = in.readString();
		this.housePhone = in.readString();
	}

	public static final Creator<Housekeeper> CREATOR = new Creator<Housekeeper>() {
		@Override
		public Housekeeper createFromParcel(Parcel source) {
			return new Housekeeper(source);
		}

		@Override
		public Housekeeper[] newArray(int size) {
			return new Housekeeper[size];
		}
	};
}
