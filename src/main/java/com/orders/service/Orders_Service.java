package com.orders.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt_service.Jwt_Service;
import com.orders.dao.OrdersDaoInterface;
import com.orders.module.Canceled_Order_Request;
import com.orders.module.Order_Items_Data_Send;
import com.orders.module.Order_Items_Total_Data;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Total_Data;
import com.products_service.Products_Get_Service;
import com.security_config.Custom_Response;
import com.products_service.ProductsServiceInterface;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Service
public class Orders_Service implements OrdersServiceInterface{

	@Autowired
	Jwt_Service jwt_service;
	
	@Autowired
	OrdersDaoInterface ordersDaoInterface;
	
	@Autowired
	ProductsServiceInterface products_service_interface;
	
	@Autowired
	User_Module_Dao_Impl user_dao ; 
	
	private List<Order_Items_Data_Send>wrap_product_data  ( List<Order_Items_Total_Data> data )
	{
		List<Order_Items_Data_Send>products_data = new ArrayList<>();
		
		for( Order_Items_Total_Data ord_item :  data )
		{
			Products_Total_Data x = ord_item;
			
			List<Products_Data_Send> prdt_data_send = this.products_service_interface.send_products_data( Arrays.asList(x));
			
			if ( prdt_data_send.size( ) == 0 )
			{
				//ignore that product
			}
			else {
			Order_Items_Data_Send  ord_data_send = new Order_Items_Data_Send(prdt_data_send.get(0));
			
			ord_data_send.setOrder_date(ord_item.getOrder_date());
			
			ord_data_send.setOrder_time(ord_item.getOrder_time());
			
			products_data.add( ord_data_send);
			}
		}
		
		return products_data;
	}
	
	public List<Order_Items_Data_Send>  get_order_items( String jwt_token )throws Exception
	{	
		String user_name = jwt_service.get_username(jwt_token);	
		List<Order_Items_Total_Data> product_data = this.ordersDaoInterface.get_order_items(user_name);
		return this.wrap_product_data(product_data);
	}

	public boolean  handle_canceled_order( Canceled_Order_Request  cancel_ord_req )
	{
			String jwt_token = cancel_ord_req.getJwt_token();
			String order_date = cancel_ord_req.getOrder_date();
			String order_time = cancel_ord_req.getOrder_time();
			String product_uuid =  cancel_ord_req.getProduct_uuid();
			String user_name =  this.jwt_service.get_username(jwt_token);
			
			Custom_Response cr = this.ordersDaoInterface.store_canceled_order( user_name ,product_uuid ,  order_date , order_time);
			
			if ( cr.isOk())
			{
				return true;
			}
			else 
			{
				return false;
			}
	}
	public List<Order_Items_Data_Send> get_canceled_orders ( String jwt_token )
	{
			String user_name  = this.jwt_service.get_username(jwt_token);
			List<Order_Items_Total_Data>details = this.ordersDaoInterface.get_caceled_orders(user_name);
			
			if ( details == null )
			{
				return null;
			}
			else 
				return this.wrap_product_data(details);
	}
}
