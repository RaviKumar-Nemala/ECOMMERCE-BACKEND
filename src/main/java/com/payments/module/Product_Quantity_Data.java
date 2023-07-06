package com.payments.module;

public class Product_Quantity_Data {
	
	private String uuid ;
	
	private int quantity;
	
	public Product_Quantity_Data( String uuid , int quantity)
	{
		this.uuid = uuid;
		this.quantity = quantity;
	}

	public String getUuid() {
		return uuid;
	}

	public int getQuantity() {
		return quantity;
	}
	
	
}
