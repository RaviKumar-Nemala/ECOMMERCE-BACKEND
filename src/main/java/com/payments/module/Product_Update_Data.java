package com.payments.module;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
public class Product_Update_Data {
	
	String product_name;
	
	float price;
	
	String category;
	
	int stock;
	
	String product_uuid;
	
	// contians the uuid values for the images which are  removed by the admin
	List<String>removed_imgs_list;
	
	//contains the newly added images 
	List<MultipartFile>newly_added_side_imgs  ;
	
	
	public List<String> getRemoved_imgs_list() {
		return removed_imgs_list;
	}

	public void setRemoved_imgs_list(List<String> removed_imgs_list) {
		this.removed_imgs_list = removed_imgs_list;
	}

	
	public List<MultipartFile> getNewly_added_side_imgs() {
		return newly_added_side_imgs;
	}

	public void setNewly_added_side_imgs(List<MultipartFile> newly_added_side_imgs) {
		this.newly_added_side_imgs = newly_added_side_imgs;
	}

	Product_Update_Data()
	{
		
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getProduct_uuid() {
		return product_uuid;
	}

	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}

	
}
