package com.orders.service;

import java.util.List;

import com.orders.module.Canceled_Order_Request;
import com.orders.module.Order_Items_Data_Send;
import com.orders.module.Order_Items_Total_Data;
import com.security_config.Custom_Response;

public interface OrdersServiceInterface {

	List<Order_Items_Data_Send>get_order_items(String jwt_token)throws Exception;;

	boolean handle_canceled_order(Canceled_Order_Request   request);
	
	List<Order_Items_Data_Send>get_canceled_orders(String jwt_token);
	
}
