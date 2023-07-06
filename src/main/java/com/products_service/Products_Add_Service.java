package com.products_service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brand_category_dao.Brand_Category_Dao;
import com.brands_dao.Brand_Dao;
import com.products_dao.ProductsDaoInterface;
import com.products_dao.Products_Dao;
import com.products_module.Products_Data;
import com.products_module.Products_Input_Data;
import com.products_module.Products_Side_Images_Data;
import com.security_config.Custom_Response;

@Service
public class Products_Add_Service {
	@Autowired
	ProductsDaoInterface productsDaoInterface;

	@Autowired
	Product_Text_Helper text_validator;

	@Autowired
	Brand_Category_Dao brand_category_dao;
	
	@Autowired
	Brand_Dao brand_dao;
	
	@Autowired
	Custom_Response response;
	
	List<String> ACCEPTED_IMG_FORMATS = Arrays.asList("image/jpeg","image/jpg","image/png","image/webp");
	
	public Float to_float( String price )
	{
		 try
		 {
			 float f = Float.parseFloat(price);
			 if ( f < 0 )
			 {
				 return null;
			 }
			 
			 return f;
		 }
		 catch( Exception e )
		 {
			 return null;
		 }
	}
	
	public Custom_Response insert_singe_record( Products_Input_Data data) throws IOException
	{
		
		String product_name = new String(data.getProduct_name().getBytes());		
		String price = new String (data.getPrice().getBytes());
		String category_name =  data.getCategory_name();
		String brand_name = data.getBrand_name() ;
		String product_specifications = new String( data.getProduct_specifications().getBytes()) ;
		List<MultipartFile>side_imgs = data.getSide_images();
		MultipartFile main_img = data.getMain_image();
		int category_id =  productsDaoInterface.get_category_id(category_name);
		
		int brand_id = brand_dao.get_brand_id(brand_name);
		
		
		System.out.println(main_img.getContentType());
		
		if ( price == null || to_float( price) == null )
		{
			response.setMessage("INVALID PRICE VALUE");
			response.setStatus(0);
			return response;
		}
		else if ( category_id == -1)
		{
			response.setMessage("INVALID CATEGORY VALUE");
			response.setStatus(0);
			return response;
		}
		else if ( brand_id  == -1  )
		{
			response.setMessage("INVAID BRAND NAME");
			response.setStatus(0);
			return response;
		}
		else if(  !validate_specifications(product_specifications))
		{
			response.setMessage("INVALID SPECIFICATIONS FORMAT");
			response.setStatus(0);
			return response;
		} else if( ACCEPTED_IMG_FORMATS.indexOf(main_img.getContentType()) == -1)
		{
			response.setMessage("INVALID IMAGE FORMAT \n ACCEPTED IMAGE FORMATS ARE JPG,PNG, WEBP ");
			response.setStatus(0);
			return response;
		}
		
		else if ( side_imgs!= null && side_imgs.size () >= 1 )
		{
			boolean invalid = false ;
			
			for( MultipartFile ele : side_imgs)
			{
				if(  !ACCEPTED_IMG_FORMATS.contains(ele.getContentType()))
				{
					response.setMessage("INVALID IMAGE FORMAT \n ACCEPTED IMAGE FORMATS ARE JPG,PNG, WEBP ");
					invalid = true;
					break;
				}
			}
			
			if( invalid == true)
			{
				response.setMessage("INVALID SIDE IMAGES");
				response.setStatus(0);
				return response;				
			}	
		}		
		try 
		{
			Float price_val = Float.parseFloat(price);
			int stock = Integer.parseInt(data.getStock_val());
			this.productsDaoInterface.add_products(data,price_val , stock , category_id , brand_id);	
		}
		catch ( DataIntegrityViolationException  dae )
		{
			 this.response.setMessage("PRODUCT ALREADY INSERTED");
			 this.response.setStatus(0);
			 return this.response;
		}
		catch ( Exception  e )
		{	
			this.response.setMessage("SOMETHING WENT WRONG WHILE INSERTING THE PRODUCT");
			this.response.setStatus(0);
			return response;
		}
		this.response.setMessage("PRODUCT_INSERTED SUCCESSFULLY");
		this.response.setStatus(1);
		return response;	
	}
	
	public boolean validate_specifications (  String spec)
	{

		boolean result = true ;

		try {
			this.text_validator.convert__to_text( spec);
		}

		catch( Exception e )
		{
			System.out.println ( e.getMessage());
			result  = false;
		}
		return result ;
	}

	public List<Products_Side_Images_Data>create_side_img(  List<MultipartFile> side_img_files) throws IOException
	{

		if ( side_img_files == null || side_img_files.size() == 0 )
				return null;

		int size = side_img_files .size();

		List<Products_Side_Images_Data > product_side_img_list = new ArrayList<>();

		for ( int i = 0 ;i <size ; i ++)
		{
				Products_Side_Images_Data product_side_img = new Products_Side_Images_Data ();
				byte[] img_bytes = side_img_files.get(i).getBytes();
				String image_type = side_img_files.get(i).getContentType();
				product_side_img.setSide_images(img_bytes);
				product_side_img.setImage_type(image_type);
				product_side_img_list.add( product_side_img);
		}

		return product_side_img_list;

	}

	public Custom_Response  insert_products( Products_Data product_data  , List<MultipartFile> side_img_files )
	{

		boolean result = validate_specifications(product_data.getProduct_specifications());
		
		Custom_Response pr = new Custom_Response();

		if ( !result)
		{
			pr.setMessage("INVALID SYNTAX AT PRODUCT SPECIFICATIONS");
			pr.setStatus(0);

			return pr;
		}

		List<Products_Side_Images_Data> side_images_data = null;

			try
		{
					side_images_data = this.create_side_img(side_img_files);
		}
			catch( Exception e )
			{
				System.out.println ( e.getMessage());
			}	
		if( side_images_data != null) {
			pr = this.productsDaoInterface.add_products(product_data, side_images_data);
		}
		return null;
	}
}
