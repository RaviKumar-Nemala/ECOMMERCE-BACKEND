package com.custom_exceptions;

public class Invalid_User_Details_Exception extends Exception {

	
	
	public Invalid_User_Details_Exception() {
		
	}
	
	public Invalid_User_Details_Exception (String msg)
	{
		super(msg);
	}
}
