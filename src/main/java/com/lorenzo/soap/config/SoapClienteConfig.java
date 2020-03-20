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
public class SoapClienteConfig {
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		
		marshaller.setPackagesToScan( "mx.com.credifiel.estadocuenta.paymentwsdl" );
		
		return marshaller;
	}
	
	@Bean
	public SOAPConnector soapConnector( Jaxb2Marshaller marshaller ) {
		
		SOAPConnector client = new SOAPConnector();
		
		client.setDefaultUri( "http://webservice.cifrada.interred.adquira.com.mx/" );
		client.setMarshaller( marshaller );
		client.setUnmarshaller( marshaller );
		
		return client;
	}
}
