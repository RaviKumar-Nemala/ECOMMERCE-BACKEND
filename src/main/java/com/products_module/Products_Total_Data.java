package com.products_module;


public class Products_Total_Data {

	int product_id ;

	private String product_name ;

	private float price;

	private String product_specification;

	private String gender ;

	private String product_uuid ;

	private String image_type;

	private int stock ;

	private int category_id ;

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
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

	public String getProduct_specification() {
		return product_specification;
	}

	public void setProduct_specification(String product_specification) {
		this.product_specification = product_specification;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProduct_uuid() {
		return product_uuid;
	}

	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}



}
