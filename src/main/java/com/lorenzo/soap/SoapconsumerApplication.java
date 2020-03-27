package com.lorenzo.soap;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.lorenzo.soap.commons.ConfigurationLoader;

@SpringBootApplication
public class SoapconsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapconsumerApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	/*
	 * How read properties values
	 * 
	 * */
	@Bean
	public Properties initProperties() throws Exception {
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream("src/main/resources/configuration.properties") ) {
            properties.load( input );
            properties.setProperty( "KEY_FIRST", new String( properties.getProperty("KEY_FIRST").getBytes( "ISO8859_1" ) ) );
            properties.setProperty( "KEY_SECOND", new String( properties.getProperty("KEY_SECOND").getBytes( "ISO8859_1" ) ) );
            
            /*
             * Setter singleton atributes
             * */
            ConfigurationLoader loader = ConfigurationLoader.getInstance();
            
            loader.setValue1( properties.getProperty( "KEY_FIRST" ) );
            loader.setValue2( properties.getProperty( "KEY_SECOND" ) );
            
            return properties;
        } catch (Exception ex) {
            throw new Exception( "Error reading properties file." );
        }
	}
}
