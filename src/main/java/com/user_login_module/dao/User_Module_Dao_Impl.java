package com.user_login_module.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.security_config.Custom_Response;
import com.user_login_module.User_Info;


@Component("dao_impl")
public class User_Module_Dao_Impl implements UserAuthenticationDaoInterface{
	
	@Autowired
	JdbcTemplate jdbc_template;

	private final String USER_ID_RETRIVAL_QUERY = "select user_id from users where username = '%s' ";

	public User_Module_Dao_Impl() {
		System.out.println("inside of the user_module_dao_impl");
	}

	int update_query_result_status;

	int usrs_tabl_update_status;

	int auth_tabl_update_status;

	String sql1;

	String sql2;

	final String USER_ROLE = "ROLE_USER";


	@Autowired
	private Custom_Response custom_response;



	public boolean update_password(String user_name, String encoded_password) {

		this.sql1 = "update users set password = ? where username = ? ";

		Object args1[] = { encoded_password, user_name };

		int result = -1;

		System.out.println("INSIDE OF THE DAO_IMPL \n UPDATE PASSWORD");

		try {
			result = this.jdbc_template.update(sql1, args1);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (result > 0) {

			System.out.println(" PASSWORD UPDATED SUCCESSFULLY");

			return true;

		} else {
			System.out.println("FAILURE WHILE UPDATING THE PASSWORD");
			return false;
		}

	}

	// helpful for the changing the password and the nick name of the user
	public boolean update_usr_details(User_Info usr_info) {

		this.sql1 = "";
		this.sql2 = "";

		sql1 = " update users set  nickname = ?  where username = ?";
		Object args1[] = { usr_info.getNick_name(), usr_info.getUsername() };

		int result = -1;

		try {
			result = this.jdbc_template.update(sql1, args1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (result > 0) {
			System.out.println("record updated successfully ");
			return true;
		} else {
			System.out.println(" updation failure");
			return false;
		}

	}

	public User_Info find_by_user_name(String user_name) {
		this.sql1 = "";

		sql1 = String.format(
				"select usr.username , usr.password ,  usr.account_active , usr.nickname , auth.authority from users as usr  inner join authorities as auth on  usr.username = '%s'  AND  auth.username = '%s'",
				user_name, user_name);

		try {

			List<User_Info> usr_info = this.jdbc_template.query(sql1, new User_Module_User_Details_Mapper());

			//System.out.println(usr_info.get(0).getPassword());

			if (usr_info.size() >= 1) {
				return usr_info.get(0);
			} else {
				System.out.println("INSIDE OF THE DAO IMPL  RECORD NOT FOUND WITH THE GIVEN CREDENTIALS");

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	//this method is useful for the inserting the user records
	// not for the admin records
	// HARD CODING THE ROLE ATTRIBUTE AS THE ROLE_USER
	public Custom_Response insert_user_record(User_Info user_info) {

		System.out.println("inside of the insert user function ");

		this.sql1 = " ";

		sql1 = "insert into users(username , password , account_active , nickname)  values( ? , ? , ? , ?)";

		this.sql2 = "";

		sql2 = " insert into authorities ( username , authority ) values  (  ?  , ? )";

		Object args1[] = { user_info.getUsername(), user_info.getPassword(), 1, user_info.getNick_name() };

		Object args2[] = { user_info.getUsername(), USER_ROLE };

		try {

			this.usrs_tabl_update_status = this.jdbc_template.update(sql1, args1);


			this.auth_tabl_update_status = this.jdbc_template.update(sql2, args2);

			if (usrs_tabl_update_status > 0 && auth_tabl_update_status > 0)
			{
				this.custom_response.setStatus(1);
				this.custom_response.setMessage("USER RECORD INSERTED SUCCESSFULLY");
			}

		} catch (org.springframework.dao.DuplicateKeyException dup) {
				this.custom_response.setMessage("USER ALREADY EXISTED WITH THE CURRENT CREDENTIALS");
				this.custom_response.setStatus(0);
		} catch (Exception e) {
				this.custom_response.setMessage("ERROR ENCOUTERED WHILE INSERTING THE USER RECORD \n PLEASE CHECK THE SYNTAX");
				this.custom_response.setStatus(0);
		}

			return this.custom_response;
	}



	// returns all the users including the admin accounts
	// returns the records from both users and the authorities table
	public List<User_Info> get_user_details() {

		System.out.println("inside of the get_user_Details ");

		this.sql1 = "select  usr.username , usr.password  , usr.nickname , usr.account_active ,  auth.authority from users as usr inner join authorities as auth  on  usr.username =  auth.username ";

		List<User_Info> usr_details = null;

		try {

			usr_details = this.jdbc_template.query(sql1, new User_Module_User_Details_Mapper());
			if (usr_details != null)
				System.out.println("USER DETAILS  RECORDS SIZE =  " + usr_details.size());

		}

		catch (Exception e) {
			System.out.println(e.getMessage());

		}

		return usr_details;
	}


	public int get_user_id ( String user_name )
	{

		List<Integer>user_id = null;

		String query =  String.format(USER_ID_RETRIVAL_QUERY , user_name);
		try {

		user_id = this.jdbc_template.query( query, new Userid_Mapper());
		}
		catch ( BadSqlGrammarException exception )
		{

			System.out.println (  exception );
		}

		catch ( DataAccessException exception )
		{
			System.out.println ( exception );
		}

		if ( user_id == null  || user_id.size() == 0 )
		{
			System.out.println ("user_id not foun ");
			return -1;
		}
		else
		{
			return user_id.get(0).intValue();
		}

	}

}
