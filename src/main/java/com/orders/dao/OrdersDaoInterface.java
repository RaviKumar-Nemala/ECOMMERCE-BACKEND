package com.orders.dao;

import java.util.List;

import com.orders.module.Order_Items_Total_Data;
import com.security_config.Custom_Response;

public interface OrdersDaoInterface {

	List<Order_Items_Total_Data> get_order_items(String user_name);
	
	Custom_Response store_canceled_order( String user_name ,String product_uuid , String order_date , String order_time);

	List<Order_Items_Total_Data> get_caceled_orders(String user_name);
	
}
