package com.custom_exceptions;

public class Empty_Brand_Names_Exception extends RuntimeException {

		public Empty_Brand_Names_Exception() {
			super ( );
		}
		
		public Empty_Brand_Names_Exception ( String message ) 
		{
			super ( message ) ;
		}
		
}
