package com.payments.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jwt_service.Jwt_Service;
import com.payments.dao.Payments_Dao;
import com.payments.module.Payment_Request;
import com.payments.module.Product_Order_Info;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Total_Data;
import com.products_service.Products_Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.security_config.Custom_Response;

@Service
public class Payments_Service {

	@Autowired
	private Jwt_Service jwt_service;
	
	@Autowired
	private Products_Service  products_service;
	
	@Autowired
	private Payment_Signature  payment_signature;
	
	@Autowired
	private Payments_Dao  payments_dao;
	
	@Value("${payments.api_key}")
	String api_key;
	
	@Value("${payments.secret_key}")
	String secret_key;
	
	private Map<String, Product_Order_Info> my_map = new HashMap<>();
	
	void store_in_map ( String user_name , Product_Order_Info  info)
	{
			this.my_map.put ( user_name,  info );
			
			return;
	}
	
	void store_in_map( String user_name, List<Product_Order_Info> info )
	{
		 return;
	}
	
	
	String get_order_id ( String user_name )
	{
		if(  my_map.containsKey(user_name))
		{
			return my_map.get(user_name).getOrder_id();
		}
		
		// for multiple items or bulk order
//		return my_map.get(user_name).at(0).getOrder_id();
		
		return null;
	}
	
	public boolean store_order_in_db (  String user_name , String payment_id)
	{
			var  data =  my_map.get(user_name );
			
			int product_id = data.getProduct_id();
		   
			String order_id = data.getOrder_id();
			
			float price = data.getPrice();
			
			int quantity = data.getQuantity();
			
			Custom_Response cr = payments_dao.store_order_details(user_name, order_id, payment_id, product_id , price , quantity);
		   		   
		   if ( cr.isOk())
		   {
			   return true ;
		   }
		   else 
		   {
			   return false;
		   }
	}
	
	public void remove_from_map( String user_name )
	{
		if(this.my_map.containsKey(user_name))
		{
			my_map.remove(user_name);
		}
		return;
	}
	
	
	public boolean validate_payment_details ( String payment_id, String jwt_token, String client_signature )
	{
		String user_name =  jwt_service.get_username(jwt_token);
		
		String order_id =  this.get_order_id(user_name);
		
		if ( order_id == null )
		{
			return false;
		}
		
		boolean res = false ;
		
		try 
		{
			String val = order_id + "|" + payment_id;
			
			String server_signature = this.payment_signature.calculateRFC2104HMAC(val , secret_key);
			
			if ( client_signature.equals(server_signature))
			{
				
				boolean x = store_order_in_db( user_name , payment_id );
				
				remove_from_map ( user_name );
				if( x == true )
					res = true ;
			}
			else 
			{
				res = false ;
			}
		}
		catch ( Exception e )
		{
			res = false; 
		}
		
		return res; 
	}
	
	public Order request_order ( Payment_Request details )
	{
		
		String user_name = jwt_service.get_username( details.getJwt_token());
		
		Products_Data_Send product_data =  products_service.get_product(details.getProduct_uuid());
		
		int price = (int)Math.ceil(product_data.getPrice());
		
		price = price * 100 ;
		
		int quantity =  details.getQuantity();
		
		//quantity value atleast 1 
		if( quantity < 1 )
		{
			return null;
		}
		
		else if ( quantity >= 1 )
		{
			price = price * quantity;
		}
		
		return this.request_order( price , user_name , details.getProduct_uuid() , details.getQuantity());
		
	}
	
	public void store_details ( String user_name, String order_id , String uuid , int price ,  int quantity)
	{
		Products_Total_Data data = products_service.get_product_total_data(uuid);
		
		int product_id = data.getProduct_id();
		
		Product_Order_Info info = new Product_Order_Info ( product_id  , order_id ,  price , quantity ) ;
		
		store_in_map( user_name,  info );
		
	}
	
	public Order request_order ( int amount , String user_name , String uuid , int quantity )
	{
		RazorpayClient client = null; 
		try {
		 client = new RazorpayClient(api_key , secret_key );
		}
		catch ( Exception  e )
		{
			
			System.out.println ( e.getMessage());
		}
		
		System.out.println ( client.toString() );
			
		
		try {
			System.out.println("INSIDE OF TRY");
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");
			Order order_check = client.orders.create(orderRequest);
			
			System.out.println ( order_check );
			
			this.store_details(user_name, order_check.get("id") , uuid  , amount , quantity );
			
			return order_check;
		  }
		catch( Exception e )
		{
			System.out.println ( e.getMessage());
			return null;
		}

	}
}
