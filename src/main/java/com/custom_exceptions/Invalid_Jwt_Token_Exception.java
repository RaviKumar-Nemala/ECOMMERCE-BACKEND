package com.custom_exceptions;

public class Invalid_Jwt_Token_Exception extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message ; 
	
	public Invalid_Jwt_Token_Exception() {
		super ();
	} 
	
	public Invalid_Jwt_Token_Exception ( String message )
	{
		super ( message );
	}
	
	
}
