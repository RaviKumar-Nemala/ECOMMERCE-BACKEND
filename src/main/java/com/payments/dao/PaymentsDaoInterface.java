package com.payments.dao;

import java.util.List;

import com.payments.module.Product_Order_Info;
import com.security_config.Custom_Response;

public interface PaymentsDaoInterface {
	
	Custom_Response store_order_details(String user_name,String payment_id, List<Product_Order_Info> data);
	
}
