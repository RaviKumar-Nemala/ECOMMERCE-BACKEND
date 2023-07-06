package com.products_service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.payments.module.Product_Update_Data;
import com.products_dao.ProductsDaoInterface;
import com.products_dao.Products_Dao;
import com.products_dao.Products_Updation_Dao;
import com.products_module.Products_Images_Updation_Data;
import com.products_module.Products_Price_Update_Data;
import com.products_module.Products_Side_Images_Data;
import com.products_module.Products_Specifications_Update_Data;
import com.products_module.Products_Stock_Update_Data;
import com.products_module.Products_Update_Data;
import com.security_config.Custom_Response;

@Service
public class Products_Updation_Service {

	@Autowired
	private Products_Updation_Dao products_updation_dao ;

	@Autowired
	private Products_Add_Service product_add_service;

	@Autowired
	private ProductsDaoInterface productsDaoInterface;
	
	List<String>accepted_image_formats  = Arrays.asList("image/jpeg","image/png","image/webp");

	@Autowired
	private Custom_Response updation_response;
	
	
	public Custom_Response  update_product_details( Product_Update_Data product_details)
	{
		
		if ( product_details.getProduct_name() == null )
		{
			this.updation_response.setMessage("INVALID  PRODUCT NAME");
			return updation_response;
		}
		
		else if ( product_details.getPrice() < 0 )
		{
			this.updation_response.setMessage("INVALID PRICE VALUE");
			return updation_response;
		}
		else if(  product_details.getCategory() == null )
		{
			this.updation_response.setMessage("INVALID CATEGORY VALUE");
			return updation_response;
		}
		else if( product_details.getStock() < 0)
		{
			this.updation_response.setMessage("INVALID STOCK VALUE ");
			return updation_response;
		}
		else if  ( product_details.getProduct_uuid( ) == null)
		{
			this.updation_response.setMessage( "INVALID PRODUCT UUID VALUE");
			return updation_response;
		}
		else
		{
			int category_id = this.productsDaoInterface.get_category_id(product_details.getCategory());			
			
			if( category_id == -1 )
			{
				this.updation_response.setMessage("INVALID  CATEGORY NAME");
				return updation_response;
			}
			
			List< MultipartFile > new_imgs  = product_details.getNewly_added_side_imgs();
			
			// check if there is any new side images are added 
			
			
			List<Products_Side_Images_Data> side_imgs_data = new ArrayList<>();
			
			
			
			boolean img_valid_status = true;

			if ( new_imgs != null && new_imgs.size() >  0 )
			{		
				
				for( MultipartFile img : new_imgs ) {
					if( img.isEmpty()==false && accepted_image_formats.contains(img.getContentType()))
					{
						Products_Side_Images_Data prd = new Products_Side_Images_Data();
												
						try {
							prd.setImage_type( img.getContentType() );
							prd.setSide_images(img.getBytes());
							side_imgs_data.add(prd);
						} catch (IOException e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
							
							img_valid_status = false ;
						}
					}
					else 
					{
						img_valid_status = false ;
					}
				}
					
			}
			
			if ( img_valid_status == false)
			{
				this.updation_response.setMessage("INVALID IMAGE PLEASE UPLOAD CORRECT IMAGE");
				this.updation_response.setStatus(0);
				return this.updation_response;
			}
			
			
			try {
			 return this.products_updation_dao.update_product_details(product_details , side_imgs_data,category_id);
			 
		}
			catch( Exception e )
			{
				Custom_Response custom_response = new Custom_Response();
				custom_response.setMessage("SOMETHING WENT WRONG WHILE UPATING THE DETEAILS");
				custom_response.setStatus(0);
				return custom_response;
			}
		}
	}
	
	public Custom_Response update_product_stock( Products_Stock_Update_Data product_stock_data )
	{

		return this.products_updation_dao.update_stock(  product_stock_data );


	}


	public Custom_Response update_product_price ( Products_Price_Update_Data product_price_update_data )
	{

		 return this.products_updation_dao.update_price( product_price_update_data);

	}

	public Custom_Response update_product_images( Products_Images_Updation_Data  product_image_update)
	{

		return this.products_updation_dao.update_product_images( product_image_update);
	}


	public Custom_Response update_product_specifications ( Products_Specifications_Update_Data  product_specification_update  )
	{

		return this.products_updation_dao.update_prouduct_specifications( product_specification_update);

	}

	public  Custom_Response add_new_side_images( Products_Update_Data product_data , List< MultipartFile> side_images_list)
	{

		List<Products_Side_Images_Data> side_images_data = null;

		try {
			side_images_data = this.product_add_service.create_side_img(side_images_list);

			return this.products_updation_dao.add_side_images(product_data.getProduct_name(), product_data.getProduct_uuid(), side_images_data);
		}
		catch ( Exception exp)
		{

			String message = "ERROR ENCOUTER WHILE UPLOADING THE FILES";

			Custom_Response custom_response = new Custom_Response();

			custom_response.setMessage(message);

			custom_response.setStatus(0);

			return custom_response;

		}

	}

}



