package com.products_dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payments.module.Product_Update_Data;
import com.products_module.Products_Images_Updation_Data;
import com.products_module.Products_Price_Update_Data;
import com.products_module.Products_Side_Images_Data;
import com.products_module.Products_Specifications_Update_Data;
import com.products_module.Products_Stock_Update_Data;
import com.security_config.Custom_Response;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Products_Updation_Dao {

	@Autowired
	private Products_Dao products_dao;

	@Autowired
	private Custom_Response custom_response;

	@Autowired
	private JdbcTemplate jdbc_template;
	
	private final String PRODUCT_UPDATION_QUERY = "update products set product_name = '%s' , price = %f  , category_id = %d , stock = %d where product_uuid = '%s' ";

	private final String UPDATE_STOCK_QUERY = "update  products set stock  = ?  where product_uuid = ? AND product_name = ?  ";

	private final String UPDATE_PRICE_QUERY = "update  products set price  = ?  where product_uuid = ? AND product_name = ?  AND price = ? ";

	private final String UPDATE_PRODUCT_SPECIFICATION = "update products set product_specifications  = ? where product_uuid = ?   AND product_name = ? ";

	private final String UPDATE_PRODUCT_IMAGE = "update products set product_image = ?  , product_image_type = ? where product_uuid = ? AND product_name = ?";

	private final int OK = 1;

	private final int NOT_OK = 0;

	Object args[] = null;

	private Custom_Response set_response(String message, int status_code) {

		this.custom_response.setMessage(message);

		this.custom_response.setStatus(status_code);

		return custom_response;

	}
	
	@Transactional
	public Custom_Response update_product_details( Product_Update_Data product_details , List<Products_Side_Images_Data> side_imgs_data , int category_id) throws Exception
	{
		
		String product_name = product_details.getProduct_name();
		
		float price = product_details.getPrice();
		
		int stock = product_details.getStock() ;
		
		String product_uuid = product_details.getProduct_uuid();
		
		String query = String.format( PRODUCT_UPDATION_QUERY , product_name , price , category_id ,  stock , product_uuid );
			
		int product_id = this.products_dao.get_product_id(product_uuid);
		
		
			int rows_effected = jdbc_template.update( query);
			
			
			this.products_dao.insert_product_side_imgs(product_id,side_imgs_data);
			
			this.products_dao.delete_side_images(product_details.getRemoved_imgs_list());
			
			this.custom_response.setMessage("PRODUCT UPDATED SUCCESSFULLY");
			
			this.custom_response.setStatus(1);
			
			return this.custom_response;
		
	}

	public Custom_Response update_stock(Products_Stock_Update_Data product_stock_update_data) {

		int stock = product_stock_update_data.getStock();

		String product_uuid = product_stock_update_data.getProduct_uuid();

		String product_name = product_stock_update_data.getProduct_name();

		args = new Object[] { stock, product_uuid, product_name };

		String message = null;

		try {

			int effected_rows = this.jdbc_template.update(UPDATE_STOCK_QUERY, args);

			if (effected_rows == 1) {

				message = "ONE PRODUCT STOCK IS UPDATED ";

				this.custom_response = set_response(message, OK);

			} else if (effected_rows > 1) {

				message = "MORE THEN ONE PRODUCT_STOCK IS UPDATED";

				this.custom_response = set_response(message, NOT_OK);

			} else {

				message = "PRODUCT_NOT_FOUND WITH THE GIVEN DETAILS";

				this.custom_response = set_response(message, NOT_OK);

			}


		}
		catch (DataAccessException bad_sql_grammer) {

			message = "INCORRECT DATA PROVIDED PLEASE CHECK YOUR DATA";

			this.custom_response = set_response(message, NOT_OK);

		}

		System.out.println(message);

		return custom_response;

	}

	public Custom_Response update_price(Products_Price_Update_Data price_update_data) {
		float new_price = price_update_data.getNew_price();

		float old_price = price_update_data.getOld_price();

		String product_uuid = price_update_data.getProduct_uuid();

		String product_name = price_update_data.getProduct_name();

		args = new Object[] { new_price, product_uuid, product_name, old_price };

		String message = null;

		try {
			int effected_rows = this.jdbc_template.update(UPDATE_PRICE_QUERY, args);

			if (effected_rows == 1) {
				message = "ONE PRODUCT_ PRICE IS UPDATED";

				this.custom_response = set_response(message, OK);

			}

			else if (effected_rows > 1) {
				message = effected_rows + " PRODUCTS  PRICES ARE UPDATED";

				this.custom_response = set_response(message, NOT_OK);
			}

			else {
				message = "PRICE NOT UPDATED PLEASE CHECK DETAILS";
				this.custom_response = set_response(message, NOT_OK);
			}

			System.out.println(message);

		} catch (DataAccessException dao) {
			message = "INVALID DETAILS ";
			this.custom_response = set_response(message, NOT_OK);
		}

		return custom_response;

	}

	public Custom_Response update_prouduct_specifications(
			Products_Specifications_Update_Data specification_update_data) {

		String product_uuid = specification_update_data.getProduct_uuid();

		String product_name = specification_update_data.getProduct_name();

		String new_product_specifications = specification_update_data.getProduct_specifications();

		args = new Object[] { new_product_specifications, product_uuid, product_name };

		String message = null;

		try {

			int effected_rows = this.jdbc_template.update(UPDATE_PRODUCT_SPECIFICATION, args);

			if (effected_rows == 1) {

				message = "ONE PRODUCT_ SPECIFICATION IS UPDATED";

				this.custom_response = set_response(message, OK);

			}

			else if (effected_rows > 1) {

				message = effected_rows + " PRODUCTS  SPECIFICATIONS ARE UPDATED";

				this.custom_response = set_response(message, NOT_OK);

			}

			else {
				message = " PRODUCT_SPECIFICATIONS NOT UPDATED PLEASE CHECK DETAILS";
				this.custom_response = set_response(message, NOT_OK);
			}

			System.out.println(message);


		}

		catch (DataAccessException dao) {

			message = "ERROR WHILE UPDATING THE PRODUCT_SPECIFICATIONS";

			this.custom_response = set_response( message , NOT_OK);

		}

		return custom_response;

	}

	public Custom_Response update_product_images(Products_Images_Updation_Data specification_updation_data) {

		byte[] new_product_image = specification_updation_data.getProduct_image();

		String new_product_image_type = specification_updation_data.getNew_image_type();

		String product_uuid = specification_updation_data.getProduct_uuid();

		String product_name = specification_updation_data.getProduct_name();

		args = new Object[] { new_product_image, new_product_image_type, product_uuid, product_name };

		String message = null;

		try {

			int effected_rows = this.jdbc_template.update(UPDATE_PRODUCT_IMAGE, args);


			if (effected_rows == 1) {

				message = "ONE PRODUCT IMAGE IS UPDATED ";

				this.custom_response = set_response(message, OK);

			} else if (effected_rows > 1) {

				message = "MORE THEN ONE PRODUCT_IMAGE IS UPDATED";

				System.out.println(message);

				this.custom_response = set_response(message, NOT_OK);
			}

			else
			{
				message = "PRODUCT_NOT_FOUND WITH GIVEN DETAILS";

				this.custom_response = set_response(message, NOT_OK);

			}

			System.out.println ( message );

		}

		catch (DataAccessException dao) {

			message = "INVALID PRODUCT_DETAILS";

			this.custom_response = set_response(message, NOT_OK);
		}

		return custom_response;

	}

	public Custom_Response add_side_images(String product_name, String product_uuid, List<Products_Side_Images_Data> side_images_list) throws Exception {

		int product_id = this.products_dao.get_product_id(product_uuid, product_name);

		String message = null;

		try {
			 int effected_rows[] =  this.products_dao.insert_product_side_imgs(product_id, side_images_list);

			 message = "SUCCESSFULLY INSERTED "+effected_rows.length + " SIDE IMAGES";

			 this.custom_response = this.set_response(message, OK);

		}
		catch (Exception e) {

			message = "FAILURE WHILE ADDING PRODUCT_IMAGES";

			this.custom_response = this.set_response(message,NOT_OK);

		}

		return custom_response;

	}

}
