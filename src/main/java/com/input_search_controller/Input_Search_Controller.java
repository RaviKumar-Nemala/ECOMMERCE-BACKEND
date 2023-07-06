package com.input_search_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.input_search_module.Brand_Category_Data_Send;
import com.input_search_module.Entered_Data;
import com.input_search_service.Input_Data_Service;
import com.products_module.Products_Data_Send;

@RestController
@RequestMapping("input")
@CrossOrigin
public class Input_Search_Controller {

	@Autowired
	private Input_Data_Service input_data_service;

	@PostMapping("/get_data")
	public ResponseEntity<?>get_search_based_products (@RequestBody(required= false) Entered_Data usr_entered_data)
	{
		List<Products_Data_Send> product_details = this.input_data_service.get_data_on_input( usr_entered_data.getKeywords());
		
		if ( product_details != null )
		{
			return ResponseEntity.ok( product_details );
		}
		else
		{
			return ResponseEntity.badRequest().body( "NO DATA FOUND ");
		}
	}
	
	@GetMapping("/get_brands_categories")
	public ResponseEntity<?>get_brands_categories ( )
	{
		
		Brand_Category_Data_Send data =    this.input_data_service.get_brand_category_info();
		
		return  ResponseEntity.ok( data);
		
	}
	
}
