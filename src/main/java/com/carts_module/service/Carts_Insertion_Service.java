package com.carts_module.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carts_module.Carts_Data;
import com.carts_module.controller.Carts_Receiver_Data_1;
import com.carts_module.dao.CartsDaoInterface;
import com.carts_module.dao.Carts_Dao;
import com.jwt_checking.Jwt_Util;
import com.products_service.ProductsServiceInterface;
import com.security_config.Custom_Response;
import com.user_login_module.dao.User_Module_Dao_Impl;


@Service
public class Carts_Insertion_Service {

	@Autowired
	private ProductsServiceInterface productsServiceInterface;

	@Autowired
	private User_Module_Dao_Impl user_dao;

	@Autowired
	private CartsDaoInterface cartsDaoInterface;

	@Autowired
	private Custom_Response custom_response;

	@Autowired
	private Jwt_Util jwt_util;


	private final int OK = 1 ;

	private final int NOT_OK = 0 ;


	public int get_product_id ( String product_uuid )
	{

		return  this.productsServiceInterface.get_product_id(product_uuid);
	}

	public int get_user_id ( String jwt_token )
	{

		String user_name =  jwt_util.extractUsername(jwt_token);

		return this.user_dao.get_user_id(user_name);

	}

	public Custom_Response add_to_carts (  Carts_Receiver_Data_1 carts_data)
	{

		int product_id = get_product_id ( carts_data.getProduct_uuid() );

		int user_id = get_user_id( carts_data.getJwt_token());

		if ( product_id < 0 || user_id < 0 )
		{
			System.out.println ( "INSIDE OF THE ADD_TO_CARTS \n INVALID PRODUCT_ID OR USER_ID");

			this.custom_response.setMessage( "INVALID USER_ID OR PRODUCT_UUID");

			this.custom_response.setStatus( NOT_OK);

			return custom_response;
		}

		Carts_Data carts_insertion_data = new Carts_Data();
		carts_insertion_data.setProduct_id(product_id);
		carts_insertion_data.setUser_id(user_id);
		carts_insertion_data.setQuantity( carts_data.getQuantity());
		carts_insertion_data.setTotal( carts_data.getTotal());

		String user_id_cart_id = user_id + "#" + product_id ;
		carts_insertion_data.setUser_id_product_id(user_id_cart_id);

		return this.cartsDaoInterface.add_to_cart(carts_insertion_data);

	}


}
