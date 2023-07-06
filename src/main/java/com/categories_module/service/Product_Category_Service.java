package com.categories_module.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.categories_module.Category_Update_Data;
import com.categories_module.Product_Category_Data;
import com.categories_module.dao.Product_Category_Dao;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;
import com.products_service.Products_Service;
import com.security_config.Custom_Response;
import com.products_service.ProductsServiceInterface;

@Service( "product_category_service")
@Primary
public class Product_Category_Service {

	String accepted_image_formats [] =  { "image/png" , "image/jpeg" , "image/webp"};
	private  String [] home_page_categories_data = { "laptops" , "phones" , "shoes" , "watches" , "shirts"};
	
	@Autowired
	@Qualifier("category_dao")
	private Product_Category_Dao category_dao ;

	@Autowired
	private ProductsServiceInterface products_service_interface;

	public Custom_Response insert_category ( Product_Category_Data product_category_data)
	{
		return this.category_dao.insert_product_category(product_category_data);
	}

	public List< Product_Category_Data> get_categories ( )
	{

		return this.category_dao.get_category_items();
	}
	
	private boolean has_numbers( String str)
	{
		Pattern patt = Pattern.compile("/[0-9]/");
		
		Matcher matcher  =patt.matcher(str);
		
		return matcher.find();
	}
	
	public boolean has_special_charachers(String str)
	{
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
	public boolean update_category (Category_Update_Data category_data )
	{
		String old_category_name = category_data.getOld_category_name();
		
		String new_category_name = category_data.getNew_category_name();
		
		if( old_category_name != null )
		{
			old_category_name = old_category_name.trim();
		}
		if( new_category_name != null)
		{
			new_category_name = new_category_name.trim();
		}
		
		if( old_category_name == null || old_category_name.length() <=2 || has_numbers(old_category_name) || has_empty_spaces(old_category_name) || has_special_charachers(old_category_name))
		{
			return false ;
		}
		
		if( new_category_name == null || new_category_name.length() <=2 || has_numbers(new_category_name) || has_empty_spaces(new_category_name) || has_special_charachers(new_category_name))
			
		{
			return false;
		}
		
		return this.category_dao.update_product_category(category_data);

	}

public boolean is_file_empty (  MultipartFile  multi_part_file )
{
		if ( multi_part_file.isEmpty() )
		{
			return true;
		}
		else
		{
			return false;
		}

}

public String validate_file (  MultipartFile multi_part_file)
{

		String message = "";
		if( is_file_empty(multi_part_file))
		{
			message = "file  content should not be empty";
		}
		else if( !is_valid_image_format( multi_part_file.getContentType()))
		{
			message = "file format not supported";
		}

		else
		{
			message ="VALID FILE FORMAT";
		}
		return message;

}

public boolean is_valid_image_format( String entered_image_format)
{
	for ( String image_format : accepted_image_formats )
	{
		if ( image_format.equals(entered_image_format))
		{
			return true;
		}
	}
	return false;
}

	@Cacheable(cacheNames =  "category_based_data_cache" , key = "#category_name")
	public Products_List_Holder get_category_based_products ( String category_name)
	{
			return new Products_List_Holder(this.products_service_interface.get_category_products(category_name));
	}
	

	//return the data from the different categories which has to be shown on the home page
	public List<List<Products_Data_Send>> get_home_page_data() 
	{
			List<List<Products_Data_Send>> products_data = new ArrayList<>();
			
			for ( String str :  home_page_categories_data )
			{
				products_data.add( this.get_category_based_products(str).getData());
			}
			return products_data;
	}
	
	public List<String> get_category_names ()
	{
		return this.category_dao.get_category_names();
	}
	
}

