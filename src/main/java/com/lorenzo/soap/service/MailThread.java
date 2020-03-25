package com.lorenzo.soap.service;

import com.lorenzo.soap.util.MailUtil;

public class MailThread implements Runnable {
	String subject;
	String to;
	String content;
	byte[] pdf;
	
	public MailThread( final String subject, final String to, final String content, final byte[] pdf ) {
		this.subject = subject;
		this.to = to;
		this.content = content;
		this.pdf = pdf;
	}
	
	@Override
	public void run() {
		
		try {
			
			MailUtil.sendEmail( this.to, this.subject, this.content, this.pdf );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
