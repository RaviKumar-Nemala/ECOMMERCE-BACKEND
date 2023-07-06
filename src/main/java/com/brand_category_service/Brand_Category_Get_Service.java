package com.brand_category_service;

import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brand_category_dao.BrandCategoryDaoInterface;
import com.brand_category_dao.Brand_Category_Dao;
import com.brand_category_module.Brand_Category_Names;
import com.brands_dao.Brand_Dao;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Total_Data;
import com.products_service.ProductsServiceInterface;
import com.products_service.Products_Get_Service;

@Service
public class Brand_Category_Get_Service {
	
	@Autowired
	private  BrandCategoryDaoInterface brandCategoryDaoInterface;
	
	@Autowired
	private ProductsServiceInterface productsServiceInterface;
	
	@Autowired
	private Brand_Dao brand_dao;
	
	private List<Products_Data_Send> util ( List< Products_Total_Data> products_details)
	{
		return this.productsServiceInterface.send_products_data(products_details);
	}
	public String get_brand_name ( int product_id ) 
	{
			return this.brandCategoryDaoInterface.get_brand_name(product_id);
	}
	
	public List<Products_Data_Send> get_price_based_products ( int category_id,  List<Float>prices )
	{
		
		if ( prices == null || prices.size()== 0 )
				return null;
		
		TreeSet<Float>prices_set = new TreeSet<>();
		
		prices_set.addAll(prices);

		float min_price = prices_set.first();
		
		float max_price = prices_set.last();
		
		List<Products_Total_Data> product_details = this.brandCategoryDaoInterface.get_price_based_products(min_price, max_price, category_id);
		
		return util ( product_details);
	}
	
	public List<Products_Data_Send> filter_by_brand ( String brand_name ) 
	{ 
		List <Products_Total_Data> product_details = this.brand_dao.filter_by_brand(brand_name);
		
		if( product_details == null)
		{
			return null; 
		}
		return util ( product_details);	
	}
	
	public List<Products_Data_Send> get_brand_based_products( List<Integer>brand_ids , int category_id)
	{
		List<Products_Total_Data> product_details =this.brandCategoryDaoInterface.get_brand_based_products(category_id ,brand_ids);
		
		return util ( product_details);	
	}
	
	public List<Products_Data_Send> get_price_brand_based_products( int category_id , List<Integer> brand_ids ,  float min_price , float max_price )
	{
		List<Products_Total_Data> product_details = this.brandCategoryDaoInterface.get_brand_price_based_products(category_id, brand_ids, min_price, max_price);
		return util ( product_details);	
	}
	
	public List<Products_Data_Send> get_brand_category_based_products_above_price( int category_id , List<Integer>brand_ids , float min_price)
	{
		List<Products_Total_Data>product_details = this.brandCategoryDaoInterface.get_brand_price_based_products_above_price(category_id,brand_ids , min_price);	
		return util ( product_details);
	}
	
	public List< Products_Data_Send> get_brand_category_based_products_below_price ( int category_id , List<Integer> brand_ids , float max_price)
	{
		List<Products_Total_Data>product_details = this.brandCategoryDaoInterface.get_brand_price_based_products_below_price(category_id, brand_ids, max_price);
		return util ( product_details);
	}

	public List<Products_Data_Send> get_category_price_based_products_under_price( int category_id , float max_price)
	{
		List<Products_Total_Data> product_details = this.brandCategoryDaoInterface.get_category_price_based_products_below_price(category_id, max_price);
		return util ( product_details);
	}
	public List<Products_Data_Send>get_category_price_based_products_above_price (int category_id ,float min_price )
	{
		List<Products_Total_Data> product_details =  this.brandCategoryDaoInterface.get_category_price_based_products_above_price(category_id, min_price);
		return util  (product_details);
	}
	
	public List<String> get_brand_names ( int category_id )
	{		
		return this.brandCategoryDaoInterface.get_brand_names(category_id);
	}
	
	public float get_min_price ( int category_id )
	{
		return this.brandCategoryDaoInterface.get_min_price(category_id);
	}
	
	public float get_max_price (int category_id )
	{
		return this.brandCategoryDaoInterface.get_max_price(category_id);
	}

	public List<Brand_Category_Names>get_brand_category_names (  )
	{
		return  this.brandCategoryDaoInterface.get_brand_category_names();
	}
}
