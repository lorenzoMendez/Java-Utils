package com.lorenzo.soap.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;

import com.lorenzo.soap.algorithms.RijndaelAlgorithm;

public class EncryptorUtil {
	
	/**
	 * Ecrypta la cadena message con la llave secret
	 * https://www.jokecamp.com/blog/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#java
	 * */
	public static String SHA256Generator( final String message, final String key ) throws Exception {
		try {
		     Mac sha256_HMAC = Mac.getInstance( "HmacSHA256" );
		     SecretKeySpec secret_key = new SecretKeySpec( key.getBytes(), "HmacSHA256");
		     sha256_HMAC.init( secret_key );

		     String hash = new String( Hex.encode( sha256_HMAC.doFinal( message.getBytes() ) ) );
		     		     
		     return hash;
			
		} catch( Exception err ) {
			throw new Exception( "Error al generar el código Hash: " + err.getMessage() );
		}
	}
	
	public static String SHA1Generator( final String message, final String key ) throws Exception {
		try {
		     Mac hmacSha1 = Mac.getInstance( "HmacSHA1" );
		     SecretKeySpec secret_key = new SecretKeySpec( key.getBytes(), "HmacSHA1");
		     hmacSha1.init( secret_key );

		     String hash = new String( Hex.encode( hmacSha1.doFinal( message.getBytes() ) ) );
		     		     
		     return hash;
			
		} catch( Exception err ) {
			throw new Exception( "Error al generar el código Hash: " + err.getMessage() );
		}
	}
	
	public static String RijndaelGenerator( String message, String key ) throws Exception {
		try {
			
			if( message == null || message.equals( "" ) ) {
				return message;
			} else if( key == null || key.equals( "" ) ) {
				throw new Exception( "Error! La llave de encriptamiento esta vacia." );
			}
			
			return RijndaelAlgorithm.setEncrypt( message, key );
			
		} catch( Exception err ) {
			throw new Exception( err.getMessage() );
		}
	}
}
