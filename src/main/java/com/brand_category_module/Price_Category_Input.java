package com.brand_category_module;

import java.util.List;

/**
 * 
 * @author Ravi kumar
 *
 *helpful for retriving the taking the price array from front end inorder to sort out records
 */
public class Price_Category_Input {

	List<Float>price_ranges;

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

	String category_name;
	
	
}
