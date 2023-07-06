package com.products_module;

public class Products_Price_Update_Data extends Products_Update_Data{


	private float new_price ;

	private float  old_price;

	public float getNew_price() {
		return new_price;
	}

	public void setNew_price(float new_price) {
		this.new_price = new_price;
	}

	public float  getOld_price() {
		return old_price;
	}

	public void setOld_price(float  old_price) {
		this.old_price = old_price;
	}


}
