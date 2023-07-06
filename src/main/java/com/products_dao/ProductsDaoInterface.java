package com.products_dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.products_module.Products_Data;
import com.products_module.Products_Input_Data;
import com.products_module.Products_Side_Image;
import com.products_module.Products_Side_Images_Data;
import com.products_module.Products_Total_Data;
import com.security_config.Custom_Response;

public interface ProductsDaoInterface {

	int get_category_id( String category_name );
	
	Custom_Response add_products( Products_Input_Data input_data , float price, int stock ,int category_id , int brand_id );

	Custom_Response add_products( Products_Data  data , List<Products_Side_Images_Data> side_imgs);
	
	List< Products_Side_Image >get_product_side_images(int product_id);
	
	String get_category_name( int category_id );
	
	List<Products_Total_Data> get_product_deatails();

	List<Products_Total_Data>get_category_based_products(String category_name);

	List<Products_Total_Data>get_products(List<String >uuids);
	
	Products_Total_Data get_product(int product_id);
	
	int get_product_id( String uuid );
	
	byte[] get_product_image(  String uuid );
	
	byte[] get_side_image( String uuid );

	
}
