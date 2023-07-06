package com.carts_module.service;

import java.util.List;

import com.carts_module.Carts_Data_Sender;
import com.carts_module.controller.Carts_Receiver_Data_1;
import com.carts_module.controller.Carts_Removal_Data;
import com.security_config.Custom_Response;

public interface CartsServiceInterface {

	Custom_Response insert_into_carts( Carts_Receiver_Data_1 data );
	
	Custom_Response remove_item_from_cart(Carts_Removal_Data  carts_removal_data);

	Custom_Response update_quantity_val(String jwt_token, String product_uuid, int quantity_val);

	boolean remove_all_cart_items(String user_name);

	List< Carts_Data_Sender > get_cart_item(String username);

}
