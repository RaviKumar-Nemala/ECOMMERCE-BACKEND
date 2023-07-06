package com.carts_module.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carts_module.Carts_Data_Sender;
import com.carts_module.service.CartsServiceInterface;
import com.carts_module.service.Carts_Service;
import com.jwt_service.Jwt_Service;
import com.security_config.Custom_Response;

@RestController
@CrossOrigin
public class Carts_Controller {

	@Autowired
	@Qualifier("carts_service")
	private CartsServiceInterface cartsServiceInterface;

	@Autowired
	private Custom_Response custom_response;

	@Autowired
	private Jwt_Service  jwt_service;

	final String CARTS_DATA_INSERTION_SUCCESSFUL = "SUCCESSFULLY INSERTED INTO CARTS TABLE";

	final String CARTS_DATA_INSERTION_FAILURE = "FAILURE WHILE INSERTING INTO CARTS";

	public ResponseEntity<String> send_response ( Custom_Response custom_response )
	{

		String message  = custom_response.getMessage();
		if ( custom_response.isOk())
		{
			return ResponseEntity.ok( message );
		}
		else
		{
			 message =  this.custom_response.getMessage();
			return new ResponseEntity<>( message , HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/add_to_cart" ,  method = RequestMethod.POST)
	public ResponseEntity<String> add_to_carts (  @RequestBody(required = true) Carts_Receiver_Data_1 carts_data)
	{
		this.custom_response  = this.cartsServiceInterface.insert_into_carts( carts_data);
		return ResponseEntity.ok( this.custom_response.getMessage());
	}

	//deletes only one product based on the given product_id
	@DeleteMapping("/remove_cart_item")
	public ResponseEntity<String> remove_cart_item ( @RequestBody Carts_Removal_Data carts_removal_data)
	{

		this.custom_response = this.cartsServiceInterface.remove_item_from_cart(carts_removal_data);
		return send_response(custom_response);
	}
	
	@PutMapping("/update_quantity")
	public  ResponseEntity<?> update_quantity_val ( @RequestBody Quantity_Updater_Data quantity_updater)
	{
		String jwt_token  = quantity_updater.getJwt_token() ;
		
		String product_uuid = quantity_updater.getProduct_uuid();
		
		int quantity_val = quantity_updater.getQuantity_val();
		
		if  ( jwt_token == null  || product_uuid == null ||  quantity_val <=0 )
		{
			 String msg = "invalid details" ; 
			 return new ResponseEntity< > (msg , HttpStatus.BAD_REQUEST);
		}
		else 
		{	
			Custom_Response cr = this.cartsServiceInterface.update_quantity_val(jwt_token, product_uuid, quantity_val);
			
			if ( cr.isOk() )
			{
				 return ResponseEntity.ok(cr.getMessage());
			}
			else 
			{ 
				return ResponseEntity.badRequest().body(cr.getMessage());
			}
		}
	}
	
	@DeleteMapping("/remove_all_cart_item/{username}")
	public String remove_all_items ( @PathVariable(required = true , value="username") String  user_name )
	{

		String message ;
		boolean delete_status =  this.cartsServiceInterface.remove_all_cart_items(user_name);
		if ( delete_status )
		{
			message = "successfully deleted all records of speicified user from carts";
		}
		else
		{
			message ="failure while deleting the records from carts";
		}
		return message;
	}

	@GetMapping("/get_carts_data")
	public ResponseEntity<List< Carts_Data_Sender> >  get_carts_data (  @RequestParam(value = "jwt_token") String jwt_token )
	{
		boolean jwt_validation_res  = this.jwt_service.validate_jwt( jwt_token );
		String username = this.jwt_service.get_username(jwt_token);
		System.out.println ( "INSIDE OF THE GET_CARTS  "+username);
		if ( !jwt_validation_res )
		{
			return ResponseEntity.badRequest().body  ( null);
		}
		else{
		List< Carts_Data_Sender > carts_data = this.cartsServiceInterface.get_cart_item(username);
		return ResponseEntity.ok( carts_data);
		}
	}
}
