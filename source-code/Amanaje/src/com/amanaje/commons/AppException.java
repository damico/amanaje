package com.amanaje.commons;

public class AppException extends Exception {

	private static final long serialVersionUID = -9217036014255718209L;
	
	public AppException(){
		super();
	}
	
	public AppException(Exception e){
		super(e);
	}
	
	public AppException(String message){
		super(message);
	}
	
	public AppException(String message, Exception e){
		super(message, e);
	}

}
