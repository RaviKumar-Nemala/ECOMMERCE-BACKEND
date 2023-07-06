package com.carts_module.controller;



public class Carts_Receiver_Data_1 {

	String jwt_token;

	String product_uuid ;

	float total ;

	int quantity ;

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

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
