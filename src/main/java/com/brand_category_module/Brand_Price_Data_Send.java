package com.brand_category_module;

import java.util.List;

import org.springframework.stereotype.Component;



@Component
public class Brand_Price_Data_Send {

	List<String>brand_names ;
	
	List<String>price_ranges;

	
	public List<String> getPrice_ranges() {
		return price_ranges;
	}

	public void setPrice_ranges(List<String> price_ranges) {
		this.price_ranges = price_ranges;
	}

	public List<String> getBrand_names() {
		return brand_names;
	}

	public void setBrand_names(List<String> brand_names) {
		this.brand_names = brand_names;
	}
	
}
