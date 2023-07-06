package com.user_login_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Password_Encoder_Config {

	@Bean("bcrypt")
	public BCryptPasswordEncoder get_encoder()
	{
		System.out.println ( "creating the password encoder");
		return new BCryptPasswordEncoder();
	}

}
