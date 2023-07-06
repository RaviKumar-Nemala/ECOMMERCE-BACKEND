package com.user_login_module.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.user_login_module.Otp_Code_Info;

public class User_Otp_Service {

	//stores the username  and the otp value 
	private static Map< String , Otp_Code_Info> user_otp_details = new HashMap<>();

	//used for number of times we should validate the otp
	private static Map <String , Integer >otp_validation_attempts = new HashMap<>();

	// used for maximum number of times that we can send the otp to the user email
	private static Map<String,Integer>otp_resend_request_attempts = new HashMap<>();

	private static final int MAX_OTP_VALIDATION_ATTEMPTS = 10;

	private static final int MAX_OTP_REQUEST_ATTEMPTS = 5;

	private Random random = new Random();
	
	//inorder to send the otp we choose the 4 digit value
	// min val and max value are used to generate the 4 digits random number
	private final int MIN_VAL = 1000;

	private final int MAX_VAL = 9999;

	protected boolean remove_userdetails ( String username )
	{

		user_otp_details.remove(username);
		otp_validation_attempts.remove( username);
		otp_resend_request_attempts.remove( username );
		return true;
	}

	//if the user naem existed then check the no ofattempts
	//other wise store the username in the map and add no_of_attempts to 1;
	protected boolean check_otp_resend_attempts ( String username )
	{
		if ( otp_resend_request_attempts.containsKey( username))
		{
			int no_of_attempts =  otp_resend_request_attempts.get(username);

			if ( no_of_attempts < MAX_OTP_REQUEST_ATTEMPTS)
			{
					return true;
			}
			else
			{
				System.out.println ( "MAX OTP RESEND ATTEMPTS HAS REACHED");
				return false;
			}
		}
		return false;
	}

	protected void incr_otp_resend_attempts ( String username)
	{

		int no_of_attempts =  1 ;

		if ( otp_resend_request_attempts.containsKey(username))
		{

			 no_of_attempts =  otp_resend_request_attempts.get(username);

			no_of_attempts++;

		}
			otp_resend_request_attempts.put(username, no_of_attempts);

		return;
	}

	protected int  get_remaining_validation_attempts ( String username)
	{

		int current_attempts =  otp_validation_attempts.get(username);
		return MAX_OTP_VALIDATION_ATTEMPTS - current_attempts;

	}

	protected int get_remaining_request_attempts ( String username )
	{
		int current_req_attempts =  otp_resend_request_attempts.get(username);
		return MAX_OTP_REQUEST_ATTEMPTS - current_req_attempts;
	}

	protected  boolean check_otp_attempts ( String username )
	{
		if ( otp_validation_attempts.containsKey( username))
		{
			int no_of_attempts =  otp_validation_attempts.get(username);

			if ( no_of_attempts < MAX_OTP_VALIDATION_ATTEMPTS)
			{
					return true;
			}
			else
			{
				System.out.println ( "MAX OTP RESEND VALIDATION ATTAMEPTS HAS REACHED");
			}
		}
			return false ;
	}
	
	protected  void incr_no_of_attemps (  String username )
	{

		int no_of_attempts =  1 ;
		if ( otp_validation_attempts.containsKey(username))
		{
			 no_of_attempts =  otp_validation_attempts.get(username);
			no_of_attempts++;
		}

			otp_validation_attempts.put(username, no_of_attempts);

		return;
	}

	//returns the 4 digit random value ( otp )
	protected int get_random_number ( )
	{
		return this.random.nextInt( MAX_VAL - MIN_VAL ) + MIN_VAL;
	}

	//used to store the otps and usernames which is used for the validation while sending the email to the user account
	protected void store_otp_details ( String username , int otp )
	{
		
		LocalDateTime date_time = LocalDateTime.now();
		
		date_time  = date_time.plusMinutes(5);
		
		Otp_Code_Info info =  new Otp_Code_Info( otp ,date_time);
		
		user_otp_details.put( username, info );

	}
	
	//returns true if the otp time expired
	//or the email not found in the otp map

	protected  boolean is_otp_time_expired( String user_name )
	{
		LocalDateTime curr_date_time  = LocalDateTime.now();
		
		Otp_Code_Info otp_code_info =  this.user_otp_details.get(user_name);
		
		if ( otp_code_info  == null )
		{
			return true;
		}

		if ( otp_code_info.getExpiration_date().isBefore(curr_date_time))
		{
			this.remove_userdetails(user_name);
			
			return true;
		}
		else 
			return false;
	}
	
	public void store_otp_resend_attemps(String username)
	{
		otp_resend_request_attempts.put(username, 0);
	}
	protected boolean is_valid_otp(  String username , int otp )
	{

		if ( user_otp_details.containsKey(username) )
		{
			int stored_otp =  user_otp_details.get(username).getOtp_val();
			
			//if the otp matched then no issue
			// then increment the no of attempts to one
			// that is why we wrote that line below of this
			if ( stored_otp ==  otp )
			{
 				return true ;
			}
		}
		
		return false;
	}
}
