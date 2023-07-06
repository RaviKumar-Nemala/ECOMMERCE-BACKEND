package com.payments.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Order_Key_Mapper implements RowMapper<Integer>
{

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		Integer  p_key = rs.getInt("p_key");
		
		return p_key ;
		
	}
		
	
	
}
