package com.user_login_module;

import java.time.LocalDateTime;

public class Otp_Code_Info {

	private int otp_val ;
	
	private LocalDateTime expiration_date;

	public Otp_Code_Info(  int otp_val , LocalDateTime expiration_time)
	{
		this.otp_val = otp_val;
		this.expiration_date = expiration_time;
	}

	public int getOtp_val() {
		return otp_val;
	}

	public LocalDateTime getExpiration_date() {
		return expiration_date;
	}
	
}

