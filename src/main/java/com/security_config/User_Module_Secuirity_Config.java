package com.security_config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.jwt_checking.Jwt_Request_Filter;
import com.user_login_module.service.My_User_Details_Service;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class User_Module_Secuirity_Config {

	@Autowired
	My_User_Details_Service user_details_service;

	@Autowired
	Jwt_Request_Filter jwt_request_filter;

	@Autowired
	@Qualifier("bcrypt")
	PasswordEncoder password_encoder;

	@Bean
	public DaoAuthenticationProvider get_details( )
	{
			DaoAuthenticationProvider dao = new DaoAuthenticationProvider() ;
			dao.setPasswordEncoder(password_encoder);
			dao.setUserDetailsService( user_details_service);
			return  dao;
	}
	
	@Value("allowed_origins")
	List<String>ALLOWED_ORIGINS;
	
@Bean
protected SecurityFilterChain get_http_filter ( HttpSecurity http ) throws Exception
{
	 http
	 .cors( c  -> {
		
		 CorsConfigurationSource config_source = ( req )->
		 {
			 CorsConfiguration config = new CorsConfiguration();
			 config.setAllowedOrigins(ALLOWED_ORIGINS);
			 return config;
		 };
		 c.configurationSource(config_source);
	 }
		 )
	
	 .authorizeRequests()
	 .mvcMatchers("/change_password").hasAnyRole("ADMIN","USER")
	.mvcMatchers("/get_users").hasRole("ADMIN")
	.mvcMatchers("/insert_user").hasRole("ADMIN")
	.mvcMatchers("/upload_files").hasRole("ADMIN")
	
	.mvcMatchers("/categories/get_products").permitAll()
	.mvcMatchers("/categories/get_electronics").permitAll()
	.antMatchers("/categories/home_page_data").permitAll()
	.mvcMatchers("/categories/update_category").hasRole("ADMIN")
	.mvcMatchers("/categories/get_categories").hasRole("ADMIN")
	.mvcMatchers("/categories/add_category").hasRole("ADMIN")
	
	.mvcMatchers("/products/add_products").hasRole("ADMIN")//products
	.mvcMatchers("/products/get_products").hasRole("ADMIN")//products
	.mvcMatchers("/products/update_product").hasRole("ADMIN")
	.mvcMatchers("/products/get_product").permitAll()
	.mvcMatchers("/products/get_image/{uuid}" , "/products/get_side_image/{uuid}").permitAll()
	
	.mvcMatchers("/get_cart_items/{user_name}").hasRole("ADMIN")
	.mvcMatchers("/add_to_cart").hasAnyRole("USER" , "ADMIN")
	.mvcMatchers("/get_carts_data").hasAnyRole( "USER" , "ADMIN")
	.mvcMatchers("/remove_cart_item").hasAnyRole("USER" , "ADMIN")
	.mvcMatchers("/remove_all_cart_item/{username}").hasRole("USER")
	.mvcMatchers("/brand_category/get_brand_category_based_products").permitAll()
	
	.mvcMatchers("/brand_category/get_brand_based_products").permitAll()
	.mvcMatchers("/brand_category/get_price_brand_based_products").permitAll()
	.mvcMatchers("/brand_category/get_brand_price_sorting_options").permitAll()
	.mvcMatchers("/brand_category/get_price_based_products").permitAll()
	
	.mvcMatchers("/login_check").permitAll()
	.mvcMatchers("/validate_user_info").permitAll()
	.mvcMatchers("/validate_otp").permitAll()
	.mvcMatchers("/resend_otp/{username}").permitAll()
	.mvcMatchers("/check_otp_expiration").permitAll()
	.mvcMatchers( "/forget_password").permitAll()
	.mvcMatchers("/forget_password/update_password").permitAll()
	.mvcMatchers("/forget_password/validate_otp").permitAll()
	.mvcMatchers("/forget_password/resend_otp/{username}").permitAll()
	
	.mvcMatchers("/jwt").permitAll()
	.mvcMatchers("/input/**").permitAll()
	
	.mvcMatchers("/payments/request").hasAnyRole("USER" ,"ADMIN")
	.mvcMatchers("/payments/bulk_request").hasAnyRole("USER","ADMIN")
	.mvcMatchers("/payments/remove_payment_details").hasAnyRole("USER","ADMIN")
	.mvcMatchers("/orders/get_order_items").hasAnyRole("USER" , "ADMIN")
	.mvcMatchers("/orders/cancel_order_items").hasAnyRole("USER","ADMIN")
	.mvcMatchers("/orders/get_canceled_items").hasAnyRole("USER","ADMN")
	.mvcMatchers("/update_quantity").hasAnyRole("USER","ADMIN")
	
	.anyRequest().authenticated()
	.and()
	.sessionManagement()
	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	.and()
	.addFilterBefore(jwt_request_filter, UsernamePasswordAuthenticationFilter.class);
	 return http.build();
}

}