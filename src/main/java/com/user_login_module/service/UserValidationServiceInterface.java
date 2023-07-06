package com.user_login_module.service;

import com.security_config.Custom_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Otp_Data;

public interface UserValidationServiceInterface {
	
	Custom_Response check_for_otp_expiration( String username );	
	
	Otp_Validation_Resend_Data validate_otp(User_Otp_Data user_info);

}
