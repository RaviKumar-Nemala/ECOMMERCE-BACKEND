package com.carts_module.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.carts_module.Carts_Data;

public class Carts_Details_Row_Mapper implements RowMapper< Carts_Data> {

	@Override
	public  Carts_Data mapRow(ResultSet rs, int rowNum) throws SQLException {

		Carts_Data carts_data = new Carts_Data();

		carts_data.setUser_id( rs.getInt("user_id"));

		carts_data.setProduct_id(rs.getInt("product_id"));

		carts_data.setQuantity(rs.getInt("quantity"));

		carts_data.setTotal(rs.getFloat("total"));

		return carts_data;
	}


}
