package com.carts_module;

public class Carts_Response {

	 private String message ;

	 private int status;

	 private boolean ok;

	 public boolean isOk()
	 {
		 return this.ok;
	 }

	public String getMessage(){
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if( status == 1 )
		{
			this.status= 1;

			this.ok = true;

		}
		else
		{
			this.status = 0;
			this.ok = false;
		}

	}


}
