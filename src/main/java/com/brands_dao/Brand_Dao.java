package com.brands_dao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.brand_category_module.Brand_Update_Data;
import com.products_dao.Products_Data_Mapper;
import com.products_module.Products_Total_Data;
import com.security_config.Custom_Response;

@Repository
public class Brand_Dao {

	@Autowired
	private JdbcTemplate jdbc_template;

	private static final String EXTRACT_BRANDS = "select * from brand";

	private static final String EXTRACT_BRAND_ID = "select brand_id from brand where brand_name = '%s'";
	
	private static final  String FILTER_BY_BRAND = "select pr.product_id, pr.product_name , pr.price , pr.product_specifications , pr.gender ,  pr.category_id , pr.product_image_type,  pr.stock , pr.product_uuid  from products as pr ,  brand as br  , brand_category_helper as bch  where br.brand_name = '%s' and bch.brand_id = br.brand_id and pr.product_id  = bch.pid";
	
	private static final String ADD_BRAND_NAME = "insert into brand(brand_name) values(?)";
	
	private static  final String UPDATE_BRAND_NAME = "update brand set brand_name = '%s'  where brand_name ='%s' ";
	
	public List< String > get_brands ()
	{
		List<String> total_brands  = this.jdbc_template.query( EXTRACT_BRANDS, new Brand_Data_Mapper());	
		return total_brands;
	}

	public int get_brand_id  ( String brand_name )
	{
		String query =  String.format(EXTRACT_BRAND_ID, brand_name);
		List<Integer> brand_id =  new ArrayList<>() ;
		Predicate<List<?>>predicate =  ( e ) -> e==null || e.size() == 0 ;

		try { 
			brand_id = this.jdbc_template.query(query, new Brand_Id_Mapper());
			
			if ( predicate.test( brand_id ))
				{
					return -1;
				}
			else 
			{
				return brand_id.get( 0 );
			}
		}
		
		catch ( Exception e )
		{
				return -1;
		}
	}
	
	public List<Products_Total_Data> filter_by_brand( String brand_name )
	{
		   	String str = String.format(FILTER_BY_BRAND , brand_name);
		   	
		   	try 
		   	{
		   		List<Products_Total_Data>product_details = this.jdbc_template.query(str, new Products_Data_Mapper());
		   		return product_details;
		   	}	
		   	catch ( Exception e )
		   	{
		   		System.out.println ( e.getLocalizedMessage());
		   		return null;
		   		
		   	}
	}
	
	public Custom_Response add_brand_names ( String brand_name )
	{
			
			Custom_Response cr =  new Custom_Response () ; 
			Object args[]= { brand_name};
			try
			{
				int rows_effected = this.jdbc_template.update(ADD_BRAND_NAME , args );
				if ( rows_effected > 0 )
				{
					cr.setMessage("SUCCESSFULLY INSERTED INTO BRAND");
					cr.setStatus(1);
				}
			}
			catch(DataIntegrityViolationException dae )
			{
				cr.setMessage("BRAND NAME ALREADY EXISTED");
				cr.setStatus(0);
			}
			catch ( Exception e )
			{
				System.out.println(e.getMessage());
				cr.setMessage("INVALID BRAND NAME");
				cr.setStatus(0);
			}
			
			return cr;
	}
	
	public boolean update_brand_name( Brand_Update_Data data )
	{
		String old_brand_name = data.getOld_brand_name();
		
		String new_brand_name = data.getNew_brand_name();
		
		String str = String.format(UPDATE_BRAND_NAME, new_brand_name, old_brand_name);
		
		try
		{
			int rows_effected = this.jdbc_template.update(str );
			if( rows_effected >0)
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		catch ( Exception e )
		{
			return false ;
		}
		
	}
}
