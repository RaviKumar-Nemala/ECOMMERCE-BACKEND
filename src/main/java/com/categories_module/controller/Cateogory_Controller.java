package com.categories_module.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.categories_module.Category_Update_Data;
import com.categories_module.Product_Category_Data;
import com.categories_module.service.Product_Category_Service;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;
import com.security_config.Custom_Response;

@RestController
@CrossOrigin
public class Cateogory_Controller {

	@Autowired
	@Qualifier("product_category_service")
	private Product_Category_Service category_service;

	@GetMapping(value = "/categories/get_categories")
	public ResponseEntity<List<?>> get_categories ( )throws IOException
	{
		List<Product_Category_Data > data  =  this.category_service.get_categories();
		return ResponseEntity.ok( data ) ;
	}

	@PreAuthorize("hasRole(ADMIN)")
	@PutMapping(value = "/categories/update_category")
	public ResponseEntity<String>update_category ( @RequestBody Category_Update_Data data )
	{
		boolean update_result = this.category_service.update_category(data);
		if ( update_result )
		{
			return ResponseEntity.ok("CATEGORY UPDATED SUCCESSFULLY");
		}
		else
		{
			return ResponseEntity.badRequest().body("INVALID CATEGORY NAME");
		}
	}
	
	@GetMapping(value ="/categories/get_products")
	public ResponseEntity<List<Products_Data_Send>>get_category_based_products (@RequestParam(value="category_name") String category_name )
	{
			Products_List_Holder data = this.category_service.get_category_based_products(category_name) ;
			return new ResponseEntity<>( data.getData() ,HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping( value = "/categories/add_category" , method=RequestMethod.POST )
	public ResponseEntity<String> insert_category ( @RequestBody Product_Category_Data pd) throws IOException
	{
		if( pd == null )
		{
			return ResponseEntity.badRequest().body("INVALID DETAILS");
		}
			ResponseEntity<String > rs = null ;
			Custom_Response cr = this.category_service.insert_category(pd);
			
		if  ( cr.isOk())
		{
			rs = new ResponseEntity<>(cr.getMessage() , HttpStatus.OK);
		}
		else
		{
			rs = new ResponseEntity<>(cr.getMessage() , HttpStatus.BAD_REQUEST);
		}
		return rs;
	}
}
