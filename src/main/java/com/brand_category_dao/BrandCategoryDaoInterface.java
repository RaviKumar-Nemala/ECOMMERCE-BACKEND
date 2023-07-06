package com.brand_category_dao;

import java.util.List;

import com.brand_category_module.Brand_Category_Names;
import com.products_module.Products_Total_Data;

public interface BrandCategoryDaoInterface {

	String get_brand_name(int product_id);
	
	List<Products_Total_Data> get_price_based_products( float min_price, float max_price, int category_id);
	
	List<Products_Total_Data> get_brand_based_products(int category_id ,List<Integer>brand_ids);
	
	List<Products_Total_Data> get_brand_price_based_products(int category_id, List<Integer>brand_ids, float min_price, float max_price);
	
	List<Products_Total_Data>get_brand_price_based_products_above_price(int category_id,List<Integer>brand_ids , float min_price);
	
	List<Products_Total_Data>get_brand_price_based_products_below_price(int category_id, List<Integer>brand_ids, float max_price);
	
	List<Products_Total_Data>get_category_price_based_products_below_price(int category_id, float max_price);
	
	List<Products_Total_Data>get_category_price_based_products_above_price(int category_id, float min_price);
	
	List<String> get_brand_names(int category_id);
	
	float get_min_price(int category_id);
	
	float get_max_price(int category_id);
	
	List<Brand_Category_Names>get_brand_category_names();
	
	List<Products_Total_Data> get_products(int brand_id, int category_id);

}
