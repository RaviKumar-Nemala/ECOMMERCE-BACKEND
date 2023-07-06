package com.products_service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.brand_category_dao.Brand_Category_Dao;
import com.products_dao.ProductsDaoInterface;
import com.products_dao.Products_Dao;
import com.products_module.Products_Data_Send;
import com.products_module.Products_Side_Image;
import com.products_module.Products_Total_Data;

@Service
public class Products_Get_Service {

	@Autowired
	private ProductsDaoInterface productDaoInterface;

	@Autowired
	private Product_Text_Helper product_text_helper;

	@Autowired
	private Brand_Category_Dao brand_category_dao;
	
	private List< Products_Side_Image> get_product_side_images (  int  product_id )
	{
		List< Products_Side_Image >product_side_images = this.productDaoInterface.get_product_side_images(product_id);
		return product_side_images;
	}

	private  Products_Data_Send util_helper ( Products_Total_Data  total_data) throws Exception
	{

		Products_Data_Send data_send = new Products_Data_Send() ;
		int product_id = total_data.getProduct_id();
		List< Products_Side_Image> product_side_imgs = this.get_product_side_images(product_id) ;
		String category_name =  this.productDaoInterface.get_category_name( total_data.getCategory_id());
		String brand_name = this.brand_category_dao.get_brand_name ( product_id );
		
		data_send.setProduct_name( total_data.getProduct_name());
		data_send.setPrice(total_data.getPrice());
		data_send.setGender( total_data.getGender());
		data_send.setCategory_name(category_name);
		data_send.setProduct_uuid(total_data.getProduct_uuid());
		data_send.setBrand_name(brand_name);
		data_send.setProduct_side_images( product_side_imgs);
		data_send.setStock(total_data.getStock());

		Map< String, String >  text_data = this.product_text_helper.convert__to_text(total_data.getProduct_specification());
		data_send.setProduct_specification(text_data);
		return data_send;
	}
	public List<Products_Data_Send> send_products_data ( List<Products_Total_Data> products_total_data)
	{
		List<Products_Data_Send> products_sending_data =  util ( products_total_data);
		return this.update_uuild_names(products_sending_data);
	}

	public  List< Products_Data_Send >  util ( List<Products_Total_Data>  product_total_data)
	{
		List< Products_Data_Send> products_data  = new ArrayList<>();
		for ( Products_Total_Data total_data : product_total_data)
		{

		Products_Data_Send product_data_send = null;
		try {
			product_data_send = this.util_helper(total_data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		products_data.add( product_data_send);

		}
		return products_data;
	}
	public List<Products_Side_Image> update_side_img_uuids (List<Products_Side_Image>side_imgs )
	{
		if ( side_imgs == null)
			return null;

		List<Products_Side_Image>uuids_list = new ArrayList<>();

		for ( Products_Side_Image uuid : side_imgs )
		{
			String update_uuid  = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/products/get_side_image/")
					.path(uuid.getProduct_uuid())
					.toUriString();
			uuid.setProduct_uuid(update_uuid);
			uuids_list.add(uuid);
		}
		return uuids_list;
	}

	public List<Products_Data_Send >update_uuild_names ( List<Products_Data_Send> products_data_list)
	{

		for( Products_Data_Send products_data : products_data_list)
		{
			String uuid = products_data.getProduct_uuid() ;

			uuid = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/products/get_image/")
					.path(uuid)
					.toUriString();

			products_data.setProduct_uuid(uuid);

			List< Products_Side_Image> side_uuids = products_data.getProduct_side_images();
			products_data.setProduct_side_images( update_side_img_uuids(side_uuids) ) ;
		}
		return products_data_list;
	}

	public List<Products_Data_Send>  get_products(  )
	{
		List<Products_Total_Data> products_total_data = this.productDaoInterface.get_product_deatails();
		return util  ( products_total_data);
	}

	public int get_category_id (  String category_name )
	{
		return this.productDaoInterface.get_category_id(category_name);
	}

	public List<Products_Data_Send> get_products(String category_name)
	{
		List<Products_Total_Data> products_total_data = this.productDaoInterface.get_category_based_products(category_name);
		return util  ( products_total_data);
	}
	
	public List<Products_Total_Data> get_products( List<String> uuids)
	{
		List<Products_Total_Data> products_total_data = this.productDaoInterface.get_products(uuids);
			
		return products_total_data;		
	}

	public Products_Data_Send get_product( int product_id)
	{

		Products_Total_Data products_total_data =this.productDaoInterface.get_product(product_id) ;
		List< Products_Total_Data> products_data = new ArrayList<>();
		products_data.add(products_total_data);
		return util( products_data ).get( 0 );
	}
	
	public int get_product_id ( String uuid )
	{
		return this.productDaoInterface.get_product_id(uuid);
		
	}
	public Products_Total_Data  get_product_total_data (  String uuid )
	{
		int product_id = this.productDaoInterface.get_product_id(uuid);
		
		return this.productDaoInterface.get_product(product_id);
		
	}
	public Products_Data_Send get_product( String uuid )
	{
		int product_id  =  this.productDaoInterface.get_product_id(uuid);
		
		return this.get_product(product_id);
	}

	public byte[] get_product_image(  String uuid )
	{
		return this.productDaoInterface.get_product_image(uuid);
	}

	public byte[ ] get_product_side_image( String uuid)
	{
		return this.productDaoInterface.get_side_image(uuid);
	}

}
