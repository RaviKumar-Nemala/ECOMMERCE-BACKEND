package com.jwt_module;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Jwt_Data_Return {

	public String jwt_token ;

	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}



}
