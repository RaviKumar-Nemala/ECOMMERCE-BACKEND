package com.security_config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Custom_Response {

	private String message ;

	private int status ;

	private boolean ok ;

	public boolean isOk()
	{
		return this.ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {

		if ( status == 1 )
		{
			this.ok =  true;
		}
		else
		{
			this.ok =  false;
		}

	}




}
