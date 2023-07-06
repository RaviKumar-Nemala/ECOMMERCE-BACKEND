package com.jwt_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jwt_checking.Authentication_Request;
import com.jwt_checking.Jwt_Controller;
import com.jwt_checking.Jwt_Util;
import com.jwt_module.Jwt_Data_Return;

@Service
public class Jwt_Service {

	@Autowired
	Jwt_Controller jwt_controller;

	@Autowired
	Jwt_Util jwt_util;

	@Autowired
	Authentication_Request auth_request ;

	@Autowired
	Jwt_Data_Return jwt_data;

	@Autowired
	private UserDetailsService  userdetails_service;

	public Jwt_Data_Return get_jwt(UserDetails  user_details )
	{

		String jwt_token =  null;

		auth_request.setUsername( user_details.getUsername());

		auth_request.setPassword(user_details.getPassword());


		ResponseEntity<String> response  =  jwt_controller.jwt_entry( auth_request);

		HttpStatus http_status = response.getStatusCode();

		if( http_status.is2xxSuccessful())
		{
				jwt_token =  response.getBody();
		}

		this.jwt_data.setJwt_token(jwt_token);

		return jwt_data;

	}

	public Jwt_Data_Return generate_jwt (  UserDetails user_details)
	{

		String jwt_token = this.jwt_util.generateToken(user_details);

		this.jwt_data.setJwt_token(jwt_token);

		return jwt_data;

	}


	public boolean  validate_jwt ( String jwt )
	{
		try {
		String username = this.jwt_util.extractUsername(jwt);
		
		UserDetails user_details = this.userdetails_service.loadUserByUsername(username);

		boolean validation_result =  this.jwt_util.validateToken(jwt, user_details);

		return  validation_result;
		}
		catch ( Exception e )
		{
			return false;
		}
	}

	public String get_username ( String jwt)
	{
		try {
		String username  = this.jwt_util.extractUsername(jwt);
		
		return username ;
		}
		catch ( Exception e )
		{
			return null;
		}
	}

}
