package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Manager implements Parcelable {
	private int managerId;
	private String name;
	private String password;
	private int sex;
	private int age;
	private String position;// 职位

	public Manager() {
	}

	public Manager(String name, String password, int sex, int age,
			String position) {
		this.name = name;
		this.password = password;
		this.sex = sex;
		this.age = age;
		this.position = position;
	}

	public Manager(int managerId, String name, String password, int sex,
			int age, String position) {
		this.managerId = managerId;
		this.name = name;
		this.password = password;
		this.sex = sex;
		this.age = age;
		this.position = position;
	}

	@Override
	public String toString() {
		return "Manager{" + "managerId=" + managerId + ", name='" + name + '\''
				+ ", password='" + password + '\'' + ", sex=" + sex + ", age="
				+ age + ", position='" + position + '\'' + '}';
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.managerId);
		dest.writeString(this.name);
		dest.writeString(this.password);
		dest.writeInt(this.sex);
		dest.writeInt(this.age);
		dest.writeString(this.position);
	}

	protected Manager(Parcel in) {
		this.managerId = in.readInt();
		this.name = in.readString();
		this.password = in.readString();
		this.sex = in.readInt();
		this.age = in.readInt();
		this.position = in.readString();
	}

	public static final Creator<Manager> CREATOR = new Creator<Manager>() {
		@Override
		public Manager createFromParcel(Parcel source) {
			return new Manager(source);
		}

		@Override
		public Manager[] newArray(int size) {
			return new Manager[size];
		}
	};
}
