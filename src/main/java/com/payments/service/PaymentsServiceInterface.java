package com.payments.service;

import java.util.List;

import com.payments.module.Payment_Request;
import com.razorpay.Order;

public interface PaymentsServiceInterface {

	void remove_payment_details(String jwt_token);
	
	Order request_for_payment(List<Payment_Request>details);
	
	boolean validate_payment_details( String payment_id , String jwt_token , String signature);
	
}
