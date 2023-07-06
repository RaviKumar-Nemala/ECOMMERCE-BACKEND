package com.user_login_module.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Userid_Mapper implements RowMapper<Integer>{

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

		Integer user_id =  rs.getInt("user_id");

		return user_id;

	}

}
