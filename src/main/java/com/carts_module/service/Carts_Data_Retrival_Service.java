package com.carts_module.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carts_module.Carts_Data;
import com.carts_module.Carts_Data_Sender;
import com.carts_module.dao.CartsDaoInterface;
import com.carts_module.dao.Carts_Dao;
import com.products_module.Products_Data_Send;
import com.products_service.Products_Service;
import com.products_service.ProductsServiceInterface;

@Service
public class Carts_Data_Retrival_Service {

	@Autowired
	private CartsDaoInterface cartsDaoInterface;

	@Autowired
	private ProductsServiceInterface products_service_interface;


	private List<Carts_Data_Sender  > wrap_cart_items ( List< Carts_Data> carts_data)
	{

		List< Carts_Data_Sender > total_cart_details =  new ArrayList<>();

		for ( Carts_Data cart_item :  carts_data)
		{

			Carts_Data_Sender carts_data_sender = new Carts_Data_Sender();
			int  product_id = cart_item.getProduct_id() ;
			int quantity =  cart_item.getQuantity() ;
			Products_Data_Send  products_data = this.products_service_interface.get_product(  product_id );

			carts_data_sender.setQuantity(quantity);
			carts_data_sender.setProducts_details(products_data);
			total_cart_details.add(  carts_data_sender );
		}
		return total_cart_details;
	}
	
	public	List<Carts_Data_Sender > get_cart_items ( String username )
	{
		List < Carts_Data > carts_data =  this.cartsDaoInterface.get_cart_items(username);
		return  this.wrap_cart_items(carts_data);
	}


}
