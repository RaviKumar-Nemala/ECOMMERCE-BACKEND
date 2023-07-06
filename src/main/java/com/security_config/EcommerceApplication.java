package com.security_config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
		basePackages =
		{
		"com.example.testautowire",
		"com.user_login_module" ,
		"com.user_login_dao_impl",
		"com.user_login_module_service",
		"com.user_login_module.controller",
		"com.carts_module",
		"com.carts_module.controller",
		"com.carts_module.dao",
		"com.carts_module.service",
		"com.categories_module",
		"com.categories_module.controller",
		"com.categories_module.service",
		"com.categories_module.dao",
		"com.products_controller",
		"com.products_dao",
		"com.products_module",
		"com.products_service",
		"com.jwt_checking",
		"com.brand_category_dao",
		"com.brand_category_service",
		"com.brand_category_module",
		"com.brand_category_controller",
		"com.brands_dao",
		"com.email_sending.service",
		"com.jwt_module",
		"com.jwt_service",
		"com.cache_service",
		"com.input_search_controller",
		"com.input_search_module",
		"com.input_search_service",
		"com.global_exception_handler",
		"com.custom_exception",
		"com.payments.controller",
		"com.payments.dao",
		"com.payments.module",
		"com.payments.service",
		"com.orders.controller",
		"com.orders.service",
		"com.orders.dao",
		"com.orders.module"
		}
			  )
@EnableCaching
public class EcommerceApplication {

	public static void main(String[] args) {

		ApplicationContext context =   SpringApplication.run(EcommerceApplication.class, args);

	}

}
