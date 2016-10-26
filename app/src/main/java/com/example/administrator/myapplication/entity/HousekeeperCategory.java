package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HousekeeperCategory implements Parcelable {
	private int housekeeperCategoryId;
	private Housekeeper housekeeper;
	private Category category;
	private float price;

	public HousekeeperCategory() {
	}

	public HousekeeperCategory(Housekeeper housekeeper, Category category,
							   float price) {
		this.housekeeper = housekeeper;
		this.category = category;
		this.price = price;
	}

	public HousekeeperCategory(Housekeeper housekeeper,
							   int housekeeperCategoryId, Category category, float price) {
		this.housekeeper = housekeeper;
		this.housekeeperCategoryId = housekeeperCategoryId;
		this.category = category;
		this.price = price;
	}

	@Override
	public String toString() {
		return "HousekeeperCategory{" + "housekeeperCategoryId="
				+ housekeeperCategoryId + ", housekeeper=" + housekeeper
				+ ", category=" + category + ", price=" + price + '}';
	}

	public int getHousekeeperCategoryId() {
		return housekeeperCategoryId;
	}

	public void setHousekeeperCategoryId(int housekeeperCategoryId) {
		this.housekeeperCategoryId = housekeeperCategoryId;
	}

	public Housekeeper getHousekeeper() {
		return housekeeper;
	}

	public void setHousekeeper(Housekeeper housekeeper) {
		this.housekeeper = housekeeper;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.housekeeperCategoryId);
		dest.writeParcelable(this.housekeeper, flags);
		dest.writeParcelable(this.category, flags);
		dest.writeFloat(this.price);
	}

	protected HousekeeperCategory(Parcel in) {
		this.housekeeperCategoryId = in.readInt();
		this.housekeeper = in.readParcelable(Housekeeper.class.getClassLoader());
		this.category = in.readParcelable(Category.class.getClassLoader());
		this.price = in.readFloat();
	}

	public static final Creator<HousekeeperCategory> CREATOR = new Creator<HousekeeperCategory>() {
		@Override
		public HousekeeperCategory createFromParcel(Parcel source) {
			return new HousekeeperCategory(source);
		}

		@Override
		public HousekeeperCategory[] newArray(int size) {
			return new HousekeeperCategory[size];
		}
	};
}
