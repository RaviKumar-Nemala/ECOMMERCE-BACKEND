package com.payments.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jwt_service.Jwt_Service;
import com.payments.dao.PaymentsDaoInterface;
import com.payments.module.Payment_Request;
import com.payments.module.Product_Order_Info;
import com.payments.module.Product_Quantity_Data;
import com.products_module.Products_Total_Data;
import com.products_service.Products_Get_Service;
import com.products_service.ProductsServiceInterface;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.security_config.Custom_Response;

@Service
public class Payments_Service_2 implements  PaymentsServiceInterface {
	
	@Value("${payments.api_key}")
	private String api_key;
	
	@Value("${payments.secret_key}")
	private String secret_key;
	
	private Map<String, List<Product_Order_Info>>user_order_map= new HashMap<>();
	
	@Autowired
	private Payment_Signature payment_signature; 
	
	//used for finding how much quantity that the user added for the specific product
	private Map< String , List<Product_Quantity_Data> > quantity_finder = new HashMap<>();
	
	@Autowired
	private Jwt_Service jwt_service;
	
	@Autowired
	private ProductsServiceInterface products_service_interface;
	
	@Autowired
	private  PaymentsDaoInterface paymentsDaoInterface;
	
	private void remove_order_details_from_cache( String user_name )
	{
		this.user_order_map.remove( user_name ) ;
		this.quantity_finder.remove( user_name);
		return ;
	}
	
	public  void remove_payment_details ( String jwt_token )
	{
		String user_name = this.jwt_service.get_username(jwt_token);
		
		if ( user_name == null )
			return ;
		else 
		remove_order_details_from_cache(  user_name );
	}
	
	public List<Products_Total_Data > get_product_details( List<String>uuids) 
	{
		return products_service_interface.get_products( uuids);
	}
	
	//store the  order details which is used for further validation 
	// after successful validation  these details are stored in the db
	private  void store_requested_order_details (String user_name, String order_id , List<Payment_Request> payment_request , List<Products_Total_Data> prd_details)
	{
		List< Product_Order_Info > prd_ord_info_list = new ArrayList<>();
		List< Product_Quantity_Data >  quantity_info = this.quantity_finder.get( user_name);
		
		for( var curr_prd :  prd_details )
		{
			int product_id = curr_prd.getProduct_id();
			
			String uuid = curr_prd.getProduct_uuid();
			
			int user_entered_quantity =  0;
					
			for( var quantity_obj : quantity_info )
			{
				if ( quantity_obj.getUuid().equals(uuid))
				{
					user_entered_quantity = quantity_obj.getQuantity();
					break;
				}
			}
			
			if( user_entered_quantity <= 0 )
			{
				continue;
			}
			
			int price  = (int)curr_prd.getPrice() * user_entered_quantity;
			
			Product_Order_Info  prd_ord_info = new Product_Order_Info
												(
													product_id , 
													order_id,
													price ,
													user_entered_quantity
												);
												
			prd_ord_info_list.add( prd_ord_info);
			
		}
		
		user_order_map.put(  user_name , prd_ord_info_list);
		
		return;
	}
	
	private Custom_Response store_details_in_db( String user_name , String payment_id ){
		Custom_Response cr = new Custom_Response() ;
		List<Product_Order_Info> data =  this.user_order_map.get(user_name);
		
		if( data == null )
		{
			cr.setMessage(" INVALID  USERNAME ");
			return cr;
		}
		
		try {
		cr = this.paymentsDaoInterface.store_order_details(user_name, payment_id, data);
		this.remove_order_details_from_cache( user_name);
		}
		catch( Exception e )
		{
			cr.setMessage("SOMETHING WENT WRONG WHILE  INSERTING THE ORDER DETAILS");
			cr.setStatus(0);
		}
		
		return cr;
	}
	
	public Order request_order( String user_name , int amount, List<Payment_Request> payment_request , List<Products_Total_Data> prd_details)
	{ 
		RazorpayClient client = null; 
		try {
		 client = new RazorpayClient(api_key , secret_key );
		}
		catch ( Exception  e )
		{
			System.out.println ( e.getMessage());
			return null;
		}
		
		System.out.println ( client.toString() );
			
		// razorypay accepts the amount in the least currency format
		// i.e in piases so need to multiply with 100 get the amount in paise format
			amount = amount * 100;
		try {
			System.out.println("INSIDE OF TRY");
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount) ; // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");
			Order order_check = client.orders.create(orderRequest);
			
			this.store_requested_order_details(user_name, order_check.get("id") , payment_request, prd_details);
			
			System.out.println ( order_check );
			
			return order_check;
		  }
			
		catch ( Exception e )
		
		{
			System.out.println ( e.getMessage());
			
			return null;
		}
	}
	
	public Order request_for_payment( List<Payment_Request> payment_request)
	{
		
		if ( payment_request.size() == 0 )
				return  null;
	
		String jwt_token  = payment_request.get( 0  ) .getJwt_token() ;
		
		String user_name = jwt_service.get_username(jwt_token);
		
		if( user_name == null )
				return null;
		
		List< String >uuids = new ArrayList<>();
		
		//for getting the uuids list 
		//also store the entered quantity for the each product
		quantity_finder.put( user_name , new ArrayList<>());
		
		for( var  curr_obj : payment_request)
		{
			if ( curr_obj != null) {
				String uuid = curr_obj != null ? curr_obj.getProduct_uuid().trim() : null;
				
				int  quantity = curr_obj.getQuantity();
				
				uuids.add( uuid );
				
				List<Product_Quantity_Data>quantity_info = quantity_finder.get(user_name);
				
				quantity_info.add(new Product_Quantity_Data(uuid, quantity));
				
				quantity_finder.put(user_name,quantity_info);
			}
		}
		
		List<Products_Total_Data>prd_data   = this.get_product_details( uuids);
		
		int total_price = 0 ;
		
		for( Products_Total_Data curr_prd :  prd_data)
		{
		
			String uuid =  curr_prd.getProduct_uuid();
			if ( !quantity_finder.containsKey(user_name)) {
				continue;
			}
			
			List<Product_Quantity_Data> quantity_data = quantity_finder.get(user_name);
		
			int user_entered_quantity = 0;
			
			for( var quantity_obj :  quantity_data)
			{
				 if (  uuid.equals(quantity_obj.getUuid()))
				 {
					 user_entered_quantity =  quantity_obj.getQuantity();
					 break;
				 }
			}
			
//			int user_entered_quantity =  quantity_finder.get(curr_prd.getProduct_uuid());
			
			//quantity value should be atleast 1 otherwise  price value becomes zero;
			if( user_entered_quantity <= 0 )
			{
				continue;
			}
			
			int prd_price = (int) curr_prd.getPrice();
			
			//need to findout how much quantity speicified by the user for each product
			
			prd_price = prd_price *  user_entered_quantity;
			
			total_price  += prd_price;
		}
		
		
		return this.request_order(user_name, total_price, payment_request, prd_data);
	}

	public boolean validate_payment_details ( String payment_id, String jwt_token, String client_signature )
	{
		String user_name =  jwt_service.get_username(jwt_token);
		
		List<Product_Order_Info> prd_ord_info = this.user_order_map.get(user_name);
		
		if( prd_ord_info == null)
		{
			return false;
		}
		
		//order id is same for the all the products
		String order_id =  prd_ord_info.get(0).getOrder_id();
		
		boolean res = false; 
		
		try 
		{
			String val = order_id + "|" + payment_id;
			
			String server_signature = this.payment_signature.calculateRFC2104HMAC(val , secret_key);
			
			if ( client_signature.equals(server_signature))
			{
			
				Custom_Response cr = this.store_details_in_db( user_name , payment_id);
				
				if( cr.isOk())
				{
					return true;
				}
				else 
				{
					return false;
				}
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
	
}
