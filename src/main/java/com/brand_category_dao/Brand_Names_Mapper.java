package com.brand_category_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Brand_Names_Mapper implements RowMapper<String >{

	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			String brand_name = rs.getString("brand_name") ;
			
			return brand_name;
	}
}