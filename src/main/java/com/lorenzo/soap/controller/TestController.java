package com.lorenzo.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lorenzo.soap.commons.BaseResponse;
import com.lorenzo.soap.commons.ConfigurationLoader;
import com.lorenzo.soap.commons.ResponseObject;
import com.lorenzo.soap.enums.ErrorResponseEnum;
import com.lorenzo.soap.service.SOAPConnector;

@RestController
@RequestMapping( "/test" )
public class TestController extends BaseResponse {
	
	@Autowired
	private SOAPConnector connector;
	
	@GetMapping( "/consumer" )
	public ResponseObject consumer() {
		try {
			
			connector.callWebService( "https://service/parameter" , new Object() );
			// ProcesaCompraOLResponse procesaCompra = (ProcesaCompraOLResponse) connector.callWebService( urlService + "/procesaCompraOL", procesaPagosObject );
			return setResponseObject( 1, HttpStatus.OK.value(), "");
			
		} catch( Exception err ) {
			return setResponseObject( null, ErrorResponseEnum.CONSUME_WEB_SERVICE.getCodeError(), err.getMessage() );
		}
	}
	
	@GetMapping( "/singleton" )
	public ResponseObject singletonTest() {
		try {
			
			//System.out.println( configLoader.getValue1() );
			
			return setResponseObject( 1, HttpStatus.OK.value(), "");
			
		} catch( Exception err ) {
			return setResponseObject( null, ErrorResponseEnum.CONSUME_WEB_SERVICE.getCodeError(), err.getMessage() );
		}
	}
	
	@GetMapping( "/welcome" )
	public ResponseObject welcome() {
		try {
			
			ConfigurationLoader loader = ConfigurationLoader.getInstance();
			
			//System.out.println( configLoader.getValue1() );
			
			return setResponseObject( "WELCOME TO TEST CONTROLLER", HttpStatus.OK.value(), "");
			
		} catch( Exception err ) {
			return setResponseObject( null, ErrorResponseEnum.CONSUME_WEB_SERVICE.getCodeError(), err.getMessage() );
		}
	}
}
