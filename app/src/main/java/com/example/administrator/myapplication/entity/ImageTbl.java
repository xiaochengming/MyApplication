package com.example.administrator.myapplication.entity;

public class ImageTbl {
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
	@Override
	public String toString() {
		return "ImageTbl [imageId=" + imageId + ", evaluate=" + evaluate + ", imageAddress=" + imageAddress + "]";
	}
	
}
