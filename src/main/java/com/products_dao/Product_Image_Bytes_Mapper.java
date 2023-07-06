package com.products_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Product_Image_Bytes_Mapper implements RowMapper<byte[]> {

	@Override
	public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {

		byte image_bytes[]   = rs.getBytes("product_image") ;

		return image_bytes;

	}

}
