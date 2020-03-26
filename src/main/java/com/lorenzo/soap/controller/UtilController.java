package com.lorenzo.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lorenzo.soap.commons.BaseResponse;
import com.lorenzo.soap.commons.ResponseObject;
import com.lorenzo.soap.enums.ErrorResponseEnum;
import com.lorenzo.soap.service.EmailService;

@RestController
@CrossOrigin( origins = "*" )
@RequestMapping( "/util" )
public class UtilController extends BaseResponse {
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping( "/getProperties" )
	public ResponseObject consumer() {
		try {
			
			return setResponseObject( emailService.getProperties(), HttpStatus.OK.value(), "");
			
		} catch( Exception err ) {
			return setResponseObject( null, ErrorResponseEnum.CONSUME_WEB_SERVICE.getCodeError(), err.getMessage() );
		}
	}
}
