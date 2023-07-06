package com.brand_category_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.brand_category_module.Brand_Category_Names;

public class Brand_Category_Names_Mapper implements RowMapper<Brand_Category_Names> {

	@Override
	public Brand_Category_Names mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		Brand_Category_Names  details = new Brand_Category_Names();
		
		details.setBrand_name(  rs.getString("brand_name"));
		
		details.setCategory_name(rs.getString("category_name"));
		
		return details;
	}

	

}
