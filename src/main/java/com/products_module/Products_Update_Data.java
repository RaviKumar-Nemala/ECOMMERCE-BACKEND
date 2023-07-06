package com.products_module;

public class Products_Update_Data {


	protected String  product_name;


	protected String product_uuid ;

	public Products_Update_Data()
	{

	}

	public Products_Update_Data( String product_name , String product_uuid )
	{
		this.product_name = product_name;
		this.product_uuid =  product_uuid;
	}

	public String getProduct_name() {
		return product_name;
	}


	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	public String getProduct_uuid() {
		return product_uuid;
	}


	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}


}
