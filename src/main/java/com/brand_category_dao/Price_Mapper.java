package com.brand_category_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Price_Mapper implements RowMapper<Float>{

	@Override
	public Float mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Float price  = rs.getFloat("price");
			
		return price ; 
	
	}

}
