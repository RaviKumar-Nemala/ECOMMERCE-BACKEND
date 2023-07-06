package com.payments.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.module.Product_Order_Info;
import com.security_config.Custom_Response;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Repository
public class Payments_Dao implements PaymentsDaoInterface{

	@Autowired
	JdbcTemplate jdbc_tempate;
	
	@Autowired
	@Qualifier("dao_impl")
	User_Module_Dao_Impl user_dao;
	
	public static String PAYMENT_DETAILS_INSERTER = "insert into orders(order_id , payment_id , user_id) values(?,?,?);";
	
	public static String ORDER_ITEMS_INSERTER = "insert into order_items(order_id, product_id , quantity , price  ) values(?,?,? , ? );";
	
	public static String ORDER_KEY_EXTRACTOR ="select p_key from orders where user_id = '%d' and payment_id = '%s' ";
	
	//the value of the order table works as foreign key in the order details table 
	//need to extract the primary key in the order table and store that key as the foreign key in the order details table 
	private  int  extract_order_key( int user_id, String payment_id)
	{
	
		String _query = String.format(ORDER_KEY_EXTRACTOR, user_id,payment_id);
		
		Integer p_key = this.jdbc_tempate.queryForObject(_query , new Order_Key_Mapper( ) );
		
		if ( p_key != null )
		{
			return p_key ;
		}
		else 
			return -1;
		
	}
	
	// whenever the user purchases the multiple  products  at same time  then  this method has called to store thoe ordered items details in the db
	
	private Custom_Response store_order_items_details( int order_id ,  List<Product_Order_Info> data)
	{
		//order_id is the primary key of the orders table 
		
		Custom_Response cr = new Custom_Response();
		
		List<Object[]>total_args =  new ArrayList<>();
		
		for( var curr_prd_item :  data) {
			
			int product_id =curr_prd_item.getProduct_id();
			
			int quantity  = curr_prd_item.getQuantity();
			
			float price= curr_prd_item.getPrice();
			
			Object curr_args[ ] = { order_id , product_id , quantity , price};
			
			total_args.add(curr_args);
		}
		
		int []x = this.jdbc_tempate.batchUpdate(  ORDER_ITEMS_INSERTER , total_args ) ;
			
		cr.setMessage("SUCCESSFULLY INSERTED INTO ORDER ITMES");
		cr.setStatus(1);
		
		return cr;
	
	}

	//for the inserting the bulk of the ordered products 
	@Transactional
	public Custom_Response store_order_details ( String user_name  , String payment_id , List< Product_Order_Info> info)
	{
		
		Custom_Response cr  = new Custom_Response();

		int user_id  = 	this.user_dao.get_user_id(user_name);
		
		if ( info == null || info.size() == 0 )
		{
			cr.setMessage("INVALID PRODUCT_DETAILS ");
			return cr;
		}
		
		if( payment_id == null )
		{
			 cr.setMessage( "INVALID PAYMENT ID");
			 return cr;
		}
		
		if( user_id == -1)
		{
			cr.setMessage("INVALID USERNAME ");
			return cr;
		}
		
		String order_id = info.get(0).getOrder_id();
		
		this.store_payment_details(user_id , order_id,  payment_id);
		
		// after storing at the payment details at order table extact p_key of the inserted order
		//use this key as f.k for the order_items table
		int p_key  = this.extract_order_key(user_id, payment_id);
		
		this.store_order_items_details(p_key, info);
			
		cr.setMessage("ORDERED ITEMS INSERTED SUCCESSFULLY ");
		cr.setStatus(1);
		
		return cr;
	}
	
	@Transactional
	public Custom_Response store_order_details ( String user_name , String order_id, String payment_id, int product_id ,float total , int quantity )
	{
			
			Custom_Response  cr = new Custom_Response();
			try {
				
			int user_id = this.user_dao.get_user_id(user_name);
			
			if ( user_id == -1 )
				
			{
				throw new RuntimeException();
			}
			
			this.store_payment_details( user_id , order_id , payment_id ) ; 
		
			int p_key  = this.extract_order_key(user_id, payment_id);
			
			this.store_order_items_details  ( p_key ,  product_id , quantity ) ;
			cr.setMessage("ORDER DETAILS INSERTED SUCCESSFULLY");
			cr.setStatus(1);
			}
			catch ( Exception e )
			{
				System.out.println ( e.getMessage());
				cr.setStatus(0);
				cr.setMessage("SOMETHING WENT WRONG WHILE STORING THE ORDER DETAILS \n YOUR MONEY WILL BE REFUNDED WITH IN 3 DAYS");
			}
			 
			return cr;
	}
	
	private void  store_payment_details( int user_id, String order_id , String payment_id )
	{
		Object args [] =  {  order_id , payment_id , user_id};
		int res =  this.jdbc_tempate.update(PAYMENT_DETAILS_INSERTER  , args );	
		if ( res <= 0 )
		{
			throw new RuntimeException();
		}
		return;
	}
	
	//here order id refers to the primary key of the orders table 
	//order_items store the product_details of the ordered product of that specific user 
	private void  store_order_items_details( int order_id , int product_id  , int quantity)
	{
		Object args []  =  { order_id ,  product_id , quantity };
		int x = this.jdbc_tempate.update(  ORDER_ITEMS_INSERTER , args ) ;
		return;
	}
}
