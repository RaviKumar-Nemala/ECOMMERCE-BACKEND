package com.user_login_module.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security_config.Custom_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Otp_Data;
import com.user_login_module.dao.UserAuthenticationDaoInterface;

@Service
public class User_Validation_Service extends User_Otp_Service implements UserValidationServiceInterface{


	@Autowired
	private UserAuthenticationDaoInterface  userAuthenticationDaoInterface;

	@Autowired
	private Otp_Validation_Resend_Data  otp_validation_resend_data;

	//if the user is existed with the given user name
		// then return true
		//otherwise return false
		public boolean is_account_existed ( String username )
		{
			if ( userAuthenticationDaoInterface.find_by_user_name(username) != null )
			{
				return true ;
			}

			else
			{
				return false ;
			}

		}
		
		@Override
		public  Custom_Response check_for_otp_expiration( String user_name )
		{
			
			Custom_Response cr = new Custom_Response () ;
			
			LocalDateTime curr_date_time  = LocalDateTime.now();
			
			if( super.is_otp_time_expired(user_name) )
			{
				cr.setMessage("OTP VALIDATION TIME HAS EXPIRED");
				cr.setStatus(0);
			}
			else 
			{
				cr.setMessage("VALIDATION TIME HAS NOT EXPIRED");
				cr.setStatus(1);
			}
			
			return cr;
		}
		
		@Override
		public Otp_Validation_Resend_Data validate_otp (  User_Otp_Data  otp_details )
		{

			String username = otp_details.getUsername();

			boolean result =  super.is_valid_otp(username, otp_details.getOtp());

			if ( result )
			{
				otp_validation_resend_data.setStatus(1);
				otp_validation_resend_data.setMessage("USER ENTERED THE CORRECT OTP");
				otp_validation_resend_data.setRemaining_attempts(1);
			}
			else
			{
				super.incr_no_of_attemps(username);
				int remaining_attempts = super.get_remaining_validation_attempts(username);
				otp_validation_resend_data.setStatus( 0 );
				otp_validation_resend_data.setMessage("USER ENTERED THE INVALID OTP");
				otp_validation_resend_data.setRemaining_attempts(remaining_attempts);
			}
			return otp_validation_resend_data;
		}
}
