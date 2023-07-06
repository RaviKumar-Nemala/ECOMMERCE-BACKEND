package com.payments.module;

public class Product_Order_Info {
	
	int product_id; 
	
	String order_id ;
	
	float price ;
	
	int quantity;
	
	public Product_Order_Info()
	{
		
	}

	public float getPrice() {
		return price;
	}


	public int getQuantity() {
		return quantity;
	}


	public int getProduct_id() {
		return product_id;
	}

	
	public String getOrder_id() {
		return order_id;
	}
	
	public Product_Order_Info( int product_id, String order_id ,  float price , int  quantity)
	{
		this.product_id =product_id ;
		this.order_id = order_id;
		this.price = price;
		this.quantity = quantity;
	}
	
}
