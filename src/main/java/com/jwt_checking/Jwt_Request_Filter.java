package com.jwt_checking;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.custom_exceptions.Jwt_Expired_Exception;

	@Component
	public class Jwt_Request_Filter  extends OncePerRequestFilter
	{

		@Autowired
		Jwt_Util jwtUtil;

		@Autowired
		UserDetailsService usr_details_service;
		
		private void store_in_context ( String jwt_value , UserDetails user_details , HttpServletRequest request , HttpServletResponse response )
		{

			UsernamePasswordAuthenticationToken user_name_pass_token = new UsernamePasswordAuthenticationToken( user_details , null , user_details.getAuthorities());

			user_name_pass_token.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(user_name_pass_token);

			return;
		}
		
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException  {

			final String autherization_header  = request.getHeader("Authorization");

			 String user_name = null;

			String jwt  = null;

			try {
			//System.out.println( "inside of the jwt filter chain ") ;

				if (  autherization_header != null && autherization_header.startsWith("Bearer "))
			{

				jwt  = autherization_header.substring(7);

				try {
				user_name = jwtUtil.extractUsername( jwt ) ;
				System.out.println ( user_name);
				}
				//called when the jwt token time is expired
				catch ( io.jsonwebtoken.ExpiredJwtException e )
				{
//					response.getWriter().println("INVALID USER DETAILS");
				
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					
					response.getWriter().write("HELL");

				
				}
		
				//does not match with the signature computed by the server  and client
				catch ( io.jsonwebtoken.SignatureException e )
				{
					System.out.println ( e.getMessage());
					response.getWriter().println("INVALID USER DETAILS");
				}
				
				catch ( io.jsonwebtoken.MalformedJwtException e )
				{
					response.getWriter().println("INVALID USER DETAILS");
					System.out.println ( e.getMessage());
				}
				catch (  Exception e )
				{
					response.getWriter().println("INVALID USER DETAILS");
					System.out.println ( e.getMessage());
				}
			}

			if ( user_name != null  && SecurityContextHolder.getContext().getAuthentication() == null )
			{
				
					
				UserDetails  usr_details = usr_details_service.loadUserByUsername(user_name);
				
				try {
				if ( jwtUtil.validateToken( jwt , usr_details) )
				{
					this.store_in_context(jwt, usr_details , request , response) ;
				}
				}
				catch ( Exception e )
				{
					response.getWriter().println("INVALID TOKEN");
				}
			}
			filterChain.doFilter(request, response);
			
		}
			
		//incase the jwt token is null we need to handle it 
		catch ( Exception e )
			{
				System.out.println( e.getMessage());
				response.getWriter().println("INVALID TOKEN");
			}

		}
	}
