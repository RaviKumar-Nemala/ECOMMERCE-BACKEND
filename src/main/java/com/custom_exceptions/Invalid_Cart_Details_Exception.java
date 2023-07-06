package com.custom_exceptions;

public class Invalid_Cart_Details_Exception  extends  RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String message ;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
	public Invalid_Cart_Details_Exception(  String message ) {
	
		super ( message );
		
	}
	
	public Invalid_Cart_Details_Exception ()
	{
		
	}
}
