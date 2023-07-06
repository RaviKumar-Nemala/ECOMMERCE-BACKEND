package com.user_login_module.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.custom_exceptions.Invalid_User_Details_Exception;
import com.email_sending.service.Email_Sending_Service;
import com.jwt_module.Jwt_Data_Return;
import com.jwt_service.Jwt_Service;
import com.security_config.Custom_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Info;
import com.user_login_module.dao.UserAuthenticationDaoInterface;


@Service
public class User_Creation_Service extends User_Otp_Service implements UserCreationServiceInterface  {

	@Autowired
	private UserAuthenticationDaoInterface userAuthenticationDaoInterface;

	@Autowired
	private User_Validation_Service  user_validation_service;

	@Autowired
	private Email_Sending_Service email_service;

	@Autowired
	private Jwt_Service jwt_service;

	@Autowired
	Otp_Validation_Resend_Data otp_validation_resend_data;

	@Autowired
	@Qualifier("bcrypt")
	private PasswordEncoder password_encoder;

	private  Map< String , Boolean>user_details_map= new HashMap<>();

	//used to store the userinfo in the map for further validation
	private void store_user_details ( String user_name)
	{
		this.user_details_map.put(user_name, true);	
	}
	
	@Override
	public boolean remove_userdetails(  String username )
	{
		super.remove_userdetails(username);
		this.user_details_map.remove( username );
		return true ;
	}

	private boolean check_for_password(  String password )
	{
		if( password == null )	return false ;
		
		String password_regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{6,20})";
		
		Pattern password_pattern = Pattern.compile(password_regex);
		
		Matcher matcher = password_pattern.matcher(password_regex);
		
		if( matcher.find())
		{
			return true;
		}
		else {
			System.out.println("invalid password sequence");
			return false;
		}	
	}
	
	//username is considered as the email
	private boolean check_for_username( String username )
	{	
		if( username == null )return  false ;
		String username_regex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		Pattern username_pattern  = Pattern.compile( username_regex);
		return username_pattern.matcher( username).find();
	}
	
	private boolean  check_for_nickname( String nickname)
	{
		 if( nickname == null )	return false ;
		 
		 String nickname_regex ="/[!@#\\$%\\^&\\*\\(\\)]/";
		 
		 //nickname sholdnot contain any special characters
		 //and contains at least size of 3
		 if ( nickname.length() >= 3 && !Pattern.compile(nickname_regex).matcher(nickname).find())
			 	return true;
		 else
			 	return false;
		
	}

	//if any malicious user  send the wrong user_info then we need to validate that userinfo before we are going to 
	//store in on the database 
	private boolean check_user_credentials( User_Info user_info )
	{
		String username = user_info.getUsername();		
		String password = user_info.getPassword();
		String nickname = user_info.getNick_name();

		if( !check_for_username(username) || !check_for_password(password) || !check_for_nickname(nickname))
		{
				return false; 
		}		
		return true;
	}
	
	public Custom_Response store_usr_in_db ( User_Info user_info ) throws Invalid_User_Details_Exception
	{

		// check if the userdetails are valid or not
		if( !check_user_credentials( user_info))
		{
			throw  new Invalid_User_Details_Exception("USER_DETAILS ARE NOT CORRECT");
		}
		
		String password = user_info.getPassword();

		String encoded_password = this.password_encoder.encode(password);

		user_info.setPassword(encoded_password);

		return this.userAuthenticationDaoInterface.insert_user_record(user_info);

	}
	
	
	//this user_details map should contains the email
	private boolean validate_user(  Map< String , String > user_details )
	{
			if( user_details == null || !user_details.containsKey("email"))
					return false;
			else 	
				return true;
	}
	
	public boolean create_user( Map<String, String>user_details)
	{
		if ( !validate_user(  user_details ))
			return false;
		
		String username = user_details.get("email");

		boolean result =  this.user_validation_service.is_account_existed(username);

		if ( result )
		{
			return false ;
		}

		this.store_user_details(username );
		
		int generated_otp_val  = get_random_number();

		super.store_otp_details(username,generated_otp_val);
		System.out.println( generated_otp_val);
		
		super.store_otp_resend_attemps( username );

		boolean email_send_res = this.email_service.send_account_creation_email(username ,generated_otp_val);

		if ( !email_send_res )
		{
			System.out.println ( "SOMETHING WENT WRONG DURING TRANSMISSION OF THE EMAIL");

			return false ;
		}
		else {

		return true ;
	}
	}


	// here we assume that the user details already stored in the user_details_map;
	//then we generate the new otp and update the otp map values for the given username
	public Otp_Validation_Resend_Data resend_otp ( String username )
	{

		// if the no.of otp limits are reached then we simply reject and does not send the otp again
		if ( !super.check_otp_resend_attempts(username))
		{

			this.otp_validation_resend_data.setStatus(0);

			this.otp_validation_resend_data.setMessage("MAXIMUM NO.OF ATTEMPTS ARE REACHED");

			this.otp_validation_resend_data.setRemaining_attempts(0);
			
			

		}

			//here after sending the email we simply increment the no_of_otp_sending requests to one
		else if ( this.user_details_map.containsKey(username))
		   {

			   int newly_generated_otp =  this.get_random_number();

			   this.store_otp_details(username, newly_generated_otp);

			   boolean email_sending_res =  this.email_service.send_account_creation_email(username ,newly_generated_otp);

			   super.incr_otp_resend_attempts(username);

			   int remaining_attempts = this.get_remaining_request_attempts(username);

			   this.otp_validation_resend_data.setStatus(1);

			   this.otp_validation_resend_data.setMessage("REMAING_ATTEMPTS ARE "+remaining_attempts);

			   this.otp_validation_resend_data.setRemaining_attempts( remaining_attempts);

		   }
		  else 
		  	{	
			  	this.otp_validation_resend_data.setMessage("SOMETHING WENT WRONG PLEASE TRY AFTER SOME TIME");
			  	this.otp_validation_resend_data.setStatus(0);
			  	this.otp_validation_resend_data.setRemaining_attempts(0);
		  	}
		return this.otp_validation_resend_data;
		
	}

		public Jwt_Data_Return generate_jwt ( String username )
		{

			UserDetails user_details = this.userAuthenticationDaoInterface.find_by_user_name(username);

			Jwt_Data_Return jwt_data  = null;

			if ( user_details != null )
			{

				jwt_data = this.jwt_service.generate_jwt(user_details);

			}

			return jwt_data;
		}
}