package com.lorenzo.soap.enums;

public enum ErrorResponseEnum {
	
	AUTHENTICATION_ERROR(100),
	CONSUME_WEB_SERVICE(300),
	BAD_REQUEST(400),
	FORM_INVALID(500),
	SAVE_OBJECT(600),
	EXIST_DATA(700),
	VALIDATE_CORREO(800),
	UNAVAILABLE_CREDIT(900);
	
	private final int codeError;
	
	private ErrorResponseEnum(int codeError) {
		this.codeError = codeError;
	}
	
	public int getCodeError() {
		return codeError;
	}
}