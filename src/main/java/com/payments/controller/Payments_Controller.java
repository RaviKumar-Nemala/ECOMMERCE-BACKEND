package com.payments.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payments.module.Payment_Request;
import com.payments.module.Payment_Validataion_Details;
import com.payments.service.Payments_Service;
import com.payments.service.PaymentsServiceInterface;
import com.razorpay.Order;

@RestController
@CrossOrigin
public class Payments_Controller {

	
	@Autowired
	private PaymentsServiceInterface paymentsServiceInterface;
	
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@DeleteMapping("/payments/remove_payment_details")
	public ResponseEntity<?> remove_payment_details( @RequestBody Map<String, String> details)
	{
		if ( details== null  ||  !details.containsKey("jwt_token"))
		{
			return ResponseEntity.badRequest().body("JWT_TOKEN NOT FOUND");
		}
		
		String jwt_token =  details.get("jwt_token");
		
		this.paymentsServiceInterface.remove_payment_details(jwt_token);
	
		return ResponseEntity.ok("DETAILS REMOVED SUCCESSFULLY");
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping ( "/payments/bulk_request")
	public ResponseEntity<?>initiliaze_bulk_payments(  @RequestBody List<Payment_Request> details )
	{
		
		Order order = this.paymentsServiceInterface.request_for_payment(details);
		System.out.println(order);
		if ( order != null )
		{
			return new ResponseEntity<>( order.toString() ,  HttpStatus.OK);
		}
		else 
		{
			String msg = "ERROR ENCOUNTERED WHILE PLACING THE ORDER";
			return new ResponseEntity<>( msg , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/payments/validate_payment_details")
	public ResponseEntity<?> validate_payment_details ( @RequestBody Payment_Validataion_Details payment_details)
	{
		
		String payment_id = payment_details.getPayment_id();
		
		String signature = payment_details.getSignature();
		
		String jwt_token = payment_details.getJwt_token();
		
		if ( payment_id == null|| signature == null || jwt_token == null )
		{
			return  new ResponseEntity<>( "INVALID PAYMENT DETAILS ORDER NOT BEEN PLACED" , HttpStatus.BAD_REQUEST);
		}
		
		boolean res = this.paymentsServiceInterface.validate_payment_details( payment_id , jwt_token , signature);
		
		if ( res ) 
		{
			return new ResponseEntity<>("ORDER PLACED SUCESSFULLY" ,  HttpStatus.OK);
		}
		else 
		{
			return  new ResponseEntity<>( "INVALID PAYMENT DETAILS ORDER NOT BEEN PLACED" , HttpStatus.BAD_REQUEST);
		}
		
	}
}
