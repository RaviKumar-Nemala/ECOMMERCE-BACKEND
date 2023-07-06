package com.brand_category_dao;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.brand_category_module.Brand_Category_Names;
import com.custom_exceptions.products.Product_Details_Not_Found_Exception;
import com.products_dao.Products_Data_Mapper;
import com.products_module.Products_Total_Data;
import com.security_config.Custom_Response;

@Repository
public class Brand_Category_Dao implements BrandCategoryDaoInterface{

	private static final String  INSERT_CATEGORY_BRAND = "INSERT INTO brand_category_helper (brand_id,category_id,pid) values ( ? , ?  , ? );";

	private static final String EXTRACT_PRODUCTS = "select * from products ,  brand_category_helper where brand_category_helper.pid = products.product_id  and  brand_category_helper.category_id =  %d and brand_category_helper.brand_id = %d";
	
	private static final String SORT_BY_PRICE = "select * from products ,  brand_category_helper where brand_category_helper.pid = products.product_id and products.price >= %f and products.price <= %f  and  brand_category_helper.category_id =  %d";

	private static final String SORT_BY_BRAND = "select * from products ,  brand_category_helper where brand_category_helper.pid = products.product_id  and  brand_category_helper.category_id =  %d and brand_category_helper.brand_id = %d";
	
	private static final int OK =  1 ;

	private static final int NOT_OK = 0;

	private static final String BRAND_NAME_BY_PRODUCT_ID = "select brand_name from brand  ,  brand_category_helper  where  brand_category_helper.pid = %d and brand_category_helper.brand_id  = brand.brand_id";
	
	private static final String MIN_PRICE_QUERY = "select MIN(price) as price from products where category_id = %d";

	private static final String MAX_PRICE_QUERY  ="select MAX(price) as price  from products where category_id = %d";
 	
	private static final String BRAND_NAMES_EXTRACTOR = "select distinct brand_name  from brand  , brand_category_helper  where brand.brand_id = brand_category_helper.brand_id  and brand_category_helper.category_id =  %d";
	
	private static final String SORT_BY_CATEGORY_AND_PRICE  = "select * from  products where category_id = %d " ;

	private static final String  BRAND_CATEGORY_NAMES_EXTRCTOR =  "select distinct  brand.brand_name  , categories.category_name from brand , categories  , brand_category_helper  as helper  where brand.brand_id =  helper.brand_id   and  categories.category_id = helper.category_id " ;
	
	@Autowired
	private Custom_Response custom_response;

	@Autowired
	private JdbcTemplate jdbc_template;

	public Custom_Response set_response ( String message , int status_code )
	{

		this.custom_response.setMessage(message );

		this.custom_response.setStatus(status_code);

		return  this.custom_response;
	}

	public  String get_brand_name (  int product_id ) 
	{
		
		String query = String.format( BRAND_NAME_BY_PRODUCT_ID , product_id ) ;
		
		//the mapper is is returning the list of the brand name 
		//product_id is unique so it only returns one record 
		//so return first element of the  list
		List<String> brand_name = jdbc_template.query( query , new Brand_Names_Mapper() ) ;
		
		if ( brand_name != null && brand_name.size () ==1 ) 
			return brand_name.get(0);
		else 
		{
			System.out.println("UNABLE TO GET THE BRAND_NAME ");
			return null;
		}
	}
	
	public  Custom_Response  insert_category_brand( int brand_id, int  category_id, int product_id)
	{
		String message  = null;
			Object args [ ] = { brand_id , category_id , product_id } ;
			int res = jdbc_template.update(INSERT_CATEGORY_BRAND , args);
			message = "INSERTED SUCCESSFULLY";
			this.custom_response =  set_response(message, OK);
			return this.custom_response;
	}
	
	private List<Products_Total_Data> util  ( String query )
	{
		Predicate<List<?>>predicate  = (e)->e == null || e.size () == 0 ;
		List<Products_Total_Data> product_details  = null;
		try {
				product_details = this.jdbc_template.query(query , new Products_Data_Mapper());
		
				if ( predicate.test(  product_details) ) 
				{
			throw new Product_Details_Not_Found_Exception();
			
				}
				else 
				{
			return  product_details; 
				}
		}
		catch (  Exception  e )
		{ 
			throw  new Product_Details_Not_Found_Exception( "PRODUCT ITEMS ARE EMPTY");
		}
	}

	public List<Products_Total_Data> get_products ( int brand_id , int category_id )
	{
			String query = String.format (EXTRACT_PRODUCTS ,  category_id ,  brand_id);
			return  util ( query);
	}

	public List<Products_Total_Data> get_price_based_products (   float min_price,  float max_price ,  int category_id)
	{		
		String query =  String.format(SORT_BY_PRICE, min_price, max_price , category_id);	
		return util ( query);
	}

	private String prepare_brand_query( int category_id , List<Integer>brand_ids)
	{
		
		String query_1  = "select * from products ,  brand_category_helper where brand_category_helper.pid = products.product_id  and  brand_category_helper.category_id = " +category_id +" and " + " ( ";
				
		String query_2 = "brand_category_helper.brand_id = ";
		
		int last_idx   = brand_ids.size () -1 ;
		
		for ( int idx = 0 ; idx <= last_idx ;idx++)
		{
			int brand_id = brand_ids.get(idx);
					
			if ( idx == last_idx )
			{
				query_2 = "brand_category_helper.brand_id = "+brand_id + " )";
				
				query_1 = query_1.concat(query_2);
				
				System.out.println( query_1);
				
				return query_1;
			}
			else {
			
			query_2 =  "brand_category_helper.brand_id = "+brand_id+ " or ";
			
			query_1 = query_1.concat(query_2);
			}
		
		}
		return query_1;
	}
	
	public List<Products_Total_Data> get_brand_based_products ( int category_id , List<Integer>brand_names)
	{
		
		String prepared_query =this.prepare_brand_query(category_id,brand_names);
	
		return util (  prepared_query);
		
	}

	public List<Products_Total_Data>get_brand_price_based_products( int category_id , List<Integer> brand_ids , float min_price , float max_price )  
	{
		
		String query_1 = this.prepare_brand_query(category_id, brand_ids);
		
		String query_2 =  query_1 + " and " + "products.price >= " +min_price + " and " + "products.price <= "+ max_price; 
		
		System.out.println (  query_2 );
		
		return util ( query_2 );
		
	}
	
	public List<Products_Total_Data>get_brand_price_based_products_above_price( int category_id , List<Integer> brand_ids , float min_price )
	{		
		String query_1 =  this.prepare_brand_query(category_id, brand_ids);
		String query_2 = query_1 + " and " + "products.price >= "+min_price;	
		return util ( query_2);
	}
	
	public List<Products_Total_Data> get_brand_price_based_products_below_price( int category_id, List<Integer>brand_ids ,  float max_price )
	{
		String query_1 = this.prepare_brand_query( category_id , brand_ids);
		String query_2 = query_1 + " and " +  "products.price <= "+max_price;
		return util ( query_2);
	}
	
	public List<Products_Total_Data>get_category_price_based_products_below_price( int category_id , float max_price )
	{
	
		String query_1 =   String.format(SORT_BY_CATEGORY_AND_PRICE , category_id);
		
		query_1 = query_1 + " and " + " products.price <= "+max_price;
		
		System.out.println ( query_1);
		
		return util ( query_1);
		
	}
	
	public List<Products_Total_Data>get_category_price_based_products_above_price ( int category_id , float min_price )
	{
		String query_1 = String.format(SORT_BY_CATEGORY_AND_PRICE , category_id);
		query_1 = query_1 + " and " + " products.price >= "+min_price;
		return util ( query_1);
	}
	
	public float get_min_price ( int category_id ) 
	{	
		String query_1 = String.format ( MIN_PRICE_QUERY ,  category_id );	
		float min_price = this.jdbc_template.queryForObject(query_1, new Price_Mapper());
		return min_price;
	}
	
	public float get_max_price ( int category_id )
	{
		String query_1 = String.format ( MAX_PRICE_QUERY, category_id );
		float max_price = this.jdbc_template.queryForObject(query_1, new Price_Mapper() );
		return max_price;
	}

	public List<String> get_brand_names ( int category_id )
	{
		String query_1 = String.format ( BRAND_NAMES_EXTRACTOR , category_id);	
		List<String>brand_names = this.jdbc_template.query(query_1 , new Brand_Names_Mapper());
		return brand_names;
  	}
	
	//returns the all the brand names and their category names 
	//used for processing the input search 
	public List< Brand_Category_Names> get_brand_category_names ()
	{
		try {
		List<Brand_Category_Names > brand_category_names = this.jdbc_template.query(BRAND_CATEGORY_NAMES_EXTRCTOR,   new Brand_Category_Names_Mapper());	
		return brand_category_names;
		}
		catch( DataAccessException exp) 
		{
			exp.printStackTrace();			
			return null;
		}
	}
}