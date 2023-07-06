package com.products_dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.brand_category_dao.Brand_Category_Dao;
import com.custom_exceptions.products.Invalid_Product_Uuid_Exception;
import com.products_module.Category_Name_Mapper;
import com.products_module.Products_Data;
import com.products_module.Products_Input_Data;
import com.products_module.Products_Side_Image;
import com.products_module.Products_Side_Images_Data;
import com.products_module.Products_Total_Data;
import com.security_config.Custom_Response;

@Repository
@Primary
public class Products_Dao implements ProductsDaoInterface{

	@Autowired
	private JdbcTemplate jdbc_template;

	private final int OK  = 1 ;
	private final int NOT_OK = 0 ;
	
	@Autowired
	private Custom_Response custom_response ;

	@Autowired
	private Brand_Category_Dao  brand_category_dao;

	private final String PRODUCT_INSERTION_QUERY =
	"insert into products"
	+ "( product_name , price ,  product_specifications, "
	+ " category_id ,  product_image , product_image_type , stock , gender)"
	+ "	values  (  ? , ? , ? , ? , ? , ? , ? , ? ) ";

	private static final String PRODUCT_FETCH_BY_UUID = "select * from products where product_uuid = '%s'";
	private static final String PRODUCT_UPDATION_QUERY = "update produts set product_name = '%s' , price = %f  , category_id = %d , stock = %d where product_uuid = '%s' ";
	private static final String PRODUCT_ID_RETRIVAL_QUERY = "select product_id from products where product_uuid = '%s'  OR  product_name = '%s' ";
	private static final String SIDE_IMAGE_INSERTION_QUERY ="insert into product_side_images(product_id , product_side_image  , image_type) values(?,?,?)";
	private static final String PRODUCT_ID_RETRIVAL_QUERY_2 = "select product_id from products where product_name = '%s' AND product_image_type = '%s' ";
	private static final String PRODUCT_SIDE_IMAGES_RETRIVAL_QUERY= "select product_uuid  from product_side_images where product_id = %d ";
	private static final String PRODUCT_ID_RETRIVAL_QUERY_3 = "select product_id from products where product_uuid = '%s'";
	private static final String CATEGORY_BASED_PRODUCTS_QUERY= "select * from  products where category_id = %d " ;
	private static final String RETRIVE_CATEGORY_NAME_QUERY = "select category_name from categories where category_id = %d ";
	private static final String RETRIVE_SINGLE_PRODUCT_QUERY = "select * from products where product_id = %d";

	private int product_id_util ( List<Integer > product_id)
	{
		if ( product_id  ==  null || product_id.size() == 0 )
		{
			System.out.println ( "product id not found ");
			return -1;
		}
		else
		{	return product_id.get(0);
		}
	}

	public int __get_product_id ( String product_name , String image_type )
	{
		String query = String.format(PRODUCT_ID_RETRIVAL_QUERY_2 , product_name,  image_type);
		List<Integer>product_id_list =   this.jdbc_template.query ( query, new Primary_Key_Mapper());

		return product_id_util (  product_id_list);
	}

	public int get_product_id ( String product_uuid , String product_name )
	{
		String query   =  String.format( PRODUCT_ID_RETRIVAL_QUERY,  product_uuid , product_name);
		List<Integer> product_id_list = this.jdbc_template.query( query, new Primary_Key_Mapper() );

		return product_id_util (  product_id_list );
	}

	public int  get_product_id ( String product_uuid )
	{
		String query   =  String.format( PRODUCT_ID_RETRIVAL_QUERY_3, product_uuid);

		List<Integer> product_id_list = new ArrayList<>();
		try {
			product_id_list = this.jdbc_template.query( query, new Primary_Key_Mapper() );
			
			if ( product_id_list.size () ==1 )
			{
				return product_id_list.get( 0 );
			}
			else  
			{
				 throw new Invalid_Product_Uuid_Exception();
			}
		}
		
		catch ( Invalid_Product_Uuid_Exception inv )
		{
				throw new Invalid_Product_Uuid_Exception( "INVALID PRODUCT UUID VALUE");
		}
		catch ( DataAccessException  exp)
		{
				System.out.println ( " SOMETHING WENT WRONG WHILE FETCHING THE  PROUDCT INFO ");
		}

		catch ( Exception  e ) 
		{
			System.out.println ( "ERROR ENCOUNTED WHILE FETCHING THE RECORD");
		}
		return -1;
	}

	public String get_category_name (  int category_id )
	{

		String query = String.format( RETRIVE_CATEGORY_NAME_QUERY ,  category_id);
		 String category_name =  this.jdbc_template.queryForObject(query ,  new Category_Name_Mapper());
		 if ( category_name == null )
		 {
			 System.out.println ( "INVALID CATEGORY_NAME ");
		 }

		return  category_name;
	}

	public Custom_Response set_response ( String message , int status_code )
	{
		this.custom_response.setMessage(message );
		this.custom_response.setStatus(status_code);
		return  this.custom_response;
	}


	@Transactional
	public int[]   insert_product_side_imgs( int product_id   , List<Products_Side_Images_Data>side_images_data  )throws Exception
	{

		List<Object[]>args_list = new ArrayList<>();

		for ( Products_Side_Images_Data side_image : side_images_data)
		{
			Object args[]   = { product_id , side_image.getSide_images() , side_image.getImage_type() } ;
			args_list.add( args );
		}
		String message = "INSERTED SUCCESSFULLY";

		int no_of_rows_effected[] = this.jdbc_template.batchUpdate(SIDE_IMAGE_INSERTION_QUERY , args_list);
		return no_of_rows_effected;

	}
	
	@Transactional
	public int delete_side_images( List<String>img_uuids)throws Exception
	{
		String main_query = "delete from product_side_images where ";
		
		if ( img_uuids == null || img_uuids.size() == 0)
				return 0;
		String str = "";
		int n = img_uuids.size() ; 
		
		String sub_part =  " product_uuid = '%s' or ";
		
		String end_part = " product_uuid = '%s' ;";
	
		for(  int idx = 0 ;  idx < n-1 ; idx ++ )
		{
			 str  += String.format(sub_part, img_uuids.get(idx));		 
		}
		
		str += String.format(end_part, img_uuids.get(n-1));
		main_query += str; 
		
		System.out.println(main_query);
		
		int rows_affected =  jdbc_template.update( main_query);
		return rows_affected;
	}

	public int  get_category_id (  String category_name )
	{
		category_name = category_name.toLowerCase();
		String query =  String.format(" select  category_id from categories where category_name =  '%s' " ,  category_name);

		try {
		Integer category_id = this.jdbc_template.queryForObject( query , new Catergory_Id_Mapper());
		return category_id ;
		}
		catch( Exception e )
		{
			return -1;
		}
	}
	
	@Transactional
	public  Custom_Response add_products(  Products_Input_Data products_data , Float price , int stock , int category_id  , int brand_id) throws Exception {

		Object args[] = { new String(products_data.getProduct_name().getBytes()) ,
						  price,
						  new String(products_data.getProduct_specifications().getBytes()) ,
						  category_id,
						  products_data.getMain_image().getBytes(),
						  products_data.getMain_image().getContentType(),
						  stock,
						  products_data.getGender()
						};
		
		this.jdbc_template.update( PRODUCT_INSERTION_QUERY ,  args);
		
		int product_id =  __get_product_id(new String(products_data.getProduct_name().getBytes()) ,  products_data.getMain_image().getContentType());	
		
		List<MultipartFile>side_imgs = products_data.getSide_images();
		 
		if( side_imgs != null && side_imgs.size() >= 0 )
		{	
			List<Products_Side_Images_Data> prd_side_img_data =  new ArrayList<>();
			
				
				for( MultipartFile curr_img : side_imgs )
				{
				Products_Side_Images_Data pd = new Products_Side_Images_Data();
				pd.setImage_type( curr_img.getContentType());
				
				pd.setSide_images( curr_img.getBytes());
				
				prd_side_img_data.add(pd);
				}
			
			
			this.insert_product_side_imgs(product_id, prd_side_img_data);		
		}
		
		brand_category_dao.insert_category_brand(brand_id , category_id , product_id);


		 String message = "PRODUCT_INSERTED_SUCCESSFULLY";

		 this.custom_response =  this.set_response(message, OK);
		 

		return this.custom_response;
	}

	public Custom_Response  add_products ( Products_Data  products_data  , List<Products_Side_Images_Data > product_side_images )
	{

//		Custom_Response pr = add_products( products_data );

		String message = null ;

		if ( product_side_images != null  && product_side_images.size()>=1) {

			int product_id =  get_product_id(products_data.getProduct_name() ,  products_data.getImage_type());
			try {
				insert_product_side_imgs(product_id , product_side_images);
				message = "PRODUCT_INSERTED_SUCCESSFULLY";
				this.custom_response =  set_response(message  , OK);
			}
			catch ( Exception  exp )
			{
				message = "INVALID PRODUCT_SIDE_IMAGES ";
				this.custom_response =  set_response ( message ,  NOT_OK);
			}
	}
		else
		{
			this.custom_response.setMessage("SIDE_IMAGES SHOULD NOT BE EMPTY");
			this.custom_response.setMessage(message);
		}

		return custom_response;
	}

	public List< Products_Total_Data>  get_product_deatails ( )
	{
		String query =  " select * from products ";
		List<Products_Total_Data> products_list = this.jdbc_template.query ( query ,  new Products_Data_Mapper( ) );
		return products_list;

	}

	
	public List<Products_Total_Data>get_category_based_products ( String category_name )
	{
		List<Products_Total_Data> products_list =  null;

		Integer category_id = this.get_category_id(category_name);

		try

		{
			String query = String.format(CATEGORY_BASED_PRODUCTS_QUERY , category_id );
			products_list  = this.jdbc_template.query(query ,  new Products_Data_Mapper());
		}
		catch ( Exception e )

		{
			products_list = null;
		}

		return products_list;
	}

	
	
	public List<Products_Total_Data> get_products( List<String>uuids)
	{
			
		if (  uuids == null || uuids.size() == 0 )
		{
			return  null;
		}
		int n  = uuids.size () ; 
		String temp = PRODUCT_FETCH_BY_UUID; 
		String total_query = new String () ;
		String helper_query =  " or product_uuid = '%s' ";
		for (  int idx = 0 ; idx < n ; idx++) {
			 
			String val =  uuids.get(idx).trim();
			
			if ( idx ==0 )
			{
				temp =  String.format(PRODUCT_FETCH_BY_UUID, val );
				
			}
			else 
			{
			temp = String.format( helper_query , val );
			}
			total_query += temp ;
		}
		
		String last_val = uuids.get( n-1 ).trim();
		
		if (  n== 1 )
		{
			total_query  = String.format( PRODUCT_FETCH_BY_UUID ,last_val );
		}
		
		System.out.println( total_query);
		
		List<Products_Total_Data>products_data = null ;
		try {
		products_data= jdbc_template.query( total_query ,  new Products_Data_Mapper());
		}
		catch( Exception  e )
		{
			products_data  = null;
		}
		return products_data;
	}

	public Products_Total_Data get_product ( int product_id )
	{
		Products_Total_Data product_total_data = null;
		try
		{
			String product_query  = String.format(RETRIVE_SINGLE_PRODUCT_QUERY  , product_id );

			List< Products_Total_Data > product_details = this.jdbc_template.query ( product_query , new Products_Data_Mapper()) ;

			if ( product_details != null )
			{
				product_total_data =  product_details.get(  0 ) ;
			}
		}
		catch  ( Exception e )
		{
			System.out.println( e.getMessage());
		}
		return  product_total_data;
	}

	//returns the product side images
	public List<Products_Side_Image > get_product_side_images(  int product_id )
	{

		String query =  String.format(PRODUCT_SIDE_IMAGES_RETRIVAL_QUERY , product_id) ;
		try {
		List<Products_Side_Image>side_image = this.jdbc_template.query(  query,  new Products_Side_Images_Mapper());

		return side_image;
		}
		catch ( Exception e )
		{
			System.out.println(e.getMessage() ) ;
		}
		return null;
	}

	//return the image in the products_table
	public byte [] get_product_image  ( String uuid)
	{
		String  query = new String () ;
		query = String.format(" select  product_image from products where product_uuid = '%s'  " , uuid) ;
		
		byte[]  image_bytes = null;
		try {
			image_bytes = this.jdbc_template.queryForObject( query , new Product_Image_Bytes_Mapper() );
		
			if ( image_bytes == null )
			{
				throw new Invalid_Product_Uuid_Exception("INVALID DETAILS");
			}
			else
				return image_bytes;
		}

		catch ( Exception exp )
		{
			throw new Invalid_Product_Uuid_Exception("INVALID DETAILS");
		}
	}
	
	//returns the image in the product_side_images table
	public byte[] get_side_image( String uuid)
	{
		String query = new String () ;
		query = String.format(" select product_side_image from product_side_images where product_uuid = '%s' " ,  uuid);
		byte[] image_bytes   = null;
		try {
			image_bytes =  this.jdbc_template.queryForObject(query , new Product_Side_Image_Bytes_Mapper() );
			return image_bytes;
		}
		catch ( DataAccessException dao)
		{
			throw new Invalid_Product_Uuid_Exception("INVALID DETAILS");
		}
	}
	@Override
	public Custom_Response add_products(Products_Input_Data input_data, float price, int stock, int category_id,
			int brand_id) {
		
		return null;
	}
}
