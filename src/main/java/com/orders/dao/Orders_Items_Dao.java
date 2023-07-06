package com.orders.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.custom_exceptions.products.Product_Details_Not_Found_Exception;
import com.orders.module.Order_Items_Total_Data;
import com.products_dao.Products_Dao;
import com.security_config.Custom_Response;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Repository
public class Orders_Items_Dao implements OrdersDaoInterface{
	
		public static String ORDER_ITEMS_FETCH = "select  pr.product_id ,  pr.product_name , pr.price , pr.product_specifications ,  pr.category_id , pr.product_uuid , pr.gender,pr.stock,pr.product_image_type,  or_it.ord_date,or_it.ord_time   from products as pr ,  orders as ord , order_items as or_it where ord.user_id = %d and  ord.p_key = or_it.order_id and or_it.product_id = pr.product_id;";
		
		public static String ORDER_ID_EXTRACTOR = "select  or_it.order_id  from order_items as or_it , orders as ord where or_it.product_id = %d  and ord.user_id = %d and ord.p_key =  or_it.order_id  and or_it.ord_date = '%s' and or_it.ord_time = '%s';";
		
		public static String CANCEL_ORDER_INSERTER ="insert into canceled_orders  (order_id,  product_id) values ( ? , ? )";
		
		public static String ORDER_ITEM_DELETER  = "delete from order_items where order_id = %d ";
		
		public static String CANCEL_ORDERS_GETTER = "select  pr.product_id ,  pr.product_name , pr.price , pr.product_specifications ,  pr.category_id , pr.product_uuid , pr.gender,pr.stock,pr.product_image_type , can_ord.ord_time ,  can_ord.ord_date   from products as pr ,  canceled_orders as can_ord  ,  orders as ord where can_ord.order_id =  ord.p_key   and  ord.user_id =  %d and can_ord.product_id = pr.product_id;";
		
		@Autowired
		Products_Dao products_dao;
		
		@Autowired
		JdbcTemplate jdbc_template;
		
		@Autowired
		User_Module_Dao_Impl user_dao;
		
		public List<Order_Items_Total_Data> get_order_items( String user_name )
		{
			try {
			int user_id = user_dao.get_user_id(user_name);
			if ( user_id == -1 )
			{
				throw new Exception () ;
			}
			List<Order_Items_Total_Data> details = new ArrayList<>();
			String x = String.format( ORDER_ITEMS_FETCH , user_id);
			details =  this.jdbc_template.query(x, new Order_Items_Mapper());
			return details;
			}
			catch ( Exception e )
			{
				throw new Product_Details_Not_Found_Exception();
			}
		}
		
		//if incase any exception has raised during this process
		// called function is defined with the transactional annotation
		//it rollbacks all the operations
		public void delete_canceled_order ( int order_id)
		{
				String query = String.format( ORDER_ITEM_DELETER,  order_id );		
				int res = this.jdbc_template.update(query);
		}
		
		@Transactional
		public Custom_Response   store_canceled_order( String user_name , String product_uuid , String order_date , String order_time )
		{	
			Custom_Response cr = new Custom_Response () ;
			
			int user_id = this.user_dao.get_user_id(user_name);
			
			if ( user_id == -1 )
			{
				return cr;
			}
			
			int product_id = this.products_dao.get_product_id(product_uuid);
			
			String x = String.format(ORDER_ID_EXTRACTOR, product_id , user_id , order_date , order_time);
			
			List<Integer> order_id = this.jdbc_template.query(x , new Order_Id_Mapper());		
			
			try {
				//store canceled items in order_canceled_table
				
				Object args[] = { order_id.get(0) ,  product_id } ;
				
				int res = this.jdbc_template.update(CANCEL_ORDER_INSERTER, args );
				
				//then detele the same item from order_items table
				
				this.delete_canceled_order(order_id.get(0));
				
			}
			
			catch(Exception e )
			{
				System.out.println ( e.getMessage () );
				cr.setMessage("SOMETHING WENT WRONG WHILE CANCLEING THE ORDER \n PLEASE TRY AFTER SOME TIME");
				cr.setStatus(0);
			}
			
			return cr;
		}
		
		public List<Order_Items_Total_Data>get_caceled_orders( String user_name)
		{
			   int user_id = this.user_dao.get_user_id(user_name);
			   
			   	if ( user_id == -1 )
			   	{
			   		return  null;
			   	}
			   	String x  = String.format ( CANCEL_ORDERS_GETTER ,  user_id) ; 
			   	List<Order_Items_Total_Data> products_data = this.jdbc_template.query( x , new Order_Items_Mapper());
			
			   	if( products_data == null || products_data.size() == 0 )
			   	{
			   		return null;
			   	}
			   	else 
			   	{
			   		return products_data;
			   	}
		}
}
