package com.hit.changyou.model;


import android.content.Context;


public class User {
	String tinyurl;
	String birthday;
	String media_type;
	int sex;//xixihaha
	String username;
	String social_uid;
	String access_token;
	
	Context context;
	
	public Context getContext() {
		return context;
	}
	public User setContext(Context context) {
		this.context = context;
		return this;
	}
	public String getTinyurl() {
		return tinyurl;
	}
	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMedia_type() {
		return media_type;
	}
	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSocial_uid() {
		return social_uid;
	}
	public void setSocial_uid(String social_uid) {
		this.social_uid = social_uid;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	@Override
	public String toString() {
		return "User [tinyurl=" + tinyurl + ", birthday=" + birthday
				+ ", media_type=" + media_type + ", sex=" + sex + ", username="
				+ username + ", social_uid=" + social_uid + ", access_token="
				+ access_token + ", context=" + context + "]";
	}
	
}
