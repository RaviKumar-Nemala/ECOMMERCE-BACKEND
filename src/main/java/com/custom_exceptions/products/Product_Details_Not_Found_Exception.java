package com.custom_exceptions.products;

public class Product_Details_Not_Found_Exception  extends RuntimeException
{

	public Product_Details_Not_Found_Exception( String message ) 
	{
		super ( message);
	}
	
	public Product_Details_Not_Found_Exception()
	{
		
	}
	
}
