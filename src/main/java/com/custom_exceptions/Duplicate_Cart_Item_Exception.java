package com.custom_exceptions;

public class Duplicate_Cart_Item_Exception extends RuntimeException{

	/**
	 * 
	 */
	String message  ; 
	
	
	private static final long serialVersionUID = 1L;
	
	public Duplicate_Cart_Item_Exception() {
		
	} 
	
	public Duplicate_Cart_Item_Exception ( String message )
	{
		super(  message );
		
	}
	
	
}	
