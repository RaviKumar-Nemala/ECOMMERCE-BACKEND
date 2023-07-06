package com.carts_module;

import org.springframework.stereotype.Component;

@Component("carts_data")
public class Carts_Data {

	private int user_id ;

	private int product_id ;

	private int quantity ;

	private float total;

	private String user_id_product_id ;

	public String getUser_id_product_id() {
		return user_id_product_id;
	}


	public void setUser_id_product_id(String user_id_product_id) {
		this.user_id_product_id = user_id_product_id;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}



}
