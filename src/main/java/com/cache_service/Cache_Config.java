package com.cache_service;

import java.time.Duration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brand_category_module.Brand_Category_Input;
import com.brand_category_module.Brand_Price_Input;
import com.brand_category_module.Price_Category_Input;
import com.products_module.Products_Data_Send;
import com.products_module.Products_List_Holder;

@Configuration
public class Cache_Config {
			
	@Value("${cache.images_cache}")
	public String IMAGES_CACHE ;
	
	@Value("${cache.category_cache}")
	public String CATEGORY_BASED_PRODUCTS_CACHE ;
	
	@Value("${cache.products_cache}")
	public String PRODUCT_DETAILS_CACHE;
	
	@Value("${cache.brand_based_products_cache}")
	public String BRANDS_BASED_PRODUCTS_CACHE;
	
	@Value("${cache.brand_category_cache}")
	public String BRAND_CATEGORY_BASED_PRODUCTS_CACHE;
	
	@Value("${cache.price_category_cache}")
	public String PRICE_CATEGORY_BASED_PRODUCTS_CACHE;
	
	@Value("${cache.price_brand_cache}")
	public String PRICE_BRAND_BASED_PRODUCTS_CACHE;
	
	// CONFIGURE THESE VALUES FROM APPLICATION.PROPERTIES FILES
	private long BRAND_BASED_TTL_VALUE;
	
	private long BRAND_CATEGORY_TTL_VALUE;
	
	private long PRICE_BRAND_TTL_VALUE;
	
	private long PRICE_CATEGORY_TTL_VALUE;
	
	private long IMAGES_TTL_VALUE ;
	
	private long CATEGORY_TTL_VALUE ;
	
	private long PRODUCTS_TTL_VALUE;
	
	
	private CacheManager get_cache_manager()
	{
		
		CachingProvider cache_provider = Caching.getCachingProvider();
		
		return cache_provider.getCacheManager();
	}
	
	private  javax.cache.configuration.Configuration<?, ?>  get_brand_based_cache_config()
	{
		
			CacheConfiguration<String,  Products_List_Holder > brand_based_cache=
				
				CacheConfigurationBuilder.newCacheConfigurationBuilder( String.class,  Products_List_Holder.class,  ResourcePoolsBuilder.heap(20).build())
				
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(BRAND_BASED_TTL_VALUE))).build();
		
		javax.cache.configuration.Configuration<String, Products_List_Holder>brands_config = Eh107Configuration.fromEhcacheCacheConfiguration(brand_based_cache);
		
		return brands_config;
	}

	private  javax.cache.configuration.Configuration<?, ?>  get_brand_category_based_config()
	{
		
			CacheConfiguration< Brand_Category_Input, Products_List_Holder> brand_category_based_cache=
				
				CacheConfigurationBuilder.newCacheConfigurationBuilder( Brand_Category_Input.class,Products_List_Holder.class, ResourcePoolsBuilder.heap(30).build())
				
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(BRAND_CATEGORY_TTL_VALUE))).build();
		
		javax.cache.configuration.Configuration<Brand_Category_Input, Products_List_Holder>brands_config = Eh107Configuration.fromEhcacheCacheConfiguration(brand_category_based_cache);
		
		return brands_config;
	}

	
	private  javax.cache.configuration.Configuration<?, ?>  get_price_brand_based_config()
	{
		
			CacheConfiguration< Brand_Price_Input, Products_List_Holder> price_brand_based_cache=
				
				CacheConfigurationBuilder.newCacheConfigurationBuilder( Brand_Price_Input.class,Products_List_Holder.class, ResourcePoolsBuilder.heap( 30).build())
				
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(PRICE_BRAND_TTL_VALUE))).build();
		
		javax.cache.configuration.Configuration<Brand_Price_Input, Products_List_Holder>price_brands_config = Eh107Configuration.fromEhcacheCacheConfiguration(price_brand_based_cache);
		
		return price_brands_config;
	}

	private  javax.cache.configuration.Configuration<?, ?>  get_price_category_based_config()
	{
		
			CacheConfiguration< Price_Category_Input, Products_List_Holder> price_category_based_cache=
				
				CacheConfigurationBuilder.newCacheConfigurationBuilder( Price_Category_Input.class,Products_List_Holder.class, ResourcePoolsBuilder.heap( 30 ).build())
				
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(PRICE_CATEGORY_TTL_VALUE))).build();
		
		javax.cache.configuration.Configuration<Price_Category_Input, Products_List_Holder> price_category_config = Eh107Configuration.fromEhcacheCacheConfiguration(price_category_based_cache);
		
		return price_category_config;
	}
	
	private  javax.cache.configuration.Configuration<?, ?>  get_image_cache_config()
	{
		
			CacheConfiguration< String, byte[]>images_cache_config = 
				
				CacheConfigurationBuilder.newCacheConfigurationBuilder( String.class,byte[].class, ResourcePoolsBuilder.heap(100).build())
				
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(IMAGES_TTL_VALUE))).build();
		
		javax.cache.configuration.Configuration<String, byte[]>images_configuration = Eh107Configuration.fromEhcacheCacheConfiguration(images_cache_config);
		
		return images_configuration; 
	}
	
	private javax.cache.configuration.Configuration<?,?> get_category_cache_config()
	{
		
		CacheConfiguration< String , Products_List_Holder>category_based_data_cache_config =  
				CacheConfigurationBuilder.newCacheConfigurationBuilder( String.class ,Products_List_Holder.class , ResourcePoolsBuilder.heap(10).build())
				.withExpiry( ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(CATEGORY_TTL_VALUE))).build();
				
		javax.cache.configuration.Configuration<String, Products_List_Holder> category_cache_configuration  = Eh107Configuration.fromEhcacheCacheConfiguration( category_based_data_cache_config);
		
		
		return category_cache_configuration;
	}
	
	private  javax.cache.configuration.Configuration<?, ?>  get_products_cache()
	{
		CacheConfiguration<String, Products_Data_Send>products_data  =  
				CacheConfigurationBuilder.newCacheConfigurationBuilder( String.class ,Products_Data_Send.class , ResourcePoolsBuilder.heap(50).build())
				.withExpiry( ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(PRODUCTS_TTL_VALUE))).build();

		javax.cache.configuration.Configuration<String, Products_Data_Send> products_cache_configuration  = Eh107Configuration.fromEhcacheCacheConfiguration( products_data);
				

		return products_cache_configuration;
	}
	
	@Bean
	public CacheManager load_all_cache_configs () 
	{
		try {
		CacheManager cache_manager = this.get_cache_manager();
		
		cache_manager.createCache( IMAGES_CACHE ,  this.get_image_cache_config());
		
		cache_manager.createCache( CATEGORY_BASED_PRODUCTS_CACHE , this.get_category_cache_config());
		
		cache_manager.createCache( PRODUCT_DETAILS_CACHE , this.get_products_cache());
		
		cache_manager.createCache(BRANDS_BASED_PRODUCTS_CACHE, this.get_brand_based_cache_config());
		
		cache_manager.createCache( BRAND_CATEGORY_BASED_PRODUCTS_CACHE, this.get_brand_category_based_config());
		
		cache_manager.createCache(PRICE_CATEGORY_BASED_PRODUCTS_CACHE,  this.get_price_category_based_config());
		
		cache_manager.createCache(PRICE_BRAND_BASED_PRODUCTS_CACHE, this.get_price_brand_based_config());
		
		System.out.println("CALLED");
		
		return cache_manager;
		}
		
		catch (Exception e )
		{
			System.out.println ( e.getMessage());
			return null;
			
		}
	}
	
}
