package com.amanaje.entities;

public class CryptoAlgoEntity {
	
	private String algoName = null;
	private String algoInstance = null;
	private int ivLength = 0;
	public String getAlgoName() {
		return algoName;
	}
	public void setAlgoName(String algoName) {
		this.algoName = algoName;
	}
	public String getAlgoInstance() {
		return algoInstance;
	}
	public void setAlgoInstance(String algoInstance) {
		this.algoInstance = algoInstance;
	}
	public int getIvLength() {
		return ivLength;
	}
	public void setIvLength(int ivLength) {
		this.ivLength = ivLength;
	}
	public CryptoAlgoEntity(String algoName, String algoInstance, int ivLength) {
		super();
		this.algoName = algoName;
		this.algoInstance = algoInstance;
		this.ivLength = ivLength;
	}
	
	
	
}