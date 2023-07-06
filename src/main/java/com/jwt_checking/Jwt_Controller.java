package com.jwt_checking;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Jwt_Controller {

	@Autowired
	UserDetailsService user_details_service;


	@Autowired
	DaoAuthenticationProvider auth;

	@Autowired
	Jwt_Util jwt_util;

	final String BAD_CREDENTIALS = "INCORRECT USERNAME OR PASSWORD ";


	final String INVALID_PASSWORD = "INCORRECT PASSWORD";


	// used to check weather the username is valid or not
	// specially used for the valid message to be displayed on the user
	// if  user name correct then INVALID_PASSWORD_MSG will be displayed'
	// if the user name is not corrcet then the BAD_CREDENTIALS msg will be displayed

	private  ResponseEntity<String> helper (  Authentication_Request  auth_reqest)
	{

		String username =  auth_reqest.getUsername() ;

		UserDetails user_details = this.user_details_service.loadUserByUsername( username );

		if ( user_details != null )
		{
			return new ResponseEntity< >(INVALID_PASSWORD , HttpStatus.BAD_REQUEST);

		}
		else
		{
			return new ResponseEntity< >  ( BAD_CREDENTIALS , HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/jwt")
	@ResponseBody
	public ResponseEntity<String> jwt_entry ( @RequestBody Authentication_Request auth_req)
	{
		System.out.println("inside of the jwt token demo ") ;

		try {
			auth.authenticate( new UsernamePasswordAuthenticationToken( auth_req.getUsername() , auth_req.getPassword() ) );
		}
		catch ( Exception e )
		{
//			System.out.println("inside of the catch block ");

			return helper(auth_req);
		}

		UserDetails  usr_details = this.user_details_service.loadUserByUsername(auth_req.getUsername() );

		
		System.out.println ( new Date(System.currentTimeMillis() + (10*60*1000)));
		
		String jwt_token = jwt_util.generateToken(usr_details);
		
		Date expired_date = jwt_util.extractExpiration(jwt_token);
		
		System.out.println ( expired_date.toString());
		

//		System.out.println("printing the jwt token " + jwt_token ) ;

		//SecurityContextHolder.getContext().getAuthentication();
	

		return new ResponseEntity<>(jwt_token, HttpStatus.OK);

	}
	
}
