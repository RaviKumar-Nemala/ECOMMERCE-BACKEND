package com.email_sending.service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Email_Sending_Service {

	@Value("email_from_address")
	private static   String FROM_ADDR ;

	@Autowired
	private JavaMailSender mail_sender;
	
	
	public boolean send_forget_password_email( String email_address , int otp )
	{
		String html_content = "<span style  = "+ " \"font-size : 20px ; color :black ; font-weight :600; font-family :sans-serif ; padding : 5px\" >" +otp + "</span>" + "<span>" + "  is your otp for reset the password " + "</span>";
		
		try 
		{
			MimeMessage mime_msg = this.mail_sender.createMimeMessage();
			
			mime_msg.setFrom(FROM_ADDR);
			
			mime_msg.setRecipient( MimeMessage.RecipientType.TO, new InternetAddress( email_address));
			
			mime_msg.setContent("PASSWORD RESET" , "text/html; charset = utf-8");
			
			mail_sender.send(mime_msg);
			
			return true;
		}
		catch( Exception e )
		{
			 return false;
		}
	}
	
	
	public boolean send_account_creation_email ( String email_address , int  otp)
	{
		
		String html_content = "<span style  = "+ " \"font-size : 20px ; color :black ; font-weight :600; font-family :sans-serif ; padding : 5px\" >" +otp + "</span>" + "<span>" + "  is your otp for account creation" + "</span>";
		
		try {
		MimeMessage  mime_message = this.mail_sender.createMimeMessage();

		mime_message.setFrom( FROM_ADDR);

		mime_message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email_address));

		mime_message.setContent( html_content ,  "text/html;charset=utf-8");

		mail_sender.send(mime_message);

		System.out.println ( "EMAIL SENT SUCESSFULLY");
		
		return true;

		}
		catch ( Exception e )
		{
			System.out.println( "EXCEPTION ENCOUNTERED WHILE SENDING THE EMAIL  TO THE USER ");
			return false ;
		}
		
	}
}
