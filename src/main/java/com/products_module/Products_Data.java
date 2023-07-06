package com.products_module;


public class Products_Data {

	private String product_name ;

	private byte[] image_bytes;

	private String image_type;

	private float price;

	private String product_specifications;

	private String product_gender;

	private String category ;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProduct_specifications() {
		return product_specifications;
	}

	public void setProduct_specifications(String product_specifications) {
		this.product_specifications = product_specifications;
	}

	public String getProduct_gender() {
		return product_gender;
	}

	public void setProduct_gender(String product_gender) {
		this.product_gender = product_gender;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public byte[] getImage_bytes() {
		return image_bytes;
	}

	public void setImage_bytes(byte[] image_bytes) {
		this.image_bytes = image_bytes;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}


}
