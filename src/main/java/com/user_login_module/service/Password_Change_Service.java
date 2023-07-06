package com.user_login_module.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.email_sending.service.Email_Sending_Service;
import com.security_config.Custom_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Otp_Data;
import com.user_login_module.User_Password_Change_Info;
import com.user_login_module.dao.User_Module_Dao_Impl;

@Service("password_change_service")
public class Password_Change_Service   extends User_Otp_Service implements  PasswordChangeServiceInterface{

	@Autowired
	User_Module_Dao_Impl dao_impl;

	@Autowired
	My_User_Details_Service  user_details_service;

	@Autowired
	@Qualifier("bcrypt")
	PasswordEncoder  password_encoder;

	@Autowired
	private Email_Sending_Service email_service;

	@Autowired
	private User_Validation_Service  user_validation_service;

	private Map< String , Boolean > password_change_user_details   = new HashMap<>();

	private static String FORGET_PASSWORD_OTP_MSG   =  "<span style  = "+ " \"font-size : 20px ; color :black ; font-weight :600; font-family :sans-serif ; padding : 5px\" >" +"%d"+ "</span>" + "<span>" + "  is your otp for creating new password" + "</span>";

 	public boolean update_password ( String user_name , String new_password)
	{
		String encoded_password = password_encoder.encode( new_password);

		return dao_impl.update_password(user_name, encoded_password);

	}

 	public boolean remove_forget_passwd_user (  String username)
 	{
 		super.remove_userdetails(username);

 		return this.password_change_user_details.remove(username);
 	}

	public boolean is_match ( String extracted_password , String entered_password)
	{
		 if ( password_encoder.matches(entered_password , extracted_password))
		 {
			 System.out.println("match found");
			 return true;
		 }
		 else
		 {
			 System.out.println("match not found");
			 return false;
		 }
	}

	public boolean check_passwod_util ( UserDetails userDetails , String entered_password)
	{
		String extracted_password = userDetails.getPassword();
		System.out.println("extracted password" + extracted_password);
		if ( is_match(extracted_password, entered_password) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean check_password ( String user_name , String entered_passwod   )
	{
		UserDetails user_details = user_details_service.loadUserByUsername( user_name) ;

		System.out.println ( user_details.getUsername()  +" " +user_details.getPassword() );

		boolean result   = false ;

		if ( user_details != null )
		{
			result =  check_passwod_util(user_details, entered_passwod);

		}

		return result;

	}


	public void store_password_update_usr_details ( String username )
	{
			this.password_change_user_details.put( username , true);
			return;
	}
	public boolean  is_valid_user ( String username )
	{

		return this.password_change_user_details.containsKey(username);
	}

	
	public Otp_Validation_Resend_Data validate_otp ( User_Otp_Data user_info )
	{
		
		Otp_Validation_Resend_Data response  = new Otp_Validation_Resend_Data();
		
		String username = user_info.getUsername();
		
		boolean result = super.is_valid_otp(username, user_info.getOtp());
		
		if (  result  )
		{
			this.store_password_update_usr_details(username);
			response.setMessage("VALID OTP");
			response.setStatus(1);
			response.setRemaining_attempts(1);
		}
		else 
		{
			super.incr_no_of_attemps(username);
			
			int remaining_attempts = super.get_remaining_validation_attempts(username);
			
			response.setRemaining_attempts(remaining_attempts);
			
			if (  remaining_attempts <= 0 )
			{
				super.remove_userdetails(username);
				
				response.setMessage("OTP VALIDATION LIMIT HAS REACHED");	
			}
		}
		return response ;
	}
	
	public  boolean forget_password_handler ( String username )
	{

		if (!user_validation_service.is_account_existed(username))
		{
			return false ;
		}

		int generated_otp_value = super.get_random_number();

		System.out.println ( generated_otp_value );

		String message = String.format(FORGET_PASSWORD_OTP_MSG,generated_otp_value);

		super.store_otp_details(username, generated_otp_value);

		super.store_otp_resend_attemps(username);
		
		return this.email_service.send_account_creation_email(username, generated_otp_value);

	}

	public Otp_Validation_Resend_Data resend_otp (  String username )
	{

		Otp_Validation_Resend_Data response = new Otp_Validation_Resend_Data();

		if ( !super.check_otp_resend_attempts(username) )
		{
			response.setMessage("OTP RESEND ATTEMPS ARE REACHED");
			response.setStatus(0);
			response.setRemaining_attempts(0);
		}
		else if  ( password_change_user_details.containsKey(username))
		{

			int otp_value = super.get_random_number();

			boolean email_status  = this.email_service.send_forget_password_email( username , otp_value);

			super.incr_otp_resend_attempts(username);

			super.store_otp_details(username, otp_value);

			int remaining_attempts = super.get_remaining_request_attempts(username);

			if ( email_status )
			{
				response.setMessage("OTP SENT TO THE EMAIL");

				response.setStatus( 1 );

				response.setRemaining_attempts(remaining_attempts);
			}
			else
			{
				response.setMessage("ERROR ENCOUNTERED WHILE SENDING THE EMAIL ");
				response.setStatus(0);
				response.setRemaining_attempts (remaining_attempts);
			}
		}

		return response ;
	}

	public Custom_Response 	update_forget_passwod(  User_Password_Change_Info password_info)
	{

		String username = password_info.getUser_name() ;

		String password =  password_info.getNew_password();

		String encoded_password =  this.password_encoder.encode ( password);

		return null;
	}

}
