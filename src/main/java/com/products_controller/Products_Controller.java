package com.products_controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.payments.module.Product_Update_Data;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Input_Data;
import com.products_service.Products_Add_Service;
import com.products_service.Products_Service;
import com.products_service.ProductsServiceInterface;
import com.products_service.Products_Updation_Service;
import com.security_config.Custom_Response;

@RestController
@RequestMapping("/products")
public class Products_Controller {

	@Autowired
	ProductsServiceInterface products_service_interface;
	
	@PostMapping(value = "/insert_single_product" , consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?>insert_product( @ModelAttribute Products_Input_Data data) throws IOException
	{
		Custom_Response cr = this.products_service_interface.insert_singe_record(data);
		
		if ( cr.isOk())
		{
			return ResponseEntity.ok("PRODUCT INSERTED SUCCESSFULLY");
		}
		else
		{
			return ResponseEntity.badRequest().body(cr.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get_products")
	public ResponseEntity< List<Products_Data_Send> > get_products ( )
	{
		List< Products_Data_Send> products_data = this.products_service_interface.get_products();
		return new ResponseEntity<>(products_data , HttpStatus.OK);
	}

	@GetMapping(value ="/get_image/{uuid}" )
	public byte[]  get_product_image( @PathVariable ( required= true) String uuid )
	{
		return this.products_service_interface.get_product_image( uuid );
	}
	
	@GetMapping(value="/get_product")
	public ResponseEntity<?>get_product ( @RequestParam(value ="uuid") String uuid)
	{		
		Products_Data_Send product_details = this.products_service_interface.get_product(uuid);	
		return ResponseEntity.ok( product_details);
	}
	
	@GetMapping(value ="/get_side_image/{uuid}")
	public byte[]  get_product_side_image( @PathVariable ( required= true) String uuid )
	{
		return this.products_service_interface.get_product_side_image( uuid );
	}
}