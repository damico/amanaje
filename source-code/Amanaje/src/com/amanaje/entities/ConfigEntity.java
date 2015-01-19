package com.amanaje.entities;

public class ConfigEntity {

	private String encAlgo = null;
	private String hashedKey = null;
	private boolean isGpsEnabled = false;
	private String panicPassword = null;
	private String number = null;
	private String seed = null;
	private String nick = null;
	private String publicKey = null;
	private String activationKey1 = null;
	private String activationKey2 = null;
	private String configFileName = null;
	
	private int panicNumber = 0;
	
	public ConfigEntity(String nick, String number, String seed, String publicKey, String activationKey1, String activationKey2) {
		super();
		this.nick = nick;
		this.number = number;
		this.seed = seed;
		this.publicKey = publicKey;
		this.activationKey1 = activationKey1;
		this.activationKey2 = activationKey2;
	}
	
	public ConfigEntity(String encAlgo, String hashedKey, boolean isGpsEnabled, String panicPassword, int panicNumber) {
		super();
		this.encAlgo = encAlgo;
		this.hashedKey = hashedKey;
		this.isGpsEnabled = isGpsEnabled;
		this.panicPassword = panicPassword;
		this.panicNumber = panicNumber;
	}
	public ConfigEntity() {}
	public String getEncAlgo() {
		return encAlgo;
	}
	public void setEncAlgo(String encAlgo) {
		this.encAlgo = encAlgo;
	}
	public String getHashedKey() {
		return hashedKey;
	}
	public void setHashedKey(String hashedKey) {
		this.hashedKey = hashedKey;
	}
	public boolean isGpsEnabled() {
		return isGpsEnabled;
	}
	public void setGpsEnabled(boolean isGpsEnabled) {
		this.isGpsEnabled = isGpsEnabled;
	}
	public String getPanicPassword() {
		return panicPassword;
	}
	public void setPanicPassword(String panicPassword) {
		this.panicPassword = panicPassword;
	}
	public int getPanicNumber() {
		return panicNumber;
	}
	public void setPanicNumber(int panicNumber) {
		this.panicNumber = panicNumber;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getActivationKey1() {
		return activationKey1;
	}

	public void setActivationKey1(String activationKey1) {
		this.activationKey1 = activationKey1;
	}

	public String getActivationKey2() {
		return activationKey2;
	}

	public void setActivationKey2(String activationKey2) {
		this.activationKey2 = activationKey2;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}
	
	
	
	
}