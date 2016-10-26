package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Price implements Parcelable {
	private int priceId;
	private int categoryId;
	private float price;
	private String unit;
	private float min;// 起步价
	private int workertime;
	private String subname;

	public Price() {
		super();
	}

	public Price(int priceId, int categoryId, float price, String unit,
			float min, int workertime, String subname) {
		super();
		this.priceId = priceId;
		this.categoryId = categoryId;
		this.price = price;
		this.unit = unit;
		this.min = min;
		this.workertime = workertime;
		this.subname = subname;
	}

	public int getPriceId() {
		return priceId;
	}

	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public int getWorkertime() {
		return workertime;
	}

	public void setWorkertime(int workertime) {
		this.workertime = workertime;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", categoryId=" + categoryId
				+ ", price=" + price + ", unit=" + unit + ", min=" + min
				+ ", workertime=" + workertime + ", subname=" + subname + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.priceId);
		dest.writeInt(this.categoryId);
		dest.writeFloat(this.price);
		dest.writeString(this.unit);
		dest.writeFloat(this.min);
		dest.writeInt(this.workertime);
		dest.writeString(this.subname);
	}

	protected Price(Parcel in) {
		this.priceId = in.readInt();
		this.categoryId = in.readInt();
		this.price = in.readFloat();
		this.unit = in.readString();
		this.min = in.readFloat();
		this.workertime = in.readInt();
		this.subname = in.readString();
	}

	public static final Creator<Price> CREATOR = new Creator<Price>() {
		@Override
		public Price createFromParcel(Parcel source) {
			return new Price(source);
		}

		@Override
		public Price[] newArray(int size) {
			return new Price[size];
		}
	};
}
