package com.products_module;

import java.util.List;


//whenever we are trying to store the List<Product_Data_Send> as value  in the cache it is not supported 
//inorder solve this List<> is placed in this class and we use this class as the value in the cache 

public class Products_List_Holder {
	
	List<Products_Data_Send>data;
	
	public Products_List_Holder()
	{
		
	}
	
	public Products_List_Holder(  List<Products_Data_Send > data)
	{	
		this.data = data;
	}

	public List<Products_Data_Send> getData() {
		return data;
	}

	public void setData(List<Products_Data_Send> data) {
		this.data = data;
	}
	
	
}
