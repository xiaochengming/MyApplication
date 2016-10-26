package com.example.administrator.myapplication.entityMi;


public class Zan {
	private int zanId;
	private int postId;
	private int userId;
	private int iszan;

	public Zan(int postId, int userId, int iszan) {
		super();
		this.postId = postId;
		this.userId = userId;
		this.iszan = iszan;
	}

	public Zan(int zanId, int postId, int userId, int iszan) {
		super();
		this.zanId = zanId;
		this.postId = postId;
		this.userId = userId;
		this.iszan = iszan;
	}

	public int getZanId() {
		return zanId;
	}

	public void setZanId(int zanId) {
		this.zanId = zanId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIszan() {
		return iszan;
	}

	public void setIszan(int iszan) {
		this.iszan = iszan;
	}

	@Override
	public String toString() {
		return "Zan [zanId=" + zanId + ", postId=" + postId + ", userId="
				+ userId + ", iszan=" + iszan + "]";
	}

}
