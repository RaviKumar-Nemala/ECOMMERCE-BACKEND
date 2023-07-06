package com.user_login_module.service;

import com.user_login_module.Otp_Validation_Resend_Data;
import com.user_login_module.User_Otp_Data;

public interface PasswordChangeServiceInterface {

	boolean check_password(String user_name, String old_password);

	boolean update_password(String user_name , String new_password);
	
	boolean forget_password_handler(String username);

	Otp_Validation_Resend_Data validate_otp(User_Otp_Data otp_info);

	void store_password_update_usr_details(String username);
	
	Otp_Validation_Resend_Data resend_otp(String username);

	boolean is_valid_user(String username);
	
	boolean remove_forget_passwd_user(String username);


}
