package com.categories_module;

import org.springframework.stereotype.Component;

@Component("product_category_data")
public class Product_Category_Data {

	public String category_name ;

	public  byte[] image_bytes;

	public String image_type;

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public byte[] getImage_bytes() {
		return image_bytes;
	}

	public void setImage_bytes(byte[] image_bytes) {
		this.image_bytes = image_bytes;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}


}
