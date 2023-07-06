package com.carts_module.controller;

public class Quantity_Updater_Data {
	
	public String product_uuid;
	
	public String jwt_token;
	 
	int quantity_val ;
	
	Quantity_Updater_Data() 
	{
		
	}

	public String getProduct_uuid() {
		return product_uuid;
	}

	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}

	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}

	public int getQuantity_val() {
		return quantity_val;
	}

	public void setQuantity_val(int quantity_val) {
		this.quantity_val = quantity_val;
	}
}
