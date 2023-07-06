package com.carts_module.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.carts_module.Carts_Data_Sender;
import com.carts_module.controller.Carts_Receiver_Data_1;
import com.carts_module.controller.Carts_Removal_Data;
import com.carts_module.dao.Carts_Dao;
import com.jwt_service.Jwt_Service;
import com.security_config.Custom_Response;


@Primary
@Service("carts_service")
public class Carts_Service implements CartsServiceInterface {

	@Autowired
	private Carts_Dao carts_dao;

	@Autowired
	private Carts_Insertion_Service carts_insert_service;

	@Autowired
	private Carts_Removal_Service  carts_removal_service;

	@Autowired
	private  Carts_Data_Retrival_Service  carts_retrival_service;

	@Autowired
	private Custom_Response custom_response ;

	@Autowired
	private Jwt_Service  jwt_service;

	public boolean is_valid( int quantity , float total )
	{
		if ( quantity <= 0 || total <= 0 )
		{
			System.out.println ( "QUANITY OR TOTAL SHOLD NOT BE ZERO");
			return false;
		}
		else
		{
			return true;
		}
	}
	// assuming the the user id and the proudct id is the valid
	public Custom_Response insert_into_carts( Carts_Receiver_Data_1 cart_data)
	{
			int quantity  =  cart_data.getQuantity();

			float  total  =  cart_data.getTotal() ;



			if ( !is_valid( quantity , total))
			{

				this.custom_response.setMessage("INVALID QUANTITY OR THE TOTAL VALUE");
				this.custom_response.setStatus( 0);
			}

			else
			{


				this.custom_response = this.carts_insert_service.add_to_carts(cart_data);
			}

			return this.custom_response;
	}

	public  Custom_Response remove_item_from_cart ( Carts_Removal_Data  carts_removal_data )
	{
		return this.carts_removal_service.remove_cart_item(carts_removal_data);

	}

	public boolean remove_all_cart_items( String user_name )
	{
			return this.carts_removal_service.remove_all_cart_item(user_name);
	}

	public List<Carts_Data_Sender> get_cart_item ( String user_name )
	{
		return this.carts_retrival_service.get_cart_items(user_name);
	}

	//this method comes under the updation of the cart quantity
	// no need to create another class for this one method 
	//access the dao from this service layer
	public Custom_Response update_quantity_val( String jwt_token , String product_uuid ,  int quantity ) 
	{
		String user_name  = this.jwt_service.get_username( jwt_token);	
		if ( user_name  == null )
		{
			return null;
		}
		
		Custom_Response cr  = this.carts_dao.update_quantity_val( user_name , product_uuid, quantity);
		return cr ;
	}
}
