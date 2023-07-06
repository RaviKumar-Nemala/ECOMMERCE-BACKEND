package com.carts_module;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.products_module.Products_Data_Send;
/**
 *
 *
 * @author Ravi kumar
 *
 *	This class is used for sending cart details to the front end
 *
 *	it contains one extra attr which is product quantity that is stored in carts of that particular user
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Carts_Data_Sender{

	int quantity ;


	Products_Data_Send products_details;

	public Products_Data_Send getProducts_details() {
		return products_details;
	}

	public void setProducts_details(Products_Data_Send products_details) {
		this.products_details = products_details;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
