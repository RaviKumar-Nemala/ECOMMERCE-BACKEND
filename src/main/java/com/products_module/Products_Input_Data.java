package com.products_module;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Products_Input_Data {
	
	MultipartFile product_name;
	
	MultipartFile price; 
	
	MultipartFile main_image ;
	
	List<MultipartFile> side_images;
	
	MultipartFile product_specifications;
	
	String category_name;
	
	String brand_name ;
	
	String stock_val;

	String gender ;
	
	public MultipartFile getProduct_name() {
		return product_name;
	}

	public void setProduct_name(MultipartFile product_name) {
		this.product_name = product_name;
	}

	public MultipartFile getPrice() {
		return price;
	}

	public void setPrice(MultipartFile price) {
		this.price = price;
	}

	public List<MultipartFile> getSide_images() {
		return side_images;
	}

	public void setSide_images(List<MultipartFile> side_images) {
		this.side_images = side_images;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public MultipartFile getMain_image() {
		return main_image;
	}

	public MultipartFile getProduct_specifications() {
		return product_specifications;
	}

	public void setProduct_specifications(MultipartFile product_specifications) {
		this.product_specifications = product_specifications;
	}

	public void setMain_image(MultipartFile main_image) {
		this.main_image = main_image;
	}

	
	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String  getStock_val() {
		return stock_val;
	}

	public void setStock_val(String  stock_val) {
		this.stock_val = stock_val;
	}
	
}
