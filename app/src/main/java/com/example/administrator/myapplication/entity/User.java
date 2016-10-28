package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/21.
 */
public class User implements Parcelable {
	private int userId;
	private String name;
	private int sex;
	private String photo;// 图片路径
	private int age;
	private String password;
	private String number;
	private Date birthday;
    private String email;


	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", name='" + name + '\'' +
				", sex=" + sex +
				", photo='" + photo + '\'' +
				", age=" + age +
				", password='" + password + '\'' +
				", number='" + number + '\'' +
				", birthday=" + birthday +
				", email='" + email + '\'' +
				'}';
	}

	public User(int userId) {
		this.userId = userId;
	}

	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public User() {
	}

	public User(Integer userId, String name, Integer sex, String photo, Integer age,String number, String password,  Date birthday, String email) {
		this.userId = userId;
		this.name = name;
		this.sex = sex;
		this.photo = photo;
		this.age = age;
		this.password = password;
		this.number = number;
		this.birthday = birthday;
		this.email = email;
	}
	public User(String name,String number,int age,int sex) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.number = number;
	}


	public User(String number, String password) {
		super();
		this.password = password;
		this.number = number;
	}

	public User(int userId,String number,String password) {
		super();
		this.userId = userId;
		this.password = password;
		this.number = number;
	}


	public User(String name, int sex, String photo, int age, String password,
			String number) {
		this.name = name;
		this.sex = sex;
		this.photo = photo;
		this.age = age;
		this.password = password;
		this.number = number;
	}
	
	public User(int userId, String name, int sex, int age, String password, String number) {
		super();
		this.userId = userId;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.password = password;
		this.number = number;
	}

	public User(int userId, String name, int sex, String photo, int age,
			String number,String password) {
		this.userId = userId;
		this.name = name;
		this.sex = sex;
		this.photo = photo;
		this.age = age;
		this.password = password;
		this.number = number;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.userId);
		dest.writeString(this.name);
		dest.writeInt(this.sex);
		dest.writeString(this.photo);
		dest.writeInt(this.age);
		dest.writeString(this.password);
		dest.writeString(this.number);
		dest.writeLong(this.birthday != null ? this.birthday.getTime() : -1);
		dest.writeString(this.email);
	}

	protected User(Parcel in) {
		this.userId = in.readInt();
		this.name = in.readString();
		this.sex = in.readInt();
		this.photo = in.readString();
		this.age = in.readInt();
		this.password = in.readString();
		this.number = in.readString();
		long tmpBirthday = in.readLong();
		this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
		this.email = in.readString();
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
