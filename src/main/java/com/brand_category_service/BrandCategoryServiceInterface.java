package com.brand_category_service;

import java.util.List;
import java.util.Map;

import com.brand_category_module.Brand_Category_Input;
import com.brand_category_module.Brand_Category_Names;
import com.brand_category_module.Brand_Price_Data_Send;
import com.brand_category_module.Brand_Price_Input;
import com.brand_category_module.Brand_Update_Data;
import com.brand_category_module.Price_Category_Input;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;
import com.security_config.Custom_Response;

public interface BrandCategoryServiceInterface {

	Products_List_Holder get_price_brand_based_products(Brand_Price_Input brand_price_input);
	
	Products_List_Holder filter_by_brand(String brand_name);

	Products_List_Holder get_price_based_products( Price_Category_Input   price_category_input );
	
	Products_List_Holder get_brand_based_products( Brand_Category_Input  brand_category_input);
	
	Brand_Price_Data_Send get_brand_price_data(String category_name);
	
	List< String >get_brand_details();
	
	Custom_Response add_brand_name(Map<String,String>brand_details);
	
	boolean update_brand_name(Brand_Update_Data data);
	
	List <Brand_Category_Names> get_brand_category_names();
	
	List<Products_Data_Send> get_data(int category_id,int  brand_id);
	
	int get_category_id( String category_name );
		
	List<Products_Data_Send> get_brand_category_based_products_above_price(int category_id,int brand_id , float price_val);
	
	List<Products_Data_Send>get_brand_category_based_products_under_price(int category_id , int brand_id , float price_val);
	
	List< Products_Data_Send> get_category_price_based_products_above_price(int category_id ,float price_val);
	
    List < Products_Data_Send > get_category_price_based_products_under_price(int category_id , float price_val);
    
}
