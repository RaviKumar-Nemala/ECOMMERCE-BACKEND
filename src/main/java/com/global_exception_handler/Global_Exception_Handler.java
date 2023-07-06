package com.global_exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.custom_exceptions.Carts_Data_Not_Found;
import com.custom_exceptions.Duplicate_Cart_Item_Exception;
import com.custom_exceptions.Invalid_Cart_Details_Exception;
import com.custom_exceptions.Invalid_Jwt_Token_Exception;
import com.custom_exceptions.Invalid_User_Details_Exception;
import com.custom_exceptions.Jwt_Expired_Exception;
import com.custom_exceptions.products.Invalid_Product_Uuid_Exception;
import com.custom_exceptions.products.Product_Details_Not_Found_Exception;


/**
 * 
 * @author Ravi kumar
 * 
 * handles all type of the user defined exceptions has been reached to the controller layer
 * 
 * when ever any type of the exception occured in the dao layer then that particular exception is handleed by corresponding exception classes  and also thrown to the caller function
 *
 *	In the service layer we are not handling any type of the exceptions the raised exceptions in the dao layer will be thrown to the controller layer then this global exception handler will take care of those exceptions and sends the  corresponding response to the front end 
 */

@RestControllerAdvice
public class Global_Exception_Handler extends ResponseEntityExceptionHandler{


	public ResponseEntity<?> send_response ( String message ,  HttpStatus http_status)
	{
		System.out.println ("INSIDE OF THE GLOBAL EXCEPTION HANDLER");
		
		return ResponseEntity.badRequest().body( message);
		
	}
	
	private void print_msg ( String message )
	{
		System.out.println ( message);
		
	}
	
	@ExceptionHandler( Invalid_Cart_Details_Exception.class)
	public ResponseEntity<?> invalid_cart_details ( Invalid_Cart_Details_Exception exp)
	{
		
		String message = exp.getMessage();
			
		print_msg ( message ) ;
	
		return ResponseEntity.badRequest().body(message);
	}
	
	
	@ExceptionHandler( Duplicate_Cart_Item_Exception.class)
	public ResponseEntity<?> cart_item_exsited_exp ( Duplicate_Cart_Item_Exception dup) 
	{
		String message =  dup.getMessage();
		
		System.out.println ( message) ; 
		
		return new ResponseEntity<String>( message , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler( Carts_Data_Not_Found.class)
	public ResponseEntity<?> handle_empty_carts ( Carts_Data_Not_Found cat)
	
	{
			String message = cat.getMessage();
			
			return new ResponseEntity<String> ( message , HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler( Invalid_Product_Uuid_Exception.class)	
	public ResponseEntity<?>handle_invalid_uuid ( Invalid_Product_Uuid_Exception inv )
	{
		return new ResponseEntity<String> ( inv.getMessage() ,  HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler( Product_Details_Not_Found_Exception.class)
	public ResponseEntity<?>handle_empty_products ( Product_Details_Not_Found_Exception pr )
	{
		return new ResponseEntity<String>( pr.getMessage() , HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler( Jwt_Expired_Exception.class)
	public ResponseEntity<?>handle_jwt_expiration ( Jwt_Expired_Exception  exp )
	{
		
		return new ResponseEntity<String>("SESSION HAS EXPIRED PLEASE LOGIN AGAIN"  , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler( Invalid_Jwt_Token_Exception.class)
	public ResponseEntity<?> handle_jwt_invalidation ( Invalid_Jwt_Token_Exception  exp )
	{
		return new ResponseEntity<String>("JWT TOKEN IS NULL OR INVALID PLEASE LOGIN AGAIN" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler( Invalid_User_Details_Exception.class)
	public ResponseEntity<?> invalid_user_details( Invalid_User_Details_Exception exp)
	{
		
		return new ResponseEntity<String>("USER DETAILS ARE NOT IN CORRECT FORMAT" , HttpStatus.PRECONDITION_FAILED);
	}

}
