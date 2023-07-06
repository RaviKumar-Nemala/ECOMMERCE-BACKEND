package com.custom_exceptions.products;

public class Invalid_Product_Uuid_Exception   extends RuntimeException{

	
	public Invalid_Product_Uuid_Exception ()
	{
		
	}
	
	public Invalid_Product_Uuid_Exception( String message) {
		
		super ( message );
	}
	
}
