package com.orders.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Order_Id_Mapper implements RowMapper<Integer >{

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Integer order_id =  rs.getInt( "order_id" );
		
		return order_id;
	}
	
}
