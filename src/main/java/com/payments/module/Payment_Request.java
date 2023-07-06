package com.payments.module;



public class Payment_Request {
	
	String jwt_token ;
	
	String product_uuid;
	
	int quantity;
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}

	public String getProduct_uuid() {
		return product_uuid;
	}

	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}
	
	
	
}
