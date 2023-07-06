package com.input_search_module;

import java.util.List;
import java.util.Map;


/**
 * 
 * @author Bhanusivateja
 *	this class is useful for sending the brand and categories names to frontend
 *	used for auto completion on the search bar
 *	eg : for categories of laptops :  list<string> contains => laptops , laptops under 20,000 , laptops under 30,00 etc
 */
public class Brand_Category_Data_Send {

	
	Map<String , String>brands;

	Map<String , List<String>>categories;

	public Map<String, String> getBrands() {
		return brands;
	}

	public void setBrands(Map<String, String> brands) {
		this.brands = brands;
	}

	public Map<String, List<String>> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, List<String>> categories) {
		this.categories = categories;
	}
	
	
}
