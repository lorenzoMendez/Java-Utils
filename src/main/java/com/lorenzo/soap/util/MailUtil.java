package com.lorenzo.soap.util;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class MailUtil {
	
	// Your mail server config SMTP
	// This params must be storage in database
    private static final String SERVIDOR_SMTP = "your server provider";
    private static final int PORTA_SERVIDOR_SMTP = 587;
    private static final String CONTA_PADRAO = "reoieortiero";
    private static final String SENHA_CONTA_PADRAO = "smtp server password";
    private static final String from = "who send the email";
    
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	private static Pattern pattern;
    
	public static void sendEmail( final String to, final String subject, final String messageContent, final byte[] pdf ) throws Exception {
				
		try {
			Message message = new MimeMessage( getSession() );
			
			MimeBodyPart textBodyPart = new MimeBodyPart();
			
			textBodyPart.setContent( messageContent, "text/html; charset=utf-8" );
            
            DataSource dataSource = new ByteArrayDataSource( pdf, "application/pdf" );
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler( new DataHandler( dataSource ) );
            pdfBodyPart.setFileName( "my_pdf_document.pdf" );

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
            
			message.setRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
			message.setFrom( new InternetAddress( from ) );
			message.setSubject( subject );
			
			message.setContent( mimeMultipart );
			message.setSentDate( new Date() );
			
			Transport.send( message );
			
		} catch (final MessagingException ex) {
			throw new Exception( "Error sendig the email." );
		}
	}
	
	private static Session getSession() {
		Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SERVIDOR_SMTP);
        properties.put("mail.smtp.port", PORTA_SERVIDOR_SMTP);
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(CONTA_PADRAO, SENHA_CONTA_PADRAO);
			}
		});
		return session;
	}

	public static boolean validateEmail( String email ) throws Exception {
		if( email == null || email.equals( "" ) ) {
			throw new Exception("Email is null :/");
		} else {
			pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
			return pattern.matcher(email).matches();
		}
	}
}
