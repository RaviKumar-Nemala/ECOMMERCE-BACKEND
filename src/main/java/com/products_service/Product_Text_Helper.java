package com.products_service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public class Product_Text_Helper {

	Map< String , String  >  filter( String values[] )throws  Exception
	{
		Map < String , String > mp = new LinkedHashMap<>();


		for ( String value : values )
		{

			String entries [] = value.split(":");
			String key = entries[0];
			key = key.trim();
			key = key.replaceAll("[\\r\\t]", "");
			//System.out.print ( key.trim());
			String _value = entries[1];
			_value = _value.trim();
			_value = _value.replaceAll("[\\r\\t]", "");
			//System.out.println(_value);
			mp.put( key, _value);
		}


		return mp;
	}
	public Map< String ,String >process_text ( String text_string)throws Exception
	{
		text_string = text_string.trim();
		String values[] = text_string .split("[\\n]");

		return filter ( values);

	}


	//takes the text content in the foramt of the string
	//returns the map <String , String >  key value pairs
	public Map < String , String >  convert__to_text ( String text_data)throws Exception
	{

		Map < String , String >  product_spec = this.process_text(text_data);

		return product_spec;

	}
}
