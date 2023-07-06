package com.orders.dao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.orders.module.Order_Items_Total_Data;

public class Order_Items_Mapper implements RowMapper<Order_Items_Total_Data>{

	@Override
	public Order_Items_Total_Data mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Order_Items_Total_Data data = new Order_Items_Total_Data();
		
		data.setProduct_id(rs.getInt("product_id"));

		data.setProduct_name(rs.getString("product_name"));

		data.setPrice(rs.getFloat("price"));

		data.setProduct_specification(rs.getString("product_specifications"));

		data.setGender(rs.getString("gender"));

		data.setCategory_id(rs.getInt("category_id"));

		data.setProduct_uuid(rs.getString("product_uuid") );

		data.setImage_type(rs.getString("product_image_type"));

		data.setStock( rs.getInt("stock"));
		
		data.setOrder_date(rs.getString("ord_date"));
		
		data.setOrder_time(rs.getString("ord_time"));
		
		System.out.println ( data.getOrder_date() );
		
		System.out.println ( data.getOrder_time());
		
		return data;
		
	}

}
