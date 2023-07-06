package com.user_login_module;

import org.springframework.stereotype.Component;

/**
 *
 *
 * @author Ravi kumar
 *
 *
 *	 this class is used to send the front end to specify that how
 *	many no of attempts remaining inorder to resend or validate the otp
 */

@Component
public class Otp_Validation_Resend_Data {



	private boolean ok ;

	int remaining_attempts;

	String message ;

	public boolean isOk()
	{
		return this.ok;
	}



	public int getRemaining_attempts() {
		return remaining_attempts;
	}



	public void setRemaining_attempts(int remaining_attempts) {
		this.remaining_attempts = remaining_attempts;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(int status) {

		if ( status == 1 )
		{
			this.ok =  true;
		}
		else
		{
			this.ok =  false;
		}

	}


}
