package com.products_service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.payments.module.Product_Update_Data;
import com.products_module.Products_Data;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Images_Updation_Data;
import com.products_module.Products_Input_Data;
import com.products_module.Products_Price_Update_Data;
import com.products_module.Products_Specifications_Update_Data;
import com.products_module.Products_Stock_Update_Data;
import com.products_module.Products_Total_Data;
import com.products_module.Products_Update_Data;
import com.security_config.Custom_Response;

@Service
public class Products_Service implements ProductsServiceInterface{

	@Autowired
	Products_Get_Service product_get_service;

	@Autowired
	Products_Add_Service products_add_service;

	@Autowired
	Products_Updation_Service products_updation_service;
	
	@Autowired
	CacheManager cache_manager;
	
	@Qualifier("${cache.images_cache}")
	private String IMAGES_CACHE ;
	
	@Qualifier("${cache.category_cache}")
	private String CATEGORY_BASED_PRODUCTS_CACHE ;
	
	@Qualifier("${cache.products_cache}")
	private String PRODUCT_DETAILS_CACHE;
	
	
	public List<Products_Data_Send>  get_products ( )
	{

		List<Products_Data_Send>product_details =  this.product_get_service.get_products();

		return this.product_get_service.update_uuild_names(product_details);
	}

	public Products_Total_Data  get_product_total_data (  String uuid )
	{
		return this.product_get_service.get_product_total_data(uuid);
		
	}

	public Custom_Response insert_products (

			String product_name ,

			float price ,

			MultipartFile product_spec_file,

			String gender,

			String categroy ,

			MultipartFile product_image  ,

			List<MultipartFile>side_img_files

			) throws IOException

	{

		Products_Data  products_data = new Products_Data() ;


		products_data.setProduct_name( product_name);

		products_data.setPrice(price);

		byte [] product_spec_bytes = product_spec_file.getBytes();

		String product_spec_text = new String ( product_spec_bytes ) ;

		products_data.setProduct_specifications(product_spec_text);

		products_data.setProduct_gender(gender);

		products_data.setCategory(categroy);

		byte[]  product_image_bytes  =  product_image.getBytes();

		String image_type  = product_image.getContentType();

		products_data.setImage_bytes(product_image_bytes);

		products_data.setImage_type(image_type );

		return  this.products_add_service.insert_products(products_data, side_img_files);
	}

	@Cacheable(cacheNames = "images_cache",  key = "#uuid")
	public byte[]  get_product_side_image ( String uuid )
	{
		System.out.println ( "side images uuiid has called");
		return this.product_get_service.get_product_side_image(uuid);
	}
	
	@Cacheable(cacheNames =  "images_cache" ,  key = "#uuid")
	public byte[] get_product_image ( String uuid )
	{
		System.out.println ( "MAIN IMAGE UUID HAS CALLED");
		return  this.product_get_service.get_product_image(uuid);
	}
		public Custom_Response insert_new_product_side_images(  Products_Update_Data  product_data ,  List<MultipartFile> side_images_list)
	{

		Custom_Response insertion_response =  this.products_updation_service.add_new_side_images(product_data, side_images_list);

		return insertion_response;

	}

	public List<Products_Data_Send> get_category_products( String category_name)
	{

		List<Products_Data_Send> products_data = this.product_get_service.get_products(category_name);

		return this.product_get_service.update_uuild_names(products_data);

	}

	public Integer get_category_d (  String category_name )
	{
		return this.product_get_service.get_category_id(category_name);
	}
	
	@Cacheable(cacheNames = "product_details_cache" , key = "#uuid")
	public Products_Data_Send  get_product( String uuid )
	{
		
		Products_Data_Send product_info = product_get_service.get_product(uuid);
		
		if( product_info == null )
				return null;
		
		List<Products_Data_Send>product_info_util = new ArrayList<>();
		product_info_util.add( product_info);
		
		return this.product_get_service.update_uuild_names(product_info_util).get(0);
	}
	
	public  Products_Data_Send get_product ( int product_id )
	{

		 Products_Data_Send products_data =  this.product_get_service.get_product(product_id);

		 List<Products_Data_Send> product_details = new ArrayList<>();

		 product_details.add(products_data);

		 return this.product_get_service.update_uuild_names(product_details).get(0);

	}
	
	
	private void remove_product_from_cache( Product_Update_Data data)
	{
		Cache<Object, Object> img_cache = this.cache_manager.getCache(IMAGES_CACHE);
		
		//remving the deleted side images from the cache
		data.getRemoved_imgs_list().stream().forEach( img_uuid -> 
		{
			img_cache.remove(img_uuid);
		});
		
		
		//removing the product details from the cache
		
		Cache<Object,Object>products_cache =  this.cache_manager.getCache(PRODUCT_DETAILS_CACHE);
		
		products_cache.remove( data.getProduct_uuid());
		
		return ;
	}
	
	public Custom_Response update_product( Product_Update_Data data)
	{
		Custom_Response cr = this.products_updation_service.update_product_details(data);
		
		remove_product_from_cache( data );
		
		return cr;
	}

	@Override
	public Custom_Response insert_singe_record(Products_Input_Data input_data) {
		Custom_Response cr = new Custom_Response ();
		try {
		 cr = this.products_add_service.insert_singe_record(input_data);
		}
		catch ( Exception  e )
		{
			System.out.println ( e.getMessage () );
		}
		return cr;
	}

	@Override
	public List<Products_Data_Send> send_products_data(List<Products_Total_Data> total_info) {
		
		return this.product_get_service.send_products_data(total_info);
	}

	@Override
	public List<Products_Total_Data> get_products(List<String> uuids) {
		return this.product_get_service.get_products(uuids);
	}

	@Override
	public int get_category_id(String category_name) {
		
		return this.product_get_service.get_category_id(category_name);
	}

	@Override
	public int get_product_id(String product_uuid) {
		return this.product_get_service.get_product_id(product_uuid);
	}
}
