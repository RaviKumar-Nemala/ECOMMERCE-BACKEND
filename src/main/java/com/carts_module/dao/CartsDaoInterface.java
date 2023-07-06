package com.carts_module.dao;

import java.util.List;

import com.carts_module.Carts_Data;
import com.carts_module.controller.Carts_Receiver_Data_1;
import com.security_config.Custom_Response;

public interface CartsDaoInterface {

	Custom_Response update_quantity_val( String user_name ,String product_uuid, int quantity);

	List < Carts_Data > get_cart_items(String username);
	
	Custom_Response  add_to_cart(Carts_Data carts_insertion_data);

	Custom_Response remove_cart_item(int user_id, int product_id);
	
	boolean remove_all_cart_items(int user_id);
	
}
