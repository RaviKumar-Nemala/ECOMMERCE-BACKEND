package com.input_search_service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brand_category_module.Brand_Category_Names;
import com.brand_category_service.BrandCategoryServiceInterface;
import com.brands_dao.Brand_Dao;
import com.categories_module.service.Product_Category_Service;
import com.input_search_module.Brand_Category_Data_Send;
import com.products_module.Products_Data_Send;

@Service
public class Input_Data_Service {

	@Autowired
	private  Brand_Dao brand_dao;
	
	@Autowired
	//private Product_Category_Dao category_dao;
	private Product_Category_Service  product_category_service;
	
	
	@Autowired
	private BrandCategoryServiceInterface brandCategoryServiceInterface;
	

	private List<String>brand_names ; 
	
	//we are using like this for testing purpose
	private static final  String laptops_auto_suggestions[] = { 
			"laptops",
			"laptops under 20,000",
			"laptops under 30,000",
			"laptops under 40,000",
			"laptops above 40,000"
	};
	
	private static final String phones_auto_suggestions [] = {
			"phones",
			"phones under 10,000",
			"phones under 20,000",
			"phones under 30,000",
			"phones under 40,000",
			"phones above 40,000"
	};
	
	private static final String watches_auto_suggestions[] = { 
			"watches",
			"watches under 500",
			"watches under 1000",
			"watches under 1500",
			"watches above 1500"
	};
	
	private static final String shoes_auto_suggestions[] = 
		{
				"shoes",
				"shoes under 400",
				"shoes under 500",
				"shoes above 500"
		};

	private static final String tvs_auto_suggestions[] = 
		{
				"tvs",
				"tvs under 10,000",
				"tvs under 20,000",
				"tvs under 40,000",
				"tvs above 40,000"
		};
	
	private static final String shirts_auto_suggestios[]= {
			"shirts",
			"shirts under 500",
			"shirts under 1000",
			"shirts above 1000"
	};
	
	private List<String> get_brand_names () 
	{

		List<String> brand_names = this.brand_dao.get_brands();
		
		return brand_names ;
	}
	
	private List<String>get_category_names ( )
	{
//	List<String>  category_names = new ArrayList<>();
	
	List<String> category_names =  this.product_category_service.get_category_names();
		
	return category_names;	
	
	}
	
	private Map<String , String >  get_brand_category_combined_data (  ) 
	{
	
		Map< String , String >  brand_category_map  = new HashMap<>();
		
		List <Brand_Category_Names> brand_category_names = this.brandCategoryServiceInterface.get_brand_category_names();
	
	
		for ( Brand_Category_Names  details  : brand_category_names )
		{
		
			String brand_name =  details.getBrand_name();
			
			String category_name  = details.getCategory_name();
			
			brand_category_map.put( brand_name, category_name);
			
		}
		return  brand_category_map;
	}
	
	
	
	
	private String is_brand_name (  List<String>  brand_names , String entered_value )
	{
		
		entered_value = entered_value.toLowerCase().trim();
		
		for ( String brand_name : brand_names )
		{
			
			if ( brand_name.toLowerCase().contains ( entered_value))
			{
				return brand_name;
			}
		}
		return null;
	}
	
	private List< Products_Data_Send>  get_data ( String category_name  )
	{
		return this.product_category_service.get_category_based_products(  category_name ).getData(); 
	}
	
	private List< Products_Data_Send>get_data (  int category_id  , int brand_id )
	{
		
		return this.brandCategoryServiceInterface.get_data(category_id, brand_id);
		
	}
	
	private String is_category_name ( List< String>  category_names ,  String keyword )
	{
		
		keyword = keyword.trim().toLowerCase(); 
		
		for( String category_name : category_names )
		{
			
				if ( category_name.toLowerCase().contains(keyword))
				{
					return  category_name;
				}
			
		}
		
		return null;
		
	}
	
	private Float get_price_val(int idx , List<String>entered_data)
	{
		
		Float price_val = null;
		
		for( int curr_idx = idx ; curr_idx <entered_data.size () ;curr_idx++)
		{
			
		String input = entered_data.get(curr_idx);
		
		input = input.trim();
		
		if ( input.contains(","))
		{
			input = input.replace(",","");
		}
		else if (input.contains("."))
		{
			input.replace ("." , "");
		}
		
		try 
		{
			price_val =  Float.parseFloat(input);
			
		}
		
		catch ( Exception e )
		{
			System.out.println("NOT A NUMBER");
		}
		
		}
		
		return price_val;
	}
	
	private Integer get_category_id ( String category_name )
	{
		
		return  this.brandCategoryServiceInterface.get_category_id( category_name );
		
	}
	
	
	public  List<Products_Data_Send> get_data_on_input ( List< String > entered_data)
	{
		
		List<String> brand_names = this.get_brand_names();
	
		List<String>category_names  = this.get_category_names();
		
		Map<String,String>brand_category_map = this.get_brand_category_combined_data();
		
		
		String brand_name = null;

		String category_name = null;
		
		boolean above_price = false;
		
		Float price_val = null;
		
		boolean under_price = false;
		
		List<Products_Data_Send>products_details = new ArrayList<>();
		

	for ( int idx = 0 ; idx <entered_data.size () ;idx ++) {
		
		 String keyword  = entered_data.get(idx);
			
			keyword = keyword.toLowerCase();
			
	
			if ( keyword.contains("under"))
			{
				under_price  = true;
				price_val = get_price_val(idx+1 ,  entered_data);
			}
			else if(keyword.contains("above"))
			{
				above_price = true;
				price_val=get_price_val(idx+1, entered_data);
			}
			if( brand_name == null )
			brand_name =   is_brand_name(brand_names, keyword);
			
			if ( category_name == null)
			category_name = is_category_name( category_names  , keyword);
			
		}
		
		
	Integer brand_id = null , category_id = null;
	
	if ( brand_name != null && category_name != null )
	{
		brand_id = this.brand_dao.get_brand_id(brand_name);
		category_id = get_category_id(category_name);
	}
	else if ( brand_name != null)
	{
		brand_id = this.brand_dao.get_brand_id(brand_name);
		category_id =get_category_id(brand_category_map.get(brand_name));
	}
		
	else if (  category_name != null )
	{
		category_id = get_category_id(category_name).intValue();
	}
	
	System.out.println( "BRAND_ID =  " + brand_id + " " +"CATEGORY_ID = " +category_id);
	
	if ( brand_id != null && category_id != null && ( above_price == true || under_price == true ))
	{
		
		if ( above_price)
		{
			 products_details = this.brandCategoryServiceInterface.get_brand_category_based_products_above_price(category_id.intValue(),brand_id.intValue(), price_val.floatValue());
		}
		else
		{
			products_details = this.brandCategoryServiceInterface.get_brand_category_based_products_under_price(category_id , brand_id , price_val);
		}
	}
	
	else if( brand_id != null && category_id != null )
	{
		
		products_details = this.get_data(category_id , brand_id );		
	}
	
	else if ( category_id != null && ( above_price || under_price ))
	{
			
		if ( above_price)
		{
			products_details = this.brandCategoryServiceInterface.get_category_price_based_products_above_price(category_id ,price_val);
		}
		else 
		{
			products_details = this.brandCategoryServiceInterface.get_category_price_based_products_under_price(category_id , price_val);
		}
		
	}
	else if ( category_id != null )
	{
			
		products_details = this.get_data(category_name);
		
	}
	
	
	return products_details;
		
		
}

	
	
	//for the testing purpose we are hard coding the values
	private Map<String,List<String>> prepare_category_data(  List<String> category_names )
	{
		Map<String, List<String>>data = new HashMap<>();
		
		for ( String category_name : category_names )
		{
			category_name = category_name.toLowerCase();
			
			List<String>auto_filled_lines = new ArrayList<>();

			if ( category_name.startsWith("laptop"))
			{
				auto_filled_lines = Arrays.asList(laptops_auto_suggestions);
				
			}	
			else if ( category_name.startsWith("phone"))
			{
				auto_filled_lines = Arrays.asList( phones_auto_suggestions);
			}
			
			else if( category_name.startsWith("tv"))
			{
				auto_filled_lines = Arrays.asList(tvs_auto_suggestions);
			}
			else if(  category_name.startsWith("watch"))
			{
				auto_filled_lines = Arrays.asList(watches_auto_suggestions);
			}
			else if ( category_name.startsWith("shoe"))
			{
				auto_filled_lines = Arrays.asList(shoes_auto_suggestions);
			}
			else if ( category_name.startsWith("shirt"))
			{
				auto_filled_lines = Arrays.asList(shirts_auto_suggestios);
			}
			
			
			if ( auto_filled_lines.size() !=  0 )
			{
				data.put( category_name , auto_filled_lines);
			}
		}
		return data;
	}
	
	public Brand_Category_Data_Send get_brand_category_info (  )
	{
		
		List<String> category_names  = this.get_category_names();
		
		List<String> brand_names  = this.get_brand_names();
		
		Map<String , List<String>> category_based_auto_suggestions = this.prepare_category_data(category_names);
		
		Map<String , String>brand_based_suggestions = get_brand_category_combined_data(); 
	
		Brand_Category_Data_Send data =  new Brand_Category_Data_Send();
		
		data.setBrands(brand_based_suggestions);
		
		data.setCategories(category_based_auto_suggestions);
		
		return data;
	}
	
}
