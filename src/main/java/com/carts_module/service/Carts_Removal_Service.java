package com.carts_module.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carts_module.controller.Carts_Removal_Data;
import com.carts_module.dao.CartsDaoInterface;
import com.jwt_checking.Jwt_Util;
import com.products_dao.Products_Dao;
import com.products_service.ProductsServiceInterface;
import com.security_config.Custom_Response;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Service
public class Carts_Removal_Service {

	@Autowired
	private ProductsServiceInterface productsServiceInterface;

	@Autowired
	private User_Module_Dao_Impl user_dao;

	@Autowired
	private CartsDaoInterface cartsDaoInterface;

	@Autowired
	private Jwt_Util jwt_util;

	public Custom_Response  remove_cart_item( Carts_Removal_Data cart_removal_data )
	{
		String user_name = jwt_util.extractUsername( cart_removal_data.getJwt_token() );
		int user_id = user_dao.get_user_id(user_name);
		int product_id = productsServiceInterface.get_product_id( cart_removal_data.getProduct_uuid());
		return this.cartsDaoInterface.remove_cart_item(user_id, product_id);
	}
	
	public boolean remove_all_cart_item( String user_name )
	{
			int user_id = user_dao.get_user_id(user_name);
			return this.cartsDaoInterface.remove_all_cart_items(user_id);
	}

}
