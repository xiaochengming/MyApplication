package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Category implements Parcelable {
	private int categoryId;
	private String name;
	private String profile;// 简介
	private String slogan;// 标语
	private String type;
	private String photo;
	private String prompt;
	private String icon;
	private List<Price> prices;

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Category(int categoryId, String name, String profile, String slogan,
			String type, String photo, String prompt, String icon,
			List<Price> prices) {
		super();
		this.categoryId = categoryId;
		this.name = name;
		this.profile = profile;
		this.slogan = slogan;
		this.type = type;
		this.photo = photo;
		this.prompt = prompt;
		this.icon = icon;
		this.prices = prices;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", name=" + name
				+ ", profile=" + profile + ", slogan=" + slogan + ", type="
				+ type + ", photo=" + photo + ", prompt=" + prompt + ", icon="
				+ icon + ", prices=" + prices + "]";
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public List<Price> getPrices() {
		return prices;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.categoryId);
		dest.writeString(this.name);
		dest.writeString(this.profile);
		dest.writeString(this.slogan);
		dest.writeString(this.type);
		dest.writeString(this.photo);
		dest.writeString(this.prompt);
		dest.writeString(this.icon);
		dest.writeList(this.prices);
	}

	protected Category(Parcel in) {
		this.categoryId = in.readInt();
		this.name = in.readString();
		this.profile = in.readString();
		this.slogan = in.readString();
		this.type = in.readString();
		this.photo = in.readString();
		this.prompt = in.readString();
		this.icon = in.readString();
		this.prices = new ArrayList<Price>();
		in.readList(this.prices, Price.class.getClassLoader());
	}

	public static final Creator<Category> CREATOR = new Creator<Category>() {
		@Override
		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		@Override
		public Category[] newArray(int size) {
			return new Category[size];
		}
	};
}
