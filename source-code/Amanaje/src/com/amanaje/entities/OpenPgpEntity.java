package com.amanaje.entities;

public class OpenPgpEntity {
	
	private String phoneNumber = null;
	private String privKeyPassword = null;
	private String aKey1 = null;
	private String aKey2 = null;
	
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
	public OpenPgpEntity(String phoneNumber, String privKeyPassword, String aKey1, String aKey2) {
		super();
		this.phoneNumber = phoneNumber;
		this.privKeyPassword = privKeyPassword;
		this.aKey1 = aKey1;
		this.aKey2 = aKey2;
		
	}
	public String getaKey1() {
		return aKey1;
	}
	public void setaKey1(String aKey1) {
		this.aKey1 = aKey1;
	}
	public String getaKey2() {
		return aKey2;
	}
	public void setaKey2(String aKey2) {
		this.aKey2 = aKey2;
	}
	
	

}
