package com.brands_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Brand_Data_Mapper implements RowMapper<String>{

	@Override
	public  String mapRow(ResultSet rs, int rowNum) throws SQLException {			
		return rs.getString("brand_name");
	}

}
