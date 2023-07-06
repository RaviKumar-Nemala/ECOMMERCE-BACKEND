package com.orders.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orders.module.Canceled_Order_Request;
import com.orders.module.Order_Items_Data_Send;
import com.orders.service.OrdersServiceInterface;

@RestController
@RequestMapping("orders")
public class Orders_Controller {

	@Autowired
	OrdersServiceInterface ordersServiceInterface;
	
	private String get_jwt ( Map<String, String> details )
	{
		System.out.println( details.size () ) ;
		
		if ( details.size() == 0 || details.containsKey("jwt_token") == false)
		{
			return null;
		}
		else 
		{
			return details.get("jwt_token");
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
	@PostMapping("/get_order_items")
	//jwt_ token which is in the post mapping will be stored into the map
	public ResponseEntity<List<Order_Items_Data_Send>> get_order_details( @RequestBody Map<String,String> details)throws Exception
	{
			String jwt_token = this.get_jwt(details);
			if ( jwt_token == null)
					return null;
			List<Order_Items_Data_Send>product_details = ordersServiceInterface.get_order_items(jwt_token);
			
			return new ResponseEntity<>(product_details, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
	@PostMapping("/cancel_order_items")
	public ResponseEntity<?>get_cancel_order_details ( @RequestBody  Canceled_Order_Request request)throws Exception
	{	
		boolean res = this.ordersServiceInterface.handle_canceled_order(request);
		
		if ( res )
		{
			return ResponseEntity.ok("ORDER_CANCELED_SUCCESSFULLY");
		}
		else 
		{
			return new ResponseEntity<>("SOMETHING WENT WRONG WHILE CANCELLING THE ORDER" , HttpStatus.BAD_REQUEST); 
		}	
	}
	
	@PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
	@PostMapping( "/get_canceled_items")
	public ResponseEntity<?>get_cancel_items(  @RequestBody Map <String  , String > details ) throws Exception
	{
		String jwt_token = get_jwt( details ) ;
		System.out.println ( "called");
		if ( jwt_token == null )
		{
			return new ResponseEntity < > ( "INVALID USER DETAILS" , HttpStatus.UNAUTHORIZED);
		}
	
		List<Order_Items_Data_Send>product_details =  this.ordersServiceInterface.get_canceled_orders(jwt_token);
		
		if ( product_details == null )
		{
			return new ResponseEntity<>("SOMETHING WENT WRONG CANNOT RETIVE CANCELED ORDER DETAILS" ,HttpStatus.BAD_REQUEST);
		}
		else 
		{
			return new ResponseEntity<> ( product_details , HttpStatus.OK);
		}
	}
}

