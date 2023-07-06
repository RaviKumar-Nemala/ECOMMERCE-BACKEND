package com.products_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Primary_Key_Mapper implements  RowMapper<Integer>{

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

			Integer product_id = rs.getInt("product_id");

			return product_id ;
	}



}
