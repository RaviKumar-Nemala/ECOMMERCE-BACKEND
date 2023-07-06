package com.user_login_module.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt_module.Jwt_Data_Return;
import com.security_config.Custom_Response;
import com.user_login_module.Otp_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Info;

import com.user_login_module.User_Otp_Data;
import com.user_login_module.User_Password_Change_Info;
import com.user_login_module.dao.User_Module_Dao_Impl;
import com.user_login_module.service.My_User_Details_Service;
import com.user_login_module.service.PasswordChangeServiceInterface;
import com.user_login_module.service.UserCreationServiceInterface;
import com.user_login_module.service.UserValidationServiceInterface;

@RestController
@CrossOrigin
public class UserAuthenticationController {

	@Autowired
	private User_Module_Dao_Impl user_dao_impl;

	@Autowired
	private  UserCreationServiceInterface userCreationServiceInterface;

	@Autowired
	private UserValidationServiceInterface  userValidationServiceInterface;

	@Autowired
	private Otp_Response  otp_response ;

	@Autowired
	@Qualifier("user_info")
	private UserDetails user_details;

	@Autowired
	@Qualifier("password_change_service")
	private PasswordChangeServiceInterface  password_change_service;

	@Autowired
	@Qualifier("my_user_details_service")
	private My_User_Details_Service  my_user_details_service;

	@Autowired
	@Qualifier("bcrypt")
	private PasswordEncoder password_encoder;

	public UserAuthenticationController()
	{
		System.out.println ( "userlogin controller object created");
	}

	@PostMapping("/insert_user")
	public String insert_user(  @RequestBody User_Info  user_info )
	{
		Custom_Response custom_response   = new Custom_Response();

		String message  = "";

		String password = user_info.getPassword();

		String encoded_passsword = this.password_encoder.encode(password);

		user_info.setPassword(encoded_passsword);

		try {

			custom_response =  user_dao_impl.insert_user_record(user_info);
		}

		catch ( Exception e )
		{
			e.printStackTrace();
		}

		if( custom_response.isOk() )
			message =  custom_response.getMessage();
		else
		{
			message = custom_response.getMessage();
		}

		return message ;
	}

	@PostMapping("/change_password")
	public String change_password(@RequestBody User_Password_Change_Info user_password_change_info )
	{
		System.out.println("inside of the change_password");

		boolean result ;
		String message = new String () ;

		String user_name = user_password_change_info.getUser_name() ;

		String old_password = user_password_change_info.getOld_password() ;

		String new_password = user_password_change_info.getNew_password();

		System.out.println ( user_name  +  old_password + new_password);

		result = this.password_change_service.check_password(user_name, old_password);

		if ( result )
		{
			boolean update_result = this.password_change_service.update_password( user_name ,  new_password);

			if ( update_result )
			{
				message = "PASSWORD UPDATED SUCCESSFULLY";
			}

		}
		else
		{
			message = "PASSWORD UPDATATION FAILURE";
		}

		return message;

	}

	@GetMapping("/get_users")
	public List< User_Info > get_users( )
	{
		
		 List < User_Info> user_list  = this.my_user_details_service.get_all_users();

		 if ( user_list == null )
		 {
			 System.out.println("users list is empty ");
		 }
		 else
		 {
			 System.out.println ( "NO OF RECORDS = " + user_list.size() ) ;
		 }

		 return user_list;
	}
	
	@PostMapping( "/check_otp_expiration")
	public ResponseEntity<?>check_otp_expiration( @RequestBody Map<String,String> user_details )
	{
		if( user_details == null || !user_details.containsKey("email") )
			return ResponseEntity.badRequest().body("INVALID USERNAME");
		
		Custom_Response cr = this.userValidationServiceInterface.check_for_otp_expiration( user_details.get("email") );	
		
		String msg  = cr.getMessage(); 
		
		System.out.println( msg ) ;
		
		if ( cr.isOk() )
		{
			return ResponseEntity.ok( msg );
		}
		else 
		{
			return ResponseEntity.badRequest().body( msg );
		}
	}
	
	// we are only accepting the  username(email id ) from the client
	// we store this email id along with the boolean value in the map
	// ans sends the otp to this email
	// after succesfully sending the email 
	// we return 200 response to the client
	@PostMapping("/validate_user_info")
	public ResponseEntity<Otp_Response> create_user (   @RequestBody Map<String,String>user_details)
	{
		boolean usr_creation_result =  this.userCreationServiceInterface.create_user(user_details);

		if ( usr_creation_result)
		{
			this.otp_response.setResponse_message("VALID EMAIL ACCOUNT");

			return ResponseEntity.ok(otp_response);
		}
		else
		{
			this.otp_response.setResponse_message("ACCOUNT EXISTED WITH THE PROVIDED EMAIL INFO");

			return ResponseEntity.badRequest().body( otp_response);
		}
	}

	/**
	 *
	 *	This method is responsible for the  validation of the otp if
	 *
	 *	it is the valid otp then we store that userinfo in the database directly
	 *
	 *	after successful insertion of the user record in  the database then we generate the jwt token and
	 *
	 *	we send that token to the front end
	 *
	 *	If the otp validation has failed then we simply return invalid otp status to the front end
	 */
	private ResponseEntity<?> send_jwt ( String username )
	{
		Jwt_Data_Return jwt_data = this.userCreationServiceInterface.generate_jwt(username);

		if ( jwt_data!= null)
		{
			return ResponseEntity.ok().body(jwt_data);
		}
		else
		{
			return ResponseEntity.badRequest().body("VALID OTP BUT ERROR ENCOUNTERED  WHILE INSERTING THE USER RECORD");
		}
	}
	
	/**
	 *
	 * IF THE VALIDATION IS SUCCESS THEN STORE THE USERDETAILS IN THE DATABASE
	 * AFTER THAT REMOVE THE USERINFO FROM  THE USERDETALS MAP ALSO FROM THE OTP MAP
	 */
	@PostMapping(value = "/validate_otp")
	public ResponseEntity<?>validate_user_otp ( @RequestBody User_Otp_Data user_info)throws Exception
	{

		Otp_Validation_Resend_Data otp_resend_data = this.userValidationServiceInterface.validate_otp(user_info);

		System.out.println( "reached");
		
		String username  = user_info.getUsername();
		
		if ( otp_resend_data.isOk())
		{

			this.userCreationServiceInterface.store_usr_in_db( user_info );
			
			//remove the user details from the cache(map)
			this.userCreationServiceInterface.remove_userdetails(username);

			return send_jwt(username);
			
		}
		else
		{
			if( otp_resend_data.getRemaining_attempts() <= 0 )
			{
				this.userCreationServiceInterface.remove_userdetails(username);
			}
			return ResponseEntity.badRequest().body( otp_resend_data);
		}
	}

	@PutMapping(  value = "/resend_otp/{username}")
	public ResponseEntity<?>resend_otp ( @PathVariable String username )
	{

		Otp_Validation_Resend_Data otp_resend_data = this.userCreationServiceInterface.resend_otp(username);

		if ( otp_resend_data.isOk())
		{
			return ResponseEntity.ok( otp_resend_data);
		}
		else
		{
			return ResponseEntity.badRequest().body( otp_resend_data);
		}

	}

	@GetMapping("/forget_password")
	public ResponseEntity<?>forget_password ( @RequestParam String username)
	{

		if ( !this.password_change_service.forget_password_handler(username) )
		{
			return  ResponseEntity.badRequest().body("INVALID USERNAME OR ERROR ENCOUNTERED WHILE SENDING EMAIL");
		}
		else
		{

			return ResponseEntity.ok( "OTP SENT TO THE EMAIL");
		}
	}

	@PostMapping ( "/forget_password/validate_otp")
	public ResponseEntity<?>forget_password_otp_validator ( @RequestBody User_Otp_Data  otp_info)
	{

		Otp_Validation_Resend_Data otp_resend_data = this.password_change_service.validate_otp(otp_info);

		String username =   otp_info.getUsername();

		if ( otp_resend_data.isOk() )
		{

			this.password_change_service.store_password_update_usr_details(username);

			return ResponseEntity.ok( "USER ENTERED CORRECT OTP VALUE");

		}
		else
		{

			return ResponseEntity.badRequest().body( otp_resend_data);
		}
	}

	@PutMapping ("/forget_password/resend_otp/{username}")
	public ResponseEntity<?>forget_passwd_resend_otp( @PathVariable String username )
	{
		Otp_Validation_Resend_Data otp_resend_data =  this.password_change_service.resend_otp(username);

		if ( otp_resend_data.isOk())
		{
			return  ResponseEntity.ok(otp_resend_data);
		}
		else
		{
			return ResponseEntity.badRequest().body ( otp_resend_data);
		}
	}

	@PostMapping("/forget_password/update_password")
	public ResponseEntity<?>update_forget_password(  @RequestBody User_Password_Change_Info password_change_info)
	{

		String username = password_change_info.getUser_name();

		String password = password_change_info.getNew_password();

		ResponseEntity<?>response = null;

		/**
		 * before changing the password of the user we need to make sure
		 *  that provided username must be matched with the forget_password requested users map
		 *
		 *  ON THE SUCCESSFUL UPDATION OF PASSWORD REMOVE THE USERNAME FROM THE PASSWORD_USERDETAILS_MAP ALSO FROM  THE OTP DETAILS MAP
		 *
		 */
		//	the below if clause returns true if the current user requested for forget password mechanism
		if  ( this.password_change_service.is_valid_user(username))
		{

			this.password_change_service.remove_forget_passwd_user(username);

			boolean result = this.password_change_service.update_password (username , password );

			if ( result )
			{
				response =   this.send_jwt ( username );
			}
			else
			{
				response = ResponseEntity.badRequest().body("SOMETHIMG WRONG WHILE UPDATING PASSWORD");
			}
		}
		else
		{
			response =   ResponseEntity.badRequest().body( "SOMETHING WENT WRONG WHILE UPDATING THE PASSWORD\n PLEASE TRY AGAIN") ;
		}

		return   response;
	}

}

