package com.products_module;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Category_Name_Mapper implements RowMapper<String>{

	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {

		return  ( rs.getString("category_name"));
	}

}
