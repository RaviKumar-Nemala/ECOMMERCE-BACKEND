package com.custom_exceptions;


public class Jwt_Expired_Exception extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	String message ; 
	
	public Jwt_Expired_Exception () 
	{
		super ();
	}	
	
	public Jwt_Expired_Exception ( String message ) 
	{
			super ( message ) ;
	}
}
