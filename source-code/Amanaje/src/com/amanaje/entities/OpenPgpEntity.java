package com.amanaje.entities;

public class OpenPgpEntity {
	
	private String phoneNumber = null;
	private String privKeyPassword = null;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPrivKeyPassword() {
		return privKeyPassword;
	}
	public void setPrivKeyPassword(String privKeyPassword) {
		this.privKeyPassword = privKeyPassword;
	}
	public OpenPgpEntity(String phoneNumber, String privKeyPassword) {
		super();
		this.phoneNumber = phoneNumber;
		this.privKeyPassword = privKeyPassword;
	}
	
	

}
