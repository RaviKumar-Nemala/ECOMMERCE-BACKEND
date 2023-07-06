package com.brand_category_module;

import java.util.List;

public class Brand_Price_Input {

	List< String >brand_names ;
	
	List<Float>price_ranges ;
	
	String category_name ;

	public List<String> getBrand_names() {
		return brand_names;
	}

	public void setBrand_names(List<String> brand_names) {
		this.brand_names = brand_names;
	}

	public List<Float> getPrice_ranges() {
		return price_ranges;
	}

	public void setPrice_ranges(List<Float> price_ranges) {
		this.price_ranges = price_ranges;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}



}
