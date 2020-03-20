package com.lorenzo.soap.service;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SOAPConnector extends WebServiceGatewaySupport {
	
	public Object callWebService( String urlService, Object request) throws Exception {
		try {
			
			return getWebServiceTemplate().marshalSendAndReceive( urlService, request );
			
		} catch( Exception err ) {
			throw new Exception( "Error al consumir el servicio: " + urlService );
		}
	}
}