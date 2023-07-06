package com.user_login_module.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.user_login_module.User_Info;

public class User_Module_User_Details_Mapper implements RowMapper<User_Info> {

	@Override
	public User_Info mapRow(ResultSet rs, int rowNum) throws SQLException {


		User_Info user_info = new User_Info();

		user_info.setUser_name(rs.getString("username"));

		user_info.setPassword(rs.getString("password"));
		System.out.println("inside of the row mapper" +rs.getString("password"));

		user_info.setNick_name( rs.getString("nickname"));

		user_info.setAuthorities(rs.getString("authority"));

		user_info.setAccount_active(rs.getInt("account_active"));


		return user_info;

	}

	}
