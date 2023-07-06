package com.user_login_module.service;
import java.util.Map;

import com.custom_exceptions.Invalid_User_Details_Exception;
import com.jwt_module.Jwt_Data_Return;
import com.security_config.Custom_Response;
import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Info;

public interface UserCreationServiceInterface {
	
	boolean create_user(Map<String,String>user_details);
	
	Jwt_Data_Return generate_jwt(String username);

	Custom_Response store_usr_in_db( User_Info data ) throws Invalid_User_Details_Exception;
	
	boolean remove_userdetails(String username);

	Otp_Validation_Resend_Data resend_otp(String username);

	
}
