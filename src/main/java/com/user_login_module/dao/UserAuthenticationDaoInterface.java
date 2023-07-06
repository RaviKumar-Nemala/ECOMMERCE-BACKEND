package com.user_login_module.dao;

import java.util.List;

import com.security_config.Custom_Response;
import com.user_login_module.User_Info;

public interface UserAuthenticationDaoInterface {
	
	boolean update_password(String user_name, String encoded_password);
	
	Custom_Response insert_user_record(User_Info user_info);
	
	User_Info find_by_user_name(String username);

	List<User_Info> get_user_details();
}

