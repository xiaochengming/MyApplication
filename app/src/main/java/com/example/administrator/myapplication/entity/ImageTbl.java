package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageTbl implements Parcelable {
	private int imageId;
	private Evaluate evaluate;
	private String imageAddress;
	
	public ImageTbl() {
		super();
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public Evaluate getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(Evaluate evaluate) {
		this.evaluate = evaluate;
	}
	public String getImageAddress() {
		return imageAddress;
	}
	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}
	public ImageTbl(int imageId, String imageAddress) {
		super();
		this.imageId = imageId;
		this.imageAddress = imageAddress;
	}
	public ImageTbl(int imageId, Evaluate evaluate, String imageAddress) {
		super();
		this.imageId = imageId;
		this.evaluate = evaluate;
		this.imageAddress = imageAddress;
	}
	@Override
	public String toString() {
		return "ImageTbl [imageId=" + imageId + ", evaluate=" + evaluate + ", imageAddress=" + imageAddress + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.imageId);
		dest.writeParcelable(this.evaluate, flags);
		dest.writeString(this.imageAddress);
	}

	protected ImageTbl(Parcel in) {
		this.imageId = in.readInt();
		this.evaluate = in.readParcelable(Evaluate.class.getClassLoader());
		this.imageAddress = in.readString();
	}

	public static final Creator<ImageTbl> CREATOR = new Creator<ImageTbl>() {
		@Override
		public ImageTbl createFromParcel(Parcel source) {
			return new ImageTbl(source);
		}

		@Override
		public ImageTbl[] newArray(int size) {
			return new ImageTbl[size];
		}
	};
}
