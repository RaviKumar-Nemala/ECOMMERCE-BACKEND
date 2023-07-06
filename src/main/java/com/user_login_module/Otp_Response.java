package com.user_login_module;

import org.springframework.stereotype.Component;

/**
 * @author Ravi kumar
 *
 *
 *	when ever we are sending the otp to email
 * on the front end we need to set up the timer for the resending the otp
 * there may be a chaces that the timer in the front end  is going to be manipulated
 * inorder to avoid that we set the time value in this class then send this to the frontend
 */


@Component
public class Otp_Response {

	// time in seconds
	public  final  int  WAITING_TIME =  120;


	public String response_message ;



	public  int getWaitingTime() {
		return WAITING_TIME;
	}


	public String getResponse_message() {
		return response_message;
	}


	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}




}
