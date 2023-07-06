package com.products_service;

import java.util.List;

import com.payments.module.Product_Update_Data;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Input_Data;
import com.products_module.Products_Total_Data;
import com.security_config.Custom_Response;

public interface ProductsServiceInterface {
	
	Custom_Response update_product( Product_Update_Data  update_data);
	
	Custom_Response insert_singe_record( Products_Input_Data input_data);
	
	List<Products_Data_Send> get_products ();
	
	byte[] get_product_image(   String uuid );
	
	Products_Data_Send get_product( String uuid);
	
	byte[]  get_product_side_image( String uuid);
	
	List<Products_Data_Send>send_products_data( List<Products_Total_Data> total_info);
	
	Products_Data_Send  get_product( int product_id );
	
	List<Products_Data_Send> get_category_products( String  category_name);
	
	List<Products_Total_Data>get_products( List<String>uuids);
	
	int get_category_id ( String category_name);
	
	int get_product_id( String product_uuid);
}
