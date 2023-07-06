package com.products_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Catergory_Id_Mapper implements RowMapper<Integer> {

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

		Integer category_id = rs.getInt("category_id");

		return category_id;
	}




}
