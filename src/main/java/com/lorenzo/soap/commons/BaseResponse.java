package com.lorenzo.soap.commons;

public class BaseResponse {
	public ResponseObject setResponseObject( Object data, int code, String message ) {
		return new ResponseObject( data, code, message );
	}
}
