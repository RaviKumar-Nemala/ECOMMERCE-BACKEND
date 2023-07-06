package com.products_module;

public class Products_Images_Updation_Data  extends Products_Update_Data{

	private String new_image_type ;

	private byte[]  new_product_image;

	public String getNew_image_type() {
		return new_image_type;
	}

	public void setNew_image_type(String new_image_type) {
		this.new_image_type = new_image_type;
	}

	public byte[] getProduct_image() {
		return new_product_image;
	}

	public void setProduct_image(byte[] product_image) {
		this.new_product_image = product_image;
	}

}


