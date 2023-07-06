package com.products_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.products_module.Products_Total_Data;

public class Products_Data_Mapper implements RowMapper<Products_Total_Data> {

	@Override
	public  Products_Total_Data mapRow(ResultSet rs, int rowNum) throws SQLException {

			Products_Total_Data product_total_data = new Products_Total_Data();

			product_total_data.setProduct_id(rs.getInt("product_id"));

			product_total_data.setProduct_name(rs.getString("product_name"));

			product_total_data.setPrice(rs.getFloat("price"));

			product_total_data.setProduct_specification(rs.getString("product_specifications"));

			product_total_data.setGender(rs.getString("gender"));

			product_total_data.setCategory_id(rs.getInt("category_id"));

			product_total_data.setProduct_uuid(rs.getString("product_uuid") );

			product_total_data.setImage_type(rs.getString("product_image_type"));

			product_total_data.setStock( rs.getInt("stock"));


			return product_total_data;
	}


}
