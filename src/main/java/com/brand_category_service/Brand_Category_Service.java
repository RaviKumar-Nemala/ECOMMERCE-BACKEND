package com.brand_category_service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.cache.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.brand_category_dao.BrandCategoryDaoInterface;
import com.brand_category_dao.Brand_Category_Dao;
import com.brand_category_module.Brand_Category_Input;
import com.brand_category_module.Brand_Category_Names;
import com.brand_category_module.Brand_Price_Data_Send;
import com.brand_category_module.Brand_Price_Input;
import com.brand_category_module.Brand_Update_Data;
import com.brand_category_module.Price_Category_Input;
import com.brands_dao.Brand_Dao;
import com.brands_dao.Brand_Data;
import com.custom_exceptions.Empty_Brand_Names_Exception;
import com.products_dao.Products_Dao;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;
import com.products_module.Products_Total_Data;
import com.products_service.Products_Get_Service;
import com.security_config.Custom_Response;
import com.products_service.ProductsServiceInterface;

@Service
public class Brand_Category_Service implements BrandCategoryServiceInterface{

	@Autowired
	private Brand_Dao brand_dao;
	
	@Autowired
	private ProductsServiceInterface productsServiceInterface;
	
	@Autowired
	private BrandCategoryDaoInterface  brandCategoryDaoInterface;

	@Autowired
	private Brand_Category_Get_Service brand_category_get_service;
	
	@Autowired
	private Brand_Price_Data_Send brand_price_data_send;
	
	@Qualifier("${cache.brand_based_products_cache}")
	private String BRANDS_BASED_PRODUCTS_CACHE;
	
	@Qualifier("${cache.brand_category_cache}")
	private String BRAND_CATEGORY_BASED_PRODUCTS_CACHE;
	
	@Qualifier("${cache.price_category_cache}")
	private String PRICE_CATEGORY_BASED_PRODUCTS_CACHE;
	
	@Qualifier("${cache.price_brand_cache}")
	private String PRICE_BRAND_BASED_PRODUCTS_CACHE;
	
	
	List<Brand_Data> brand_details;

	
	/**	
	 * 	it stores the categoryname as the key and 
		stores the corresponding price ranges as value
		example :for laptops  -> value = {  Any Price
											Under 20,000
											20,000 - 30,000
											30,000 - 40,000
										  } 
	**/
	
	private Map<String, List<String> > category_price_ranges = new HashMap<>();
	
	
	public Brand_Category_Service() {
		
		String category_name = "laptops";
		List<String> price_ranges = Arrays.asList
				("Any Price" ,
				"Under 20,000" , 
				"20,000-30,000" , 
				"30,000-40,000",
				"40,000-50,000",
				"Above 50,000"
				);
		category_price_ranges.put(category_name, price_ranges);
		
		category_name = "phones";
		price_ranges = Arrays.asList
				("Any Price" ,
				"Under 10,000" , 
				"10,000-20,000",
				"20,000-30,000" , 
				"30,000-40,000",
				"Above 40,000"
				);
		category_price_ranges.put(category_name, price_ranges);
		
		category_name = "watches";
		price_ranges = Arrays.asList(
				"Any Price",
				"below 500",
				"500-1000",
				"1000-1500",
				"Above 1500"
				);
		category_price_ranges.put(category_name, price_ranges);
		
		category_name ="shoes";
		price_ranges = Arrays.asList(
				"Any Price",
				"below 400",
				"400-500",
				"Above 500"
				);
		category_price_ranges.put(category_name, price_ranges);
		
		category_name = "tvs";
		price_ranges = Arrays.asList(
					  "Any Price",
					  "below 10,000",
					  "10,000-20,000",
					  "20,000-40,000",
					  "Above 40,000"
				 );
		category_price_ranges.put(category_name, price_ranges);
		
		category_name = "shirts";
		price_ranges = Arrays.asList(
						"Any Price",
						"below 500",
						"500-1000",
						"Above 1000"
				   		);
		category_price_ranges.put(category_name, price_ranges);
				 
	}
	
	public List <String> get_brand_details ()
	{
		return this.brand_dao.get_brands();
	}
	
	private boolean has_numbers( String str)
	{
		Pattern patt = Pattern.compile("/[0-9]/");
		
		Matcher matcher  =patt.matcher(str);
		
		return matcher.find();
	}
	
	public boolean has_special_charachers(String str)
	{
//		/[!@#\$%\^&\*\(\)]/
		Pattern patt = Pattern.compile("/[!@\\$\\.@\\^\\(\\)]/");
			
		Matcher matcher = patt.matcher(str);
		
		return matcher.find();
	}
	private boolean has_empty_spaces(String str)
	{
		if( str != null && str.contains(" "))
		{
			return true;
		}
		return false ;
	}
	
	public Custom_Response add_brand_name( Map<String,String> brand_details)
	{
		Custom_Response cr = new Custom_Response();
		
		if ( brand_details == null || brand_details.size() == 0 || !brand_details.containsKey("brand_name") )
		{
			cr.setMessage("INVALID BRAND DETAILS");
		}
		
		String brand_name = brand_details.get("brand_name");
		
		boolean res = has_numbers( brand_name );
		
		if ( res )
		{
			cr.setMessage("BRAND NAME SHOULD NOT CONTAIN NUMBERS");
			return cr;
		}

		res  = has_special_charachers(brand_name);
		
		if ( res)
		{
			cr.setMessage("BRAND NAME SHOULD NOT CONTAIN SPECIAL CHARACTERS");
			return cr;
		}
		
		res = has_empty_spaces(brand_name);
		
		if ( res  )
		{
			cr.setMessage("BRAND NAME SHOULD NOT CONTAIN ANY EMPTY SPACES");
			return cr;
		}
		
		return brand_dao.add_brand_names(brand_name);
	}
	
	public String get_brand_name ( int product_id ) 
	{
			return this.brand_category_get_service.get_brand_name(product_id);
			
	}
	
	private int brand_name_to_id (  String brand_name )
	{

		return this.brand_dao.get_brand_id(brand_name);

	}

	public int get_category_id( String category_name )
	{
		return this.productsServiceInterface.get_category_id ( category_name);
	}
	
	public boolean update_brand_name( Brand_Update_Data data )
	{
		String new_brand_name = data.getNew_brand_name();
		
		String old_brand_name = data.getOld_brand_name();
		
		if( old_brand_name != null )
		{
			old_brand_name = old_brand_name.trim();
		}
		if( new_brand_name != null)
		{
			new_brand_name = new_brand_name.trim();
		}
		
		if( old_brand_name == null || old_brand_name.length() <=2 || has_numbers(old_brand_name) || has_empty_spaces(old_brand_name) || has_special_charachers(old_brand_name))
		{
			return false ;
		}
		
		if( new_brand_name == null || new_brand_name.length() <=2 || has_numbers(new_brand_name) || has_empty_spaces(new_brand_name) || has_special_charachers(new_brand_name))	
		{
			return false;
		}
		
		return  this.brand_dao.update_brand_name(data);
	}
	
	
	public List<Products_Data_Send> get_products(  String brand_name , String category_name)
	{

		int brand_id = this.brand_name_to_id(brand_name);

		int category_id = this.get_category_id(category_name);

		List<Products_Total_Data> product_details = this.brandCategoryDaoInterface.get_products(brand_id, category_id);

		return this.productsServiceInterface.send_products_data( product_details);

	}
	
	@Cacheable(cacheNames = "price_category_cache" , key = "#price_category_input")
	public Products_List_Holder   get_price_based_products ( Price_Category_Input price_category_input)
	{
		
		String category_name = price_category_input.getCategory_name();
		
		int category_id = this.productsServiceInterface.get_category_id(category_name);
		
		List<Float>price_ranges = price_category_input.getPrice_ranges();
		
		return new Products_List_Holder(this.brand_category_get_service.get_price_based_products(category_id  , price_ranges) );	
	}
	
	/**
	 * incase if any one of the brand name is  null or not valid 
	 * then we cannot get back the brand id 
	 * in case of the invalid brand id  brand dao returns the -1 ;
	 **/ 
	private List<Integer> get_brand_ids( List<String >brand_names )
	{
		
		List<Integer>brand_ids = new ArrayList<>();
		
		for( String brand_name : brand_names)
		{
			int brand_id = this.brand_name_to_id(brand_name);
			
			if ( brand_id != -1 )
				brand_ids.add( brand_id);
			
		}

		return brand_ids;
		
	}
		
	@Cacheable(cacheNames ="brand_category_cache" , key = "#brand_category_input")
	public Products_List_Holder   get_brand_based_products( Brand_Category_Input  brand_category_input)
	{
		
		List<String >brand_names = brand_category_input.getBrand_names();
		
		String category_name = brand_category_input.getCategory_name();
	
		if ( is_empty( brand_names))
		{
			throw new   Empty_Brand_Names_Exception("BRAND NAMES SHOULD NOT BE EMPTY");
		}
		List<Integer>brand_ids = this.get_brand_ids(brand_names);
		int category_id = this.productsServiceInterface.get_category_id(category_name);
		
		return new  Products_List_Holder(this.brand_category_get_service.get_brand_based_products( brand_ids , category_id));
	}
	private boolean is_empty ( List<?> list)
	{
		if ( list == null || list.size () == 0 )
		{
			return true;
		}
			return false; 
	}
	
	/**
	 * 
	 * if the  brand names list is null then simply throw an error invalid brand names 
	 * 
	 * if the user specified the invalid price values or the price ranges list is null then simply get the products based on the brand names 
	 */
	
	@Cacheable(cacheNames ="price_brand_cache" , key = "#details")
	public Products_List_Holder get_price_brand_based_products ( Brand_Price_Input details)
	{
	
		 List<String> brand_names  =  details.getBrand_names() ;
		 
		 if ( is_empty( brand_names ))
		 {
			
		 }
		 
		 List<Float> price_ranges  = details.getPrice_ranges(); 
		 
		 String category_name = details.getCategory_name();
		
		 if ( is_empty(price_ranges))
		 {
			 Brand_Category_Input input = new Brand_Category_Input();
			 
			 input.setBrand_names(brand_names);
			 input.setCategory_name(category_name);
			 
			 return this.get_brand_based_products( input );
		 }
		 
		 List<Integer>brand_ids = this.get_brand_ids(  brand_names );
		 
		 TreeSet<Float>my_set = new TreeSet<>();
		 
		 my_set.addAll(price_ranges);
		 
		 float min_price =  my_set.first();
		 
		 float max_price = my_set.last();
		 
		 int category_id = this.productsServiceInterface.get_category_id(category_name);
			 
		 List<Products_Data_Send> product_details = this.brand_category_get_service.get_price_brand_based_products(category_id, brand_ids, min_price, max_price);
		 
		 return new Products_List_Holder(product_details);
	}
	
	
	@Cacheable( value = "brand_based_products_cache" , key = "#brand_name" , condition = "#brand_name != null&& #brand_name.length()>3" )
	public Products_List_Holder  filter_by_brand(  String brand_name)
	{
		 if( brand_name == null || brand_name.length() <= 3 )
		 {
			  return null;
		 }
		 return new Products_List_Holder(this.brand_category_get_service.filter_by_brand(brand_name ) );
	}
	
	//it returns the brand names that are availabe for the given category name
	//ex :  if category name  = laptops then brand names = "asus" , "hp" , "dell"
	//useful for displaying the sorting option in the front end 
	
	public Brand_Price_Data_Send get_brand_price_data ( String category_name )
	{
		int category_id  = this.productsServiceInterface.get_category_id(category_name);
		if( category_id == -1 )
		{
			return null;
		}
		
		List<String> brand_names = this.brand_category_get_service.get_brand_names(category_id);
		List<String>price_ranges = this.category_price_ranges.get(category_name);		
		this.brand_price_data_send.setBrand_names(brand_names);
		this.brand_price_data_send.setPrice_ranges(price_ranges);

		return this.brand_price_data_send;
	}

	
	public List<Brand_Category_Names> get_brand_category_names ()  {
		return this.brand_category_get_service.get_brand_category_names();
	}
	
	//helpful for  the  processing the input data
	//used for processing the search based data
	public List<Products_Data_Send>get_data( int category_id , int brand_id )
	{
		
		List< Integer> brand_ids  = new ArrayList<>();
		brand_ids.add(brand_id);
		
		return  this.brand_category_get_service.get_brand_based_products( brand_ids , category_id);
	}
	
	private List<Integer>get_brand_arr ( int brand_id )
	{
		List<Integer>brand_ids = new ArrayList<>();
		brand_ids.add( brand_id);
		return brand_ids;
	}
	
	public List<Products_Data_Send>get_brand_category_based_products (  int category_id , int brand_id , float min_price , float max_price )
	{
		
		List<Integer> brand_ids =  get_brand_arr ( brand_id );
		
		return this.brand_category_get_service.get_price_brand_based_products(category_id, brand_ids, min_price, max_price);
		
	}
	
	public List<Products_Data_Send>get_brand_category_based_products_above_price( int category_id , int brand_id , float min_price)
	
	{
		List<Integer> brand_ids =  get_brand_arr ( brand_id );
		return this.brand_category_get_service.get_brand_category_based_products_above_price(category_id, brand_ids, min_price);
		
	}
	
	public List<Products_Data_Send>get_brand_category_based_products_under_price (int category_id , int brand_id , float max_price )
	{
		List<Integer> brand_ids =  get_brand_arr ( brand_id );
		return this.brand_category_get_service.get_brand_category_based_products_below_price(category_id, brand_ids, max_price);
	}
	
	public List<Products_Data_Send> get_category_price_based_products_under_price( int category_id , float max_price)
	{
		return this.brand_category_get_service.get_category_price_based_products_under_price(category_id, max_price);
	}
	
	public List<Products_Data_Send>get_category_price_based_products_above_price (int category_id ,float min_price )
	{
		return this.brand_category_get_service.get_category_price_based_products_above_price(category_id, min_price);
	}
}
