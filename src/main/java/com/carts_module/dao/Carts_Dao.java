package com.carts_module.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carts_module.Carts_Data;
import com.custom_exceptions.Carts_Data_Not_Found;
import com.custom_exceptions.Duplicate_Cart_Item_Exception;
import com.custom_exceptions.Invalid_Cart_Details_Exception;
import com.products_dao.Products_Dao;
import com.security_config.Custom_Response;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Repository
public class Carts_Dao implements CartsDaoInterface {

	@Autowired
	private JdbcTemplate jdbc_template;

	@Autowired
	private Custom_Response  custom_response;

	private final int OK = 1;

	private final int NOT_OK = 0;

	private final String CARTS_INSERTION_QUERY = "insert into carts(product_id , user_id ,  quantity , total , user_id_product_id ) values ( ? , ? , ? , ? , ?)";

	private final String CART_REMOVE_ITEM = "delete from carts where user_id = ? AND product_id = ?";

	private final String CART_REMOVE_ITEM_ALL = "delete from carts where user_id = ? ";

	private final String CARTS_GETTER_QUERY = "select * from carts where user_id = '%d'";

	private final String CARTS_QUANTITY_UPDATER = "update carts set quantity = ?  where product_id = ?  and user_id = ? ";
	
	@Autowired
	private User_Module_Dao_Impl user_module_dao_impl;

	@Autowired 
	private Products_Dao products_dao;
	
	public Custom_Response add_to_cart(Carts_Data carts_data) {

		int product_id = carts_data.getProduct_id();
		int user_id = carts_data.getUser_id();
		int quantity = carts_data.getQuantity();
		float total = carts_data.getTotal();
		String user_id_product_id = carts_data.getUser_id_product_id();
		Object args[] = { product_id, user_id, quantity, total, user_id_product_id };

		try {
			this.jdbc_template.update(CARTS_INSERTION_QUERY, args);
			custom_response.setMessage("SUCCESSFULLY INSERTED INTO THE CARTS");
			custom_response.setStatus(1);
			return custom_response;
		}
		catch (DuplicateKeyException exp) {
			throw new  Duplicate_Cart_Item_Exception("CART_ITEM_ALREADY_EXISTED");	
		}
		catch (DataAccessException dae) {
				throw new Invalid_Cart_Details_Exception("INVALID USER_NAME OR PRODUCT_UUID");
		}
		
	}

	public Custom_Response remove_cart_item(int  user_id, int  product_id) {

		Object args[] = { user_id, product_id };

		try {
			int no_of_rows_effected = this.jdbc_template.update(CART_REMOVE_ITEM, args);

			if (no_of_rows_effected == 1) {
				this.custom_response.setMessage("ITEM REMOVED FROM THE CARTS");
				this.custom_response.setStatus(this.OK);
			} else if (no_of_rows_effected == 0) {
				this.custom_response.setMessage("CARTS ARE EMPTY");
				this.custom_response.setStatus(this.OK);
			}
		}
		catch (Exception e) {
			this.custom_response.setMessage("SOMETHING WENT WRONG WHILE REMOVING THE CARTS_DATA");
			this.custom_response.setStatus(this.NOT_OK);
		}
		return custom_response;
	}

	public boolean remove_all_cart_items(int user_id) {
		System.out.println("INSIDE OF THE REMOVE ALL THE CART ITEMS ");

		try {
			this.jdbc_template.update(CART_REMOVE_ITEM_ALL, user_id);
			return true;
		}

		catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public List<Carts_Data> get_cart_items(String user_name) {
		int user_id = user_module_dao_impl.get_user_id(user_name);
		List<Carts_Data> cart_details = null;
		
		try {
			String query = String.format(CARTS_GETTER_QUERY, user_id);
			cart_details = this.jdbc_template.query(query, new Carts_Details_Row_Mapper());
		
			if ( cart_details.size() == 0 )
			{
				throw  new Carts_Data_Not_Found();
			}
			else 
			{
				return cart_details ;
			}
		}
		catch ( Carts_Data_Not_Found cat )
		{
			throw new Carts_Data_Not_Found( "CART ITEMS ARE NOT AVAILABLE");
		}
		catch (Exception e) {	
			throw new Carts_Data_Not_Found("CARTS DATA NOT FOUND DUE TO THE INVALID USERNAME");
		}

	}

	public Custom_Response update_quantity_val( String user_name ,String product_uuid , int quantity_val )
	{
		Custom_Response  cr = new Custom_Response();		
		int user_id  = this.user_module_dao_impl.get_user_id(user_name);
		int product_id = this.products_dao.get_product_id(product_uuid);
		if ( user_id ==  -1 )
		{
			 return null;
		}
		Object args []  =  {  quantity_val , product_id , user_id };
		try  { 
			
		int res =  this.jdbc_template.update( CARTS_QUANTITY_UPDATER , args );
		
		if ( res  >= 1  )
		{
			cr.setMessage("quantity_value updated successfully ");
			cr.setStatus(1);
		}
		else 
		{
			 cr.setMessage( "SOMETHING WENT WRONG WHILE UPDATING THE QUANTITY");
			 cr.setStatus(0 );
		}
		}
		catch  ( Exception e )
		{
			cr.setMessage( "SOMETHING WENT WRONG WHILE UPDATING THE QUANTITY");
			cr.setStatus(0);
		}
		
		return cr;
	}
}