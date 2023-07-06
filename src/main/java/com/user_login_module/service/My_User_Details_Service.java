package com.user_login_module.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.user_login_module.User_Info;
import com.user_login_module.dao.UserAuthenticationDaoInterface;

@Primary
@Service("my_user_details_service")
public class My_User_Details_Service implements UserDetailsService {

	@Autowired
	UserAuthenticationDaoInterface userAuthenticationDaoInterface;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user_details = userAuthenticationDaoInterface.find_by_user_name(username);

		return user_details;

	}

	public List< User_Info > get_all_users( )
	{
		return this.userAuthenticationDaoInterface.get_user_details();

	}

}
