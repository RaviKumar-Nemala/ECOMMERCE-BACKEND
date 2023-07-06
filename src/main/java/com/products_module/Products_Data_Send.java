package com.products_module;

import java.util.List;
import java.util.Map;

public class Products_Data_Send {

	private String product_name ;

	private float price;

	private Map< String , String > product_specification;

	private String gender ;

	private String product_uuid ;

	private List<Products_Side_Image>  product_side_images;

	private int stock ;
	
	private String brand_name ;
	
	public Products_Data_Send( Products_Data_Send pr)
	{
		this.product_name =  pr.getProduct_name();
		this.price = pr.getPrice();
		this.product_specification = pr.getProduct_specification();
		this.gender =  pr.getGender();
		this.product_uuid =  pr.getProduct_uuid();
		this.product_side_images = pr.getProduct_side_images();
		this.stock = pr.getStock();
		this.brand_name =  pr.getBrand_name();
		this.category_name= pr.getCategory_name();
	}
	
	public Products_Data_Send () 
	{
		
	}
	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	private String category_name ;

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

	public Map<String, String> getProduct_specification() {
		return product_specification;
	}

	public void setProduct_specification(Map<String, String> product_specification) {
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



	public List<Products_Side_Image> getProduct_side_images() {
		return product_side_images;
	}

	public void setProduct_side_images(List<Products_Side_Image> product_side_images) {
		this.product_side_images = product_side_images;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}




}
