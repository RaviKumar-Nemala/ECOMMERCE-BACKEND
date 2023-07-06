package com.categories_module.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.categories_module.Category_Update_Data;
import com.categories_module.Product_Category_Data;
import com.products_module.Category_Name_Mapper;
import com.security_config.Custom_Response;

@Primary
@Repository("category_dao")
public class Product_Category_Dao {

	@Autowired
	private JdbcTemplate jdbc_template;

	private String  sql  ;
	
	private static final String CATEGORY_NAMES_EXTRACTOR = "select  category_name from categories  ";
	
	private static final String  CATEGORY_DETAILS_EXTRACTOR = "select * from categories";
	
	private static final String UPDATE_CATEGORY_NAME  = "update categories  set category_name =  ?  where category_name = ?";
	
	private static final String CATEGORY_INSERTION_QUERY = "insert into categories ( category_name , category_thumb_nail , category_image_type) values ( ? , ? , ?  ) ";

	//updates the  category based on the category name
	public boolean update_product_category ( Category_Update_Data data)
	{

		boolean update_result  = false;

		Object arr[] = { data.getNew_category_name() , data.getOld_category_name()};
		int result = -1 ;

		try
		{
			result = this.jdbc_template.update( UPDATE_CATEGORY_NAME , arr);
		}
		catch ( DataAccessException dae)
		{
			dae.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		if ( result > 0 )
			update_result = true;
		else
			update_result = false;

		return update_result;

	}

	public  Custom_Response insert_product_category (  Product_Category_Data product_category_data)
	{
		Custom_Response  cr  = new Custom_Response();
		
		String message = new String();

		Object args[]  = { product_category_data.getCategory_name() , product_category_data.getImage_bytes() ,  product_category_data.getImage_type()};

		int result  =  -1 ;

	try {
		result = this.jdbc_template.update( CATEGORY_INSERTION_QUERY , args);
		message ="RECORD INSERTED SUCCESSFULLY";
		cr.setStatus(1);
		cr.setMessage(message);
		return cr;
	}
	
	catch ( DataIntegrityViolationException dae)
	{
		message = "CATEGORY NAME ALREADY EXISTED";
	}

	catch ( DataAccessException data_access_exp)
	{
		message ="INVALID CATEGORY NAME ";
	}
	catch ( Exception  e )
	{
		message ="INVALID CATEGORY NAME";
	}
		cr.setStatus(0);
		cr.setMessage(message);
		return cr;
	}

	public List< Product_Category_Data >  get_category_items ( )
	{
			List<Product_Category_Data>  category_list =  null ;
			try {
			category_list = this.jdbc_template.query( CATEGORY_DETAILS_EXTRACTOR,  new Product_Cateogory_Data_Mapper());
			}

			catch ( DataAccessException data_exp)
			{
				System.out.println(data_exp.getMessage());
			}

			catch ( Exception  e )
			{
				System.out.println ( e.getMessage());
			}

			return category_list;
	}
	
	public List<String> get_category_names ( )
	{	
		try 
		{
			List<String> category_names =this.jdbc_template.query( CATEGORY_NAMES_EXTRACTOR, new Category_Name_Mapper());	
			return category_names ;
		}
		catch( DataAccessException  exp )
		{
			return null;
		}
	}
	
}
