package com.lorenzo.soap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.lorenzo.soap.service.SOAPConnector;

/*
 * Config the cliente first
 * 
 * */

@Configuration
public class SoapClientConfig {
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		
		// Define your wsdl location
		marshaller.setPackagesToScan( "com.lorenzo.soap" );
		
		return marshaller;
	}
	
	@Bean
	public SOAPConnector soapConnector( Jaxb2Marshaller marshaller ) {
		
		SOAPConnector client = new SOAPConnector();
		
		client.setDefaultUri( "http://webservice.com.mx/" );
		client.setMarshaller( marshaller );
		client.setUnmarshaller( marshaller );
		
		return client;
	}
}