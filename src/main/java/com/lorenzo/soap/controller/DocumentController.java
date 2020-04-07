package com.lorenzo.soap.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin( "*" )
@RestController
@RequestMapping( "/document" )
public class DocumentController {
	
	@GetMapping( value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE )
	public byte[] duildPdf() {
		
	}
}
