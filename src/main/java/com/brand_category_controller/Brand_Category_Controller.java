package com.brand_category_controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brand_category_module.Brand_Category_Input;
import com.brand_category_module.Brand_Price_Data_Send;
import com.brand_category_module.Brand_Price_Input;
import com.brand_category_module.Brand_Update_Data;
import com.brand_category_module.Price_Category_Input;
import com.brand_category_service.BrandCategoryServiceInterface;
import com.brand_category_service.Brand_Category_Service;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;
import com.security_config.Custom_Response;

@RestController
@RequestMapping("brand_category")
public class Brand_Category_Controller {

	@Autowired
	private BrandCategoryServiceInterface  brandCategoryServiceInterface;
		
	@PostMapping(value="/get_price_brand_based_products")	
	public ResponseEntity<?>get_product_details (@RequestBody ( required = false)Brand_Price_Input brand_price_input)
	{
		Products_List_Holder product_details =   this.brandCategoryServiceInterface.get_price_brand_based_products(brand_price_input);
			
		return ResponseEntity.ok( product_details.getData());
	}
	
	@GetMapping(value = "/get_brand_based_products")
	public ResponseEntity<?>filter_by_brand( @RequestParam(value="brand_name") String brand_name)
	{
		Products_List_Holder details =  this.brandCategoryServiceInterface.filter_by_brand(brand_name);
		if( details == null)
		{
			return ResponseEntity.badRequest().body("INVALID BRAND NAME ");
		}
		else 
			return ResponseEntity.ok( details.getData() );	
	}
	
	@PostMapping(value="/get_price_based_products")	
	public ResponseEntity<?>get_product_details (@RequestBody Price_Category_Input price_category_input)
	{
		Products_List_Holder product_details = this.brandCategoryServiceInterface.get_price_based_products( price_category_input );
		return ResponseEntity.ok( product_details.getData());
	}
	
	@PostMapping(value={ "/get_brand_category_based_products"})	
	public ResponseEntity<?>get_product_details (@RequestBody Brand_Category_Input brand_category_input)
	{	
		Products_List_Holder product_details = this.brandCategoryServiceInterface.get_brand_based_products(brand_category_input);
		
		return ResponseEntity.ok(product_details.getData());
	}
	
	//for the given category name it return how many brands are available for the category also returns the price ranges
	@GetMapping(value = "/get_brand_price_sorting_options")
	public ResponseEntity<?> get_brand_price_values( @RequestParam String category_name)
	{
		Brand_Price_Data_Send brand_price_details = this.brandCategoryServiceInterface.get_brand_price_data(category_name);
		
		return  ResponseEntity.ok(brand_price_details);
		
	}
	
	@GetMapping(value ="/get_brand_names")
	public ResponseEntity<?> get_brand_names( )
	{	
		
		List< String >brand_names =  this.brandCategoryServiceInterface.get_brand_details();
		
		class Brand_Name
		{
			String brand_name ; 
			Brand_Name(String str)
			{
				this.brand_name = str;
			}
			public String getBrandName()
			{
				return this.brand_name;
			}
		}
		
		List< Brand_Name> response = new ArrayList<>();
		
		for ( String brand_name : brand_names )
		{
			Brand_Name  br = new Brand_Name( brand_name) ;
			
			response.add( br ) ; 
		}
		
		if( brand_names == null || brand_names.size () == 0 )
		{
			return ResponseEntity.badRequest().body("SOMETHING WENT WROHNG UNABLE TO FETCH THE BRAND NAMES");
		}
		else
		{
			return ResponseEntity.ok ( response );
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add_brand")
	public ResponseEntity<?>add_brand( @RequestBody Map<String,String> brand_details)
	{
		  Custom_Response cr =  this.brandCategoryServiceInterface.add_brand_name(brand_details);
		  
		  if ( cr.isOk())
		  {
			  return ResponseEntity.ok(cr.getMessage());
		  }
		  else 
		  {
			  return ResponseEntity.badRequest().body(cr.getMessage());
		  }
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update_brand")
	public ResponseEntity<?>update_brand( @RequestBody Brand_Update_Data  data)
	{
		boolean res = this.brandCategoryServiceInterface.update_brand_name(data);
			
		System.out.println ( res ) ; 
		
		if( res )
		{
			return ResponseEntity.ok("BRAND NAME UPDATED SUCCESSFULLY");
		}
		else 
		{
			return ResponseEntity.badRequest().body("FIALURE WHILE UPDATING THE BRAND NAME");
		}
	}
}