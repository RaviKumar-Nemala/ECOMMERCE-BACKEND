package com.orders.module;

import com.products_module.Products_Data_Send;

public class Order_Items_Data_Send extends Products_Data_Send{

	String order_date ;
	
	String order_time;
	
	public Order_Items_Data_Send( Products_Data_Send pr )
	{
		super( pr);
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	
	
}
