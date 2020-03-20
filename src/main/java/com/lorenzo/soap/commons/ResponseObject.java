package com.lorenzo.soap.commons;

import java.io.Serializable;

public class ResponseObject implements Serializable {

	private static final long serialVersionUID = 8272842752892614711L;
	private Object data;
	private int codeHttpStatus;
	private String messageError;
	
	public ResponseObject() {
		super();
	}
	
	public ResponseObject( Object data, int codeHttpStatus, String error ) {
		this.data = data;
		this.codeHttpStatus = codeHttpStatus;
		this.messageError = error;
	}
	
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * @return the codeHttpStatus
	 */
	public int getCodeHttpStatus() {
		return codeHttpStatus;
	}
	
	/**
	 * @param codeHttpStatus the codeHttpStatus to set
	 */
	public void setCodeHttpStatus(int codeHttpStatus) {
		this.codeHttpStatus = codeHttpStatus;
	}
	
	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
	}
	
	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}
}
