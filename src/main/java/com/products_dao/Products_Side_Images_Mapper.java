package com.products_dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.products_module.Products_Side_Image;

public class Products_Side_Images_Mapper implements RowMapper<Products_Side_Image> {

	@Override
	public Products_Side_Image mapRow(ResultSet rs, int rowNum) throws SQLException {

		Products_Side_Image side_img = new Products_Side_Image() ;

		side_img.setProduct_uuid(rs.getString("product_uuid") ) ;

		return side_img;
	}


}
