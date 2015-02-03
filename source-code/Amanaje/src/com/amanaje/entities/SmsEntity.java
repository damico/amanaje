package com.amanaje.entities;

public class SmsEntity {
	
	private String body = null;
	private String address = null;
	private long date = -0l;
	private String pubKey = null;
	private String seed = null;
	private String privKeyPasswd = null;
	
	
	
	public String getPrivKeyPasswd() {
		return privKeyPasswd;
	}
	public void setPrivKeyPasswd(String privKeyPasswd) {
		this.privKeyPasswd = privKeyPasswd;
	}
	public String getPubKey() {
		return pubKey;
	}
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	public SmsEntity(String body, String address, long date) {
		super();
		this.body = body;
		this.address = address;
		this.date = date;
	}
	public SmsEntity() {
		// TODO Auto-generated constructor stub
	}
	
	

}
