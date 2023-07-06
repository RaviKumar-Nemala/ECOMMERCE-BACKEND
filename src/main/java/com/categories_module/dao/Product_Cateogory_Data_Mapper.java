package com.categories_module.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.categories_module.Product_Category_Data;


public class Product_Cateogory_Data_Mapper implements RowMapper<Product_Category_Data> {

	@Override
	public Product_Category_Data mapRow(ResultSet rs, int rowNum) throws SQLException {

		Product_Category_Data product_category_data = new Product_Category_Data() ;

		product_category_data.setCategory_name(rs.getString("category_name"));

		product_category_data.setImage_bytes(rs.getBytes("category_thumb_nail"));

		product_category_data.setImage_type(rs.getString("category_image_type"));

		return product_category_data;

	}
}


