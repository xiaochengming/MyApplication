package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Address implements Parcelable {
	private int addressId;
	private String address;
	private String userName;// 订单的联系人
	private String phone;// 订单联系电话
	private int userId;
	private int isdefault;

	public Address() {
	}

	public Address(int addressId, String address, String userName,
			String phone, int userId, int isdefault) {
		super();
		this.addressId = addressId;
		this.address = address;
		this.userName = userName;
		this.phone = phone;
		this.userId = userId;
		this.isdefault = isdefault;
	}

	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", address=" + address
				+ ", userName=" + userName + ", phone=" + phone + ", userId="
				+ userId + ", isdefault=" + isdefault + "]";
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.addressId);
		dest.writeString(this.address);
		dest.writeString(this.userName);
		dest.writeString(this.phone);
		dest.writeInt(this.userId);
		dest.writeInt(this.isdefault);
	}

	protected Address(Parcel in) {
		this.addressId = in.readInt();
		this.address = in.readString();
		this.userName = in.readString();
		this.phone = in.readString();
		this.userId = in.readInt();
		this.isdefault = in.readInt();
	}

	public static final Creator<Address> CREATOR = new Creator<Address>() {
		@Override
		public Address createFromParcel(Parcel source) {
			return new Address(source);
		}

		@Override
		public Address[] newArray(int size) {
			return new Address[size];
		}
	};
}
